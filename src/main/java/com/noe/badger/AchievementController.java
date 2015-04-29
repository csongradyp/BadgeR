package com.noe.badger;

import com.noe.badger.bundle.AchievementBundle;
import com.noe.badger.bundle.domain.IAchievement;
import com.noe.badger.bundle.domain.IAchievementBean;
import com.noe.badger.bundle.domain.achievement.*;
import com.noe.badger.bundle.trigger.NumberTrigger;
import com.noe.badger.dao.AchievementDao;
import com.noe.badger.dao.CounterDao;
import com.noe.badger.entity.AchievementEntity;
import com.noe.badger.event.EventBus;
import com.noe.badger.event.message.Achievement;
import com.noe.badger.event.message.Score;
import com.noe.badger.util.DateFormatUtil;
import java.util.*;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class AchievementController {

    private static final Logger LOG = LoggerFactory.getLogger(AchievementController.class);

    @Inject
    private CounterDao counterDao;
    @Inject
    private AchievementDao achievementDao;
    private ResourceBundle resourceBundle;

    private AchievementBundle achievementBundle;
    private String internationalizationBaseName;

    public AchievementController() {
        achievementBundle = new AchievementBundle();
        EventBus.setController(this);
    }

    public void setDefinition(final AchievementBundle definition) {
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
        final Collection<Achievement> unlockableAchievements = check(getAll());
        unlockableAchievements.forEach(this::unlock);
    }

    public void triggerEvent(final String event, final Long score) {
        LOG.debug("Achievement event triggered: {} with score {} ", event, score);
        final Long currentValue = counterDao.setScore(event, score);
        EventBus.publishScoreChanged(new Score(event, currentValue));
        final Collection<Achievement> unlockables = getUnlockables(event, currentValue);
        unlockables.forEach(this::unlock);
    }

    public void triggerEvent(final String event, final String... owners) {
        triggerEvent(event, Arrays.asList(owners));
    }

    public void triggerEvent(final String event, final Collection<String> owners) {
        LOG.debug("Achievement event triggered: {} with owners {} ", event, owners);
        final Long currentValue = counterDao.increment(event);
        EventBus.publishScoreChanged(new Score(event, currentValue));
        final Collection<Achievement> unlockables = getUnlockables(event, currentValue, owners);
        unlockables.forEach(this::unlock);
    }

    public void triggerEvent(final String event) {
        LOG.info("Achievement event triggered: {}", event);
        final Long currentValue = counterDao.increment(event);
        EventBus.publishScoreChanged(new Score(event, currentValue));
        final Collection<Achievement> unlockables = getUnlockables(event, currentValue);
        unlockables.forEach(this::unlock);
    }

    private Collection<Achievement> getUnlockables(final String event, final Long currentValue) {
        final Collection<Achievement> unlockables = new ArrayList<>();
        final Collection<IAchievement> achievementBeans = achievementBundle.getAchievementsSubscribedFor(event);
        for (IAchievement achievementBean : achievementBeans) {
            final Optional<Achievement> achievement = unlockable(currentValue, achievementBean);
            if (achievement.isPresent()) {
                unlockables.add(achievement.get());
            }
        }
        return unlockables;
    }

    private Collection<Achievement> getUnlockables(final String event, final Long currentValue, final Collection<String> owners) {
        final Collection<Achievement> unlockables = new ArrayList<>();
        final Collection<IAchievement> achievementBeans = achievementBundle.getAchievementsSubscribedFor(event);
        for (IAchievement achievementBean : achievementBeans) {
            final Optional<Achievement> achievement = unlockable(currentValue, achievementBean);
            if (achievement.isPresent()) {
                final Achievement toUnlock = achievement.get();
                toUnlock.addOwners(owners);
                unlockables.add(toUnlock);
            }
        }
        return unlockables;
    }

    private Collection<Achievement> check(final Collection<IAchievement> achievementBeans) {
        final Collection<Achievement> unlockables = new ArrayList<>();
        for (IAchievement achievementBean : achievementBeans) {
            final Optional<Achievement> achievement = unlockable(counterDao.scoreOf(achievementBean.getId()), achievementBean);
            if (achievement.isPresent()) {
                unlockables.add(achievement.get());
            }
        }
        return unlockables;
    }

    public Optional<Achievement> unlockable(final Long currentValue, final IAchievement achievementBean) {
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

    private Optional<Achievement> checkDateTrigger(final IAchievementBean<String> dateAchievement) {
        final List<String> dateTriggers = dateAchievement.getTrigger();
        final String now = DateFormatUtil.formatDate(new Date());
        for (String dateTrigger : dateTriggers) {
            if (dateTrigger.equals(now) && !isUnlocked(dateAchievement.getId())) {
                final Achievement achievement = createAchievement(dateAchievement, now);
                return Optional.of(achievement);
            }
        }
        return Optional.empty();
    }

    private Optional<Achievement> checkTimeTrigger(final TimeAchievementBean timeAchievement) {
        final List<String> timeTriggers = timeAchievement.getTrigger();
        final String now = DateFormatUtil.formatTime(new Date());
        for (String timeTrigger : timeTriggers) {
            if (timeTrigger.equals(now) && !isUnlocked(timeAchievement.getId())) {
                final Achievement achievement = createAchievement(timeAchievement, now);
                return Optional.of(achievement);
            }
        }
        return Optional.empty();
    }

    private Optional<Achievement> checkTimeRangeTrigger(final TimeRangeAchievementBean timeAchievement) {
        final List<TimeRangeAchievementBean.TimeTriggerPair> timeTriggers = timeAchievement.getTrigger();
        for (TimeRangeAchievementBean.TimeTriggerPair timeTrigger : timeTriggers) {
            final Date startTrigger = timeTrigger.getStartTrigger();
            final Date endTrigger = timeTrigger.getEndTrigger();
            final Date now = new Date();
            if (startTrigger.before(endTrigger)) {
                if (now.after(startTrigger) && now.before(endTrigger) && !isUnlocked(timeAchievement.getId())) {
                    final Achievement achievement = createAchievement(timeAchievement, DateFormatUtil.formatTime(now));
                    return Optional.of(achievement);
                }
            } else if (now.before(startTrigger) || now.after(endTrigger) && !isUnlocked(timeAchievement.getId())) {
                final Achievement achievement = createAchievement(timeAchievement, DateFormatUtil.formatTime(now));
                return Optional.of(achievement);
            }
        }
        return Optional.empty();
    }

    private Optional<Achievement> checkCounterTrigger(final Long currentValue, final IAchievementBean<NumberTrigger> achievementBean) {
        final List<NumberTrigger> triggers = achievementBean.getTrigger();
        for (int triggerIndex = 0; triggerIndex < triggers.size(); triggerIndex++) {
            final NumberTrigger trigger = triggers.get(triggerIndex);
            if (isTriggered(currentValue, trigger) && isLevelValid(achievementBean, triggerIndex) && !isLevelUnlocked(achievementBean.getId(), triggerIndex)) {
                final Achievement achievement = createAchievement(achievementBean, triggerIndex + 1, currentValue);
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

    private void unlock(final Achievement achievement) {
        if (!isUnlocked(achievement.getId())) {
            achievementDao.unlock(achievement.getId(), achievement.getLevel(), achievement.getOwners());
            EventBus.publishUnlocked(achievement);
        }
    }

    public Boolean isUnlocked(final String achievementId) {
        return achievementDao.isUnlocked(achievementId);
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

    public void setCounterDao(final CounterDao counterDao) {
        this.counterDao = counterDao;
    }

    public void setAchievementDao(final AchievementDao achievementDao) {
        this.achievementDao = achievementDao;
    }
}
