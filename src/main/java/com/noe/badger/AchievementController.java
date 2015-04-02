package com.noe.badger;

import com.noe.badger.bundle.AchievementBundle;
import com.noe.badger.bundle.domain.*;
import com.noe.badger.bundle.trigger.NumberTrigger;
import com.noe.badger.dao.AchievementDao;
import com.noe.badger.dao.CounterDao;
import com.noe.badger.event.EventBus;
import com.noe.badger.event.message.Achievement;
import com.noe.badger.util.DateFormatUtil;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class AchievementController {

    private static final Logger LOG = LoggerFactory.getLogger(AchievementController.class);

    @Inject
    private AchievementBundle achievementBundle;
    @Inject
    private CounterDao counterDao;
    @Inject
    private AchievementDao achievementDao;
    private ResourceBundle resourceBundle;

    private String internationalizationBaseName;

    public AchievementController() {
        EventBus.setController(this);
    }

    public void setInternationalizationBaseName(final String internationalizationBaseName) {
        this.internationalizationBaseName = internationalizationBaseName;
        resourceBundle = ResourceBundle.getBundle(internationalizationBaseName, Locale.ENGLISH);
    }

    public void setLocale(final Locale locale) {
        resourceBundle = ResourceBundle.getBundle(internationalizationBaseName, locale);
    }

    public void setSource(final InputStream source) {
        achievementBundle.setSource(source);
    }

    public void setSource(final File achievementIni) {
        achievementBundle.setSource(achievementIni);
    }

    public Collection<IAchievementBean> getAll() {
        return achievementBundle.getAll();
    }

    public IAchievementBean get(final AchievementType type, final String id) {
        return achievementBundle.get(type, id);
    }


    public Map<String, Set<IAchievementBean>> getAllByEvents() {
        return achievementBundle.getAllByEvents();
    }

    public void checkAll() {
        final Collection<Achievement> unlockableAchievements = check(getAll());
        unlockableAchievements.forEach(this::unlock);
    }

    public void triggerEvent(final String event, final Long newScore) {
        LOG.debug("Achievement event triggered: {} with score {} ", event, newScore);
        final Long currentValue = counterDao.setScore(event, newScore);
        final Collection<Achievement> unlockables = getUnlockables(event, currentValue);
        unlockables.forEach(this::unlock);
    }

    public void triggerEvent(final String event) {
        LOG.info("Achievement event triggered: {}", event);
        final Long currentValue = counterDao.increment(event);
        final Collection<Achievement> unlockables = getUnlockables(event, currentValue);
        unlockables.forEach(this::unlock);
    }

    private Collection<Achievement> getUnlockables(final String event, final Long currentValue) {
        final Collection<Achievement> unlockables = new ArrayList<>();
        final Collection<IAchievementBean> achievementBeans = achievementBundle.getAchievementsSubscribedFor(event);
        for (IAchievementBean achievementBean : achievementBeans) {
            final Optional<Achievement> achievement = unlockable(currentValue, achievementBean);
            if (achievement.isPresent()) {
                unlockables.add(achievement.get());
            }
        }
        return unlockables;
    }

    private Collection<Achievement> check(final Collection<IAchievementBean> achievementBeans) {
        final Collection<Achievement> unlockables = new ArrayList<>();
        for (IAchievementBean achievementBean : achievementBeans) {
            final Optional<Achievement> achievement = unlockable(counterDao.getValueOf(achievementBean.getId()), achievementBean);
            if (achievement.isPresent()) {
                unlockables.add(achievement.get());
            }
        }
        return unlockables;
    }

    public Optional<Achievement> unlockable(final Long currentValue, final IAchievementBean achievementBean) {
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
                final Achievement achievement = createAchievement(relationBean, "");
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
                final Achievement achievement = createAchievement(achievementBean, triggerIndex, currentValue);
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
        if (!achievementDao.isUnlocked(achievementId)) {
            final IAchievementBean achievementBean = achievementBundle.get(type, achievementId);
            achievementDao.unlock(achievementId);
            final Achievement achievement = createAchievement(achievementBean, triggeredValue);
            EventBus.publishUnlocked(achievement);
        }
    }

    public void unlock(final String achievementId, final String triggerValue) {
        final Optional<IAchievementBean> matchingAchievement = achievementBundle.getAll().parallelStream().filter(achievement -> achievement.getId().equals(achievementId)).findFirst();
        if (matchingAchievement.isPresent()) {
            final Achievement achievement = createAchievement(matchingAchievement.get(), triggerValue);
            unlock(achievement);
        }
    }

    public void unlock(final Achievement achievement) {
        if (!isUnlocked(achievement.getId())) {
            achievementDao.unlock(achievement.getId());
            EventBus.publishUnlocked(achievement);
        }
    }

    public Boolean isUnlocked(final String achievementId) {
        return achievementDao.isUnlocked(achievementId);
    }

    public Long getCurrentScore(final String id) {
        return counterDao.getValueOf(id);
    }

    private Achievement createAchievement(final IAchievementBean achievementBean, final Integer level, final Long triggeredValue) {
        final Achievement achievement = createAchievement(achievementBean, String.valueOf(triggeredValue));
        achievement.setLevel(level);
        return achievement;
    }

    private Achievement createAchievement(final IAchievementBean achievementBean, final String triggeredValue) {
        final String title = resourceBundle.getString(achievementBean.getTitleKey());
        final String text = resourceBundle.getString(achievementBean.getTextKey());
        return new Achievement(achievementBean.getId(), achievementBean.getCategory(), title, text, triggeredValue);
    }
}
