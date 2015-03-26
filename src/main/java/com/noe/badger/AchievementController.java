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

    public Map<String, Set<IAchievementBean>> getAllByEvents() {
        return achievementBundle.getAllByEvents();
    }

    public void checkAll() {
        check(getAll());
    }

    public void triggerEvent(final String event, final Long newScore) {
        LOG.debug("Achievement event triggered: {} with score {} ", event, newScore);
        final Long currentValue = counterDao.setScore(event, newScore);
        check(event, currentValue);
    }

    public void triggerEvent(final String event) {
        LOG.info("Achievement event triggered: {}", event);
        final Long currentValue = counterDao.increment(event);
        check(event, currentValue);
    }

    private void check(final String event, final Long currentValue) {
        final Collection<IAchievementBean> achievementBeans = achievementBundle.getAchievementsSubscribedFor(event);
        for (IAchievementBean achievementBean : achievementBeans) {
            check(currentValue, achievementBean);
        }
    }

    private void check(final Collection<IAchievementBean> achievementBeans) {
        for (IAchievementBean achievementBean : achievementBeans) {
            check(counterDao.getValueOf(achievementBean.getId()), achievementBean);
        }
    }

    private void check(final Long currentValue, final IAchievementBean achievementBean) {
        if (CounterAchievementBean.class.isAssignableFrom(achievementBean.getClass())) {
            checkCounterTrigger(currentValue, (CounterAchievementBean) achievementBean);
        } else if (DateAchievementBean.class.isAssignableFrom(achievementBean.getClass())) {
            checkDateTrigger((DateAchievementBean) achievementBean);
        } else if (TimeAchievementBean.class.isAssignableFrom(achievementBean.getClass())) {
            checkTimeTrigger((TimeAchievementBean) achievementBean);
        } else if (TimeRangeAchievementBean.class.isAssignableFrom(achievementBean.getClass())) {
            checkTimeRangeTrigger((TimeRangeAchievementBean) achievementBean);
        } else {
            // TODO add missing types
        }
    }

    private void checkDateTrigger(final IAchievementBean<String> dateAchievement) {
        final List<String> dateTriggers = dateAchievement.getTrigger();
        final String now = DateFormatUtil.formatDate(new Date());
        dateTriggers.stream().filter(date -> date.equals(now)).forEach(time -> unlock(dateAchievement.getId(), now));
    }

    private void checkTimeTrigger(final TimeAchievementBean timeAchievement) {
        final List<String> timeTriggers = timeAchievement.getTrigger();
        final String now = DateFormatUtil.formatTime(new Date());
        timeTriggers.stream().filter(time -> time.equals(now)).forEach(time -> unlock(timeAchievement.getId(), now));
    }

    private void checkTimeRangeTrigger(final TimeRangeAchievementBean timeAchievement) {
        final List<TimeRangeAchievementBean.TimeTriggerPair> timeTriggers = timeAchievement.getTrigger();
        for (TimeRangeAchievementBean.TimeTriggerPair timeTrigger : timeTriggers) {
            final Date startTrigger = timeTrigger.getStartTrigger();
            final Date endTrigger = timeTrigger.getEndTrigger();
            final Date now = new Date();
            if (startTrigger.before(endTrigger)) {
                if (now.after(startTrigger) && now.before(endTrigger)) {
                    unlock(timeAchievement.getId(), DateFormatUtil.formatDate(now));
                }
            } else {
                if (now.before(startTrigger) || now.after(endTrigger)) {
                    unlock(timeAchievement.getId(), DateFormatUtil.formatDate(now));
                }
            }
        }
    }

    private void checkCounterTrigger(Long currentValue, IAchievementBean<NumberTrigger> achievementBean) {
        final List<NumberTrigger> triggers = achievementBean.getTrigger();
        for (int triggerIndex = 0; triggerIndex < triggers.size(); triggerIndex++) {
            final NumberTrigger trigger = triggers.get(triggerIndex);
            if (isTriggered(currentValue, trigger) && isLevelValid(achievementBean, triggerIndex) && !isLevelUnlocked(achievementBean.getId(), triggerIndex)) {
                achievementDao.unlockLevel(achievementBean.getId(), triggerIndex);
                final Achievement achievement = createAchievement(achievementBean, triggerIndex, currentValue);
                EventBus.publishUnlocked(achievement);
            }
        }
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

    public void unlock(final String id, String triggeredValue) {
        if (!achievementDao.isUnlocked(id)) {
            final IAchievementBean achievementBean = achievementBundle.get(id);
            achievementDao.unlock(achievementBean);
            final Achievement achievement = createAchievement(achievementBean, triggeredValue);
            EventBus.publishUnlocked(achievement);
        }
    }

    public Long getCurrentScore(final String id) {
        return counterDao.getValueOf(id);
    }

    private Achievement createAchievement(final IAchievementBean achievementBean, final Integer level, final Long triggerWith) {
        final Achievement achievement = createAchievement(achievementBean, String.valueOf(triggerWith));
        achievement.setLevel(level);
        return achievement;
    }

    private Achievement createAchievement(final IAchievementBean achievementBean, final String triggeredValue) {
        final String title = resourceBundle.getString(achievementBean.getTitleKey());
        final String text = resourceBundle.getString(achievementBean.getTextKey());
        return new Achievement(achievementBean.getId(), title, text, new Date(), triggeredValue);
    }

}
