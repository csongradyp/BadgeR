package com.noe.badger;

import com.noe.badger.bundle.AchievementBundle;
import com.noe.badger.bundle.domain.CounterAchievementBean;
import com.noe.badger.bundle.domain.DateAchievementBean;
import com.noe.badger.bundle.domain.IAchievementBean;
import com.noe.badger.bundle.domain.TimeAchievementBean;
import com.noe.badger.dao.AchievementDao;
import com.noe.badger.dao.CounterDao;
import com.noe.badger.event.EventBus;
import com.noe.badger.event.domain.Achievement;
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
        resourceBundle = ResourceBundle.getBundle(internationalizationBaseName, Locale.getDefault());
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

    public void check(final String... ids) {
        for (String id : ids) {
            final IAchievementBean achievement = achievementBundle.getAchievement(id);
            if (DateAchievementBean.class.isAssignableFrom(achievement.getClass())) {
                checkDateTrigger(id);
            } else if (CounterAchievementBean.class.isAssignableFrom(achievement.getClass())) {
                checkCounterTrigger(id);
            }
        }
    }

    private void checkDateTrigger(final String id) {
        final IAchievementBean<String> dateAchievement = achievementBundle.getDateAchievement(id);
        checkDateTrigger(dateAchievement);
    }

    public void triggerEvent(final String event) {
        final Long currentValue = counterDao.increment(event);
        trigger(event, currentValue);
    }

    private void trigger(String event, Long currentValue) {
        final Collection<IAchievementBean> achievementBeans = achievementBundle.getAchievementsSubscribedFor(event);
        for (IAchievementBean achievementBean : achievementBeans) {
            if (CounterAchievementBean.class.isAssignableFrom(achievementBean.getClass())) {
                checkCounterTrigger(currentValue, (CounterAchievementBean) achievementBean);
            } else if (DateAchievementBean.class.isAssignableFrom(achievementBean.getClass())) {
                checkDateTrigger((DateAchievementBean) achievementBean);
            } else if (TimeAchievementBean.class.isAssignableFrom(achievementBean.getClass())) {
                checkTimeTrigger((TimeAchievementBean) achievementBean);
            } else {
                // TODO add missing types
            }
        }
    }

    private void checkDateTrigger(IAchievementBean<String> dateAchievement) {
        final List<String> dateTriggers = dateAchievement.getTrigger();
        dateTriggers.stream().filter(date -> date.equals(DateFormatUtil.formatDate(new Date()))).forEach(time -> unlock(dateAchievement.getId()));
    }

    private void checkTimeTrigger(TimeAchievementBean timeAchievement) {
        final List<String> timeTriggers = timeAchievement.getTrigger();
        timeTriggers.stream().filter(time -> time.equals(DateFormatUtil.formatTime(new Date()))).forEach(time -> unlock(timeAchievement.getId()));
    }

    public void setScoreAndCheck(final String event, final Long score) {
        final Long currentValue = counterDao.setScore(event, score);
        trigger(event, currentValue);
    }

    private void checkCounterTrigger(final String id) {
        checkCounterTrigger(id, getCurrentScore(id));
    }

    private void checkCounterTrigger(final String counter, final Long currentValue) {
        final List<IAchievementBean<Long>> counterAchievements = achievementBundle.getCounterAchievementForCounter(counter);
        for (IAchievementBean<Long> counterAchievement : counterAchievements) {
            checkCounterTrigger(currentValue, counterAchievement);
        }
    }

    private void checkCounterTrigger(Long currentValue, IAchievementBean<Long> achievementBean) {
        final List<Long> triggers = achievementBean.getTrigger();
        for (int triggerIndex = 0; triggerIndex < triggers.size(); triggerIndex++) {
            final Long triggerValue = triggers.get(triggerIndex);
            if (isTriggered(currentValue, triggerValue) && isLevelValid(achievementBean, triggerIndex) && !isLevelUnlocked(achievementBean.getId(), triggerIndex)) {
                achievementDao.unlockLevel(achievementBean.getId(), triggerIndex);
                final Achievement achievement = createAchievement(achievementBean, triggerIndex, currentValue);
                EventBus.publishUnlocked(achievement);
            }
        }
    }

    private boolean isTriggered(final Long currentValue, final Long triggerValue) {
        return currentValue >= triggerValue;
    }

    private boolean isLevelValid(final IAchievementBean<Long> counterAchievement, final Integer triggerIndex) {
        return counterAchievement.getMaxLevel() >= triggerIndex;
    }

    private Boolean isLevelUnlocked(final String id, final Integer level) {
        return achievementDao.isUnlocked(id, level);
    }

    public void unlock(final String id) {
        final IAchievementBean achievementBean = achievementBundle.getAchievement(id);
        achievementDao.unlock(achievementBean);
        final Achievement achievement = createAchievement(achievementBean, "");
        EventBus.publishUnlocked(achievement);
    }

    public Long getCurrentScore(final String id) {
        return counterDao.getValueOf(id);
    }

    private Achievement createAchievement(final IAchievementBean achievementBean, final Integer level, final Long triggerWith) {
        final Achievement achievement = createAchievement(achievementBean, String.valueOf(triggerWith));
        achievement.setLevel(level);
        return achievement;
    }

    private Achievement createAchievement(final IAchievementBean achievementBean, final String triggerWith) {
        final String title = resourceBundle.getString(achievementBean.getTitleKey());
        final String text = resourceBundle.getString(achievementBean.getTextKey());
        return new Achievement(achievementBean.getId(), title, text, new Date(), triggerWith);
    }

}
