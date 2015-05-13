package net.csongradyp.badger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongrady.badger.AchievementDefinition;
import net.csongrady.badger.IAchievementController;
import net.csongrady.badger.domain.AchievementType;
import net.csongrady.badger.domain.IAchievement;
import net.csongrady.badger.domain.IAchievementBean;
import net.csongrady.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.domain.achievement.CompositeAchievementBean;
import net.csongradyp.badger.domain.achievement.CounterAchievementBean;
import net.csongradyp.badger.domain.achievement.DateAchievementBean;
import net.csongradyp.badger.domain.achievement.TimeAchievementBean;
import net.csongradyp.badger.domain.achievement.TimeRangeAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.NumberTrigger;
import net.csongradyp.badger.event.EventBus;
import net.csongradyp.badger.event.message.Achievement;
import net.csongradyp.badger.event.message.Score;
import net.csongradyp.badger.persistence.AchievementDao;
import net.csongradyp.badger.persistence.CounterDao;
import net.csongradyp.badger.persistence.entity.AchievementEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class AchievementController implements IAchievementController {

    private static final Logger LOG = LoggerFactory.getLogger(AchievementController.class);

    @Inject
    private CounterDao counterDao;
    @Inject
    private AchievementDao achievementDao;
    @Inject
    private IDateProvider dateProvider;
    private ResourceBundle resourceBundle;

    private AchievementDefinition achievementBundle;
    private String internationalizationBaseName;

    public AchievementController() {
        achievementBundle = new AchievementBundle();
        EventBus.setController(this);
    }

    public void setDefinition(final AchievementDefinition definition) {
        this.achievementBundle = definition;
    }

    public void setInternationalizationBaseName(final String internationalizationBaseName) {
        this.internationalizationBaseName = internationalizationBaseName;
        resourceBundle = ResourceBundle.getBundle(internationalizationBaseName, Locale.ENGLISH);
    }

    public void setResourceBundle(final ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public void setLocale(final Locale locale) {
        resourceBundle = ResourceBundle.getBundle(internationalizationBaseName, locale);
    }

    public Collection<IAchievement> getAll() {
        return achievementBundle.getAll();
    }

    public Collection<IAchievement> getByOwner(final String owner) {
        final Collection<IAchievement> achievementsByOwner = new ArrayList<>();
        final Collection<AchievementEntity> achievementEntities = achievementDao.getByOwner(owner);
        for (AchievementEntity achievementEntity : achievementEntities) {
            final Optional<IAchievement> achievement = achievementBundle.get(achievementEntity.getId());
            if (achievement.isPresent()) {
                achievementsByOwner.add(achievement.get());
            }
        }
        return achievementsByOwner;
    }

    public IAchievement get(final AchievementType type, final String id) {
        return achievementBundle.get(type, id);
    }

    public Map<String, Set<IAchievement>> getAllByEvents() {
        return achievementBundle.getAllByEvents();
    }

    public void checkAll() {
        final Collection<IAchievementUnlockedEvent> unlockableAchievements = check(getAll());
        unlockableAchievements.forEach(this::unlock);
    }

    public void triggerEventWithHighScore(final String event, final Long score) {
        if (isNewHighScore(event, score)) {
            LOG.debug("New highscore submitted!");
            triggerEvent(event, score);
        }
    }

    private boolean isNewHighScore(final String event, final Long score) {
        return counterDao.scoreOf(event) <= score;
    }

    public void triggerEvent(final String event, final Long score) {
        if (isDifferentValueAsStored(event, score)) {
            LOG.debug("Achievement event triggered: {} with score {} ", event, score);
            final Long currentValue = counterDao.setScore(event, score);
            EventBus.publishScoreChanged(new Score(event, currentValue));
            final Collection<IAchievementUnlockedEvent> unlockables = getUnlockables(event, currentValue);
            unlockables.forEach(this::unlock);
        }
    }

    private boolean isDifferentValueAsStored(String event, Long score) {
        final Long currentValue = counterDao.scoreOf(event);
        return currentValue != score;
    }

    public void triggerEvent(final String event, final String... owners) {
        triggerEvent(event, Arrays.asList(owners));
    }

    public void triggerEvent(final String event, final Collection<String> owners) {
        LOG.debug("Achievement event triggered: {} with owners {} ", event, owners);
        final Long currentValue = counterDao.increment(event);
        EventBus.publishScoreChanged(new Score(event, currentValue));
        final Collection<IAchievementUnlockedEvent> unlockables = getUnlockables(event, currentValue, owners);
        unlockables.forEach(this::unlock);
    }

    public void triggerEvent(final String event) {
        LOG.info("Achievement event triggered: {}", event);
        final Long currentValue = counterDao.increment(event);
        EventBus.publishScoreChanged(new Score(event, currentValue));
        final Collection<IAchievementUnlockedEvent> unlockables = getUnlockables(event, currentValue);
        unlockables.forEach(this::unlock);
    }

    private Collection<IAchievementUnlockedEvent> getUnlockables(final String event, final Long currentValue) {
        final Collection<IAchievementUnlockedEvent> unlockables = new ArrayList<>();
        final Collection<IAchievement> achievementBeans = achievementBundle.getAchievementsSubscribedFor(event);
        for (IAchievement achievementBean : achievementBeans) {
            final Optional<IAchievementUnlockedEvent> achievement = unlockable(currentValue, achievementBean);
            if (achievement.isPresent()) {
                unlockables.add(achievement.get());
            }
        }
        return unlockables;
    }

    private Collection<IAchievementUnlockedEvent> getUnlockables(final String event, final Long currentValue, final Collection<String> owners) {
        final Collection<IAchievementUnlockedEvent> unlockables = new ArrayList<>();
        final Collection<IAchievement> achievementBeans = achievementBundle.getAchievementsSubscribedFor(event);
        for (IAchievement achievementBean : achievementBeans) {
            final Optional<IAchievementUnlockedEvent> achievement = unlockable(currentValue, achievementBean);
            if (achievement.isPresent()) {
                final IAchievementUnlockedEvent toUnlock = achievement.get();
                toUnlock.addOwners(owners);
                unlockables.add(toUnlock);
            }
        }
        return unlockables;
    }

    private Collection<IAchievementUnlockedEvent> check(final Collection<IAchievement> achievementBeans) {
        final Collection<IAchievementUnlockedEvent> unlockables = new ArrayList<>();
        for (IAchievement achievementBean : achievementBeans) {
            final Optional<IAchievementUnlockedEvent> achievement = unlockable(counterDao.scoreOf(achievementBean.getId()), achievementBean);
            if (achievement.isPresent()) {
                unlockables.add(achievement.get());
            }
        }
        return unlockables;
    }

    public Optional<IAchievementUnlockedEvent> unlockable(final Long currentValue, final IAchievement achievementBean) {
        if (CounterAchievementBean.class.isAssignableFrom(achievementBean.getClass())) {
            return checkCounterTrigger(currentValue, (CounterAchievementBean) achievementBean);
        } else if (DateAchievementBean.class.isAssignableFrom(achievementBean.getClass())) {
            return checkDateTrigger((DateAchievementBean) achievementBean);
        } else if (TimeAchievementBean.class.isAssignableFrom(achievementBean.getClass())) {
            return checkTimeTrigger((TimeAchievementBean) achievementBean);
        } else if (TimeRangeAchievementBean.class.isAssignableFrom(achievementBean.getClass())) {
            return checkTimeRangeTrigger((TimeRangeAchievementBean) achievementBean);
        } else if (CompositeAchievementBean.class.isAssignableFrom(achievementBean.getClass())) {
            CompositeAchievementBean relationBean = (CompositeAchievementBean) achievementBean;
            if (relationBean.evaluate(this)) {
                final Achievement achievement = createAchievement(relationBean);
                return Optional.of(achievement);
            }
        }
        return Optional.empty();
    }

    private Optional<IAchievementUnlockedEvent> checkDateTrigger(final IAchievementBean<String> dateAchievement) {
        final List<String> dateTriggers = dateAchievement.getTrigger();
        final String now = dateProvider.now();
        for (String dateTrigger : dateTriggers) {
            if (dateTrigger.equals(now) && !isUnlocked(dateAchievement.getId())) {
                final Achievement achievement = createAchievement(dateAchievement, now);
                return Optional.of(achievement);
            }
        }
        return Optional.empty();
    }

    private Optional<IAchievementUnlockedEvent> checkTimeTrigger(final TimeAchievementBean timeAchievement) {
        final List<String> timeTriggers = timeAchievement.getTrigger();
        final String now = dateProvider.now();
        for (String timeTrigger : timeTriggers) {
            if (timeTrigger.equals(now) && !isUnlocked(timeAchievement.getId())) {
                final Achievement achievement = createAchievement(timeAchievement, now);
                return Optional.of(achievement);
            }
        }
        return Optional.empty();
    }

    private Optional<IAchievementUnlockedEvent> checkTimeRangeTrigger(final TimeRangeAchievementBean timeAchievement) {
        final List<TimeRangeAchievementBean.TimeTriggerPair> timeTriggers = timeAchievement.getTrigger();
        for (TimeRangeAchievementBean.TimeTriggerPair timeTrigger : timeTriggers) {
            final Date startTrigger = timeTrigger.getStartTrigger();
            final Date endTrigger = timeTrigger.getEndTrigger();
            if (startTrigger.before(endTrigger)) {
                if (dateProvider.isCurrentTimeAfter(startTrigger) && dateProvider.isCurrentTimeBefore(endTrigger) && !isUnlocked(timeAchievement.getId())) {
                    final Achievement achievement = createAchievement(timeAchievement, dateProvider.now());
                    return Optional.of(achievement);
                }
            } else if (dateProvider.isCurrentTimeBefore(startTrigger) || dateProvider.isCurrentTimeAfter(endTrigger) && !isUnlocked(timeAchievement.getId())) {
                final Achievement achievement = createAchievement(timeAchievement, dateProvider.now());
                return Optional.of(achievement);
            }
        }
        return Optional.empty();
    }

    private Optional<IAchievementUnlockedEvent> checkCounterTrigger(final Long currentValue, final IAchievementBean<NumberTrigger> achievementBean) {
        final List<NumberTrigger> triggers = achievementBean.getTrigger();
        for (int i = 0; i < triggers.size(); i++) {
            final NumberTrigger trigger = triggers.get(i);
            final Integer level = i + 1;
            if (isTriggered(currentValue, trigger) && isLevelValid(achievementBean, level) && !isLevelUnlocked(achievementBean.getId(), level)) {
                final Achievement achievement = createAchievement(achievementBean, level, currentValue);
                return Optional.of(achievement);
            }
        }
        return Optional.empty();
    }

    private Boolean isTriggered(final Long currentValue, final NumberTrigger trigger) {
        final Long triggerValue = trigger.getTrigger();
        switch (trigger.getOperation()) {
            case GREATER_THAN:
                return currentValue >= triggerValue;
            case LESS_THAN:
                return currentValue <= triggerValue;
            case EQUALS:
                return currentValue.equals(triggerValue);
        }
        return false;
    }

    private boolean isLevelValid(final IAchievementBean<NumberTrigger> counterAchievement, final Integer triggerIndex) {
        return counterAchievement.getMaxLevel() >= triggerIndex;
    }

    private Boolean isLevelUnlocked(final String id, final Integer level) {
        return achievementDao.isUnlocked(id, level);
    }

    public void unlock(final AchievementType type, final String achievementId, String triggeredValue) {
        if (!isUnlocked(achievementId)) {
            final IAchievement achievementBean = achievementBundle.get(type, achievementId);
            final Achievement achievement = createAchievement(achievementBean, triggeredValue);
            unlock(achievement);
        }
    }

    public void unlock(final AchievementType type, final String achievementId, final String triggeredValue, final String... owners) {
        if (!isUnlocked(achievementId)) {
            final IAchievement achievementBean = achievementBundle.get(type, achievementId);
            final Achievement achievement = createAchievement(achievementBean, triggeredValue);
            achievement.addOwners(owners);
            unlock(achievement);
        }
    }

    public void unlock(final String achievementId, final String triggerValue, final Collection<String> owners) {
        final Optional<IAchievement> matchingAchievement = getAll().parallelStream().filter(achievement -> achievement.getId().equals(achievementId)).findFirst();
        if (matchingAchievement.isPresent()) {
            final Achievement achievement = createAchievement(matchingAchievement.get(), triggerValue);
            achievement.addOwners(owners);
            unlock(achievement);
        }
    }

    public void unlock(final String achievementId, final String triggerValue) {
        final Optional<IAchievement> matchingAchievement = achievementBundle.get(achievementId);
        if (matchingAchievement.isPresent()) {
            final Achievement achievement = createAchievement(matchingAchievement.get(), triggerValue);
            unlock(achievement);
        }
    }

    private void unlock(final IAchievementUnlockedEvent achievement) {
        if (!isLevelUnlocked(achievement.getId(), achievement.getLevel())) {
            achievementDao.unlock(achievement.getId(), achievement.getLevel(), achievement.getOwners());
            EventBus.publishUnlocked(achievement);
        }
    }

    public Boolean isUnlocked(final String achievementId) {
        return achievementDao.isUnlocked(achievementId);
    }

    public Boolean isUnlocked(final String achievementId, final Integer level) {
        return achievementDao.isUnlocked(achievementId, level);
    }

    public Long getCurrentScore(final String id) {
        return counterDao.scoreOf(id);
    }

    private Achievement createAchievement(final IAchievementBean achievementBean, final Integer level, final Long triggeredValue) {
        final Achievement achievement = createAchievement(achievementBean, String.valueOf(triggeredValue));
        achievement.setLevel(level);
        LOG.info("Achievement created with id: {} level: {}", achievementBean.getId(), level);
        return achievement;
    }

    private Achievement createAchievement(final IAchievement achievementBean) {
        return createAchievement(achievementBean, "");
    }

    private Achievement createAchievement(final IAchievement achievementBean, final String triggeredValue) {
        final String title;
        final String text;
        if (resourceBundle != null) {
            title = resourceBundle.getString(achievementBean.getTitleKey());
            text = resourceBundle.getString(achievementBean.getTextKey());
        } else {
            title = achievementBean.getTitleKey();
            text = achievementBean.getTextKey();
        }
        LOG.info("Achievement created with id: {}", achievementBean.getId());
        return new Achievement(achievementBean.getId(), achievementBean.getCategory(), title, text, triggeredValue);
    }

    public void reset() {
        counterDao.deleteAll();
        achievementDao.deleteAll();
    }

    void setCounterDao(final CounterDao counterDao) {
        this.counterDao = counterDao;
    }

    void setAchievementDao(final AchievementDao achievementDao) {
        this.achievementDao = achievementDao;
    }

    public void setDateProvider(IDateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }
}
