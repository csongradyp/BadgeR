package com.noe.badger;

import com.noe.badger.bundle.AchievementBundle;
import com.noe.badger.bundle.domain.IAchievementBean;
import com.noe.badger.dao.AchievementDao;
import com.noe.badger.dao.CounterDao;
import com.noe.badger.event.EventBus;
import com.noe.badger.event.domain.Achievement;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
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

    public void incrementAndCheck(final String id) {
        final Long currentValue = counterDao.increment(id);
        checkCounterTrigger(id, currentValue);
    }

    public void setScoreAndCheck(final String id, final Long score) {
        final Long currentValue = counterDao.setScore(id, score);
        checkCounterTrigger(id, currentValue);
    }

    private void checkCounterTrigger(final String id, final Long currentValue) {
        final IAchievementBean<Long> counterAchievement = achievementBundle.getCounterAchievement(id);
        checkTrigger(id, currentValue, counterAchievement);
    }

    private void checkTrigger(String id, Long currentValue, IAchievementBean<Long> achievementBean) {
        final Long[] triggers = achievementBean.getTrigger();
        for (int triggerIndex = 0; triggerIndex < triggers.length; triggerIndex++) {
            final Long triggerValue = triggers[triggerIndex];
            if (isTriggered(currentValue, triggerValue) && isLevelValid(achievementBean, triggerIndex) && !isLevelUnlocked(id, triggerIndex)) {
                achievementDao.unlockLevel(id, triggerIndex);
                final Achievement achievement = createAchievement(achievementBean, triggerIndex, currentValue);
                EventBus.publishUnlocked(achievement);
            }
        }
    }

    private boolean isTriggered(final Long currentValue, final Long triggerValue) {
        return currentValue > triggerValue;
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
        return new Achievement(title, text, new Date(), triggerWith);
    }

}
