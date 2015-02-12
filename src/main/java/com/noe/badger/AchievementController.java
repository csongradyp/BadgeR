package com.noe.badger;

import com.noe.badger.bundle.AchievementBundle;
import com.noe.badger.bundle.domain.IAchievementBean;
import com.noe.badger.dao.AchievementDao;
import com.noe.badger.dao.CounterDao;
import com.noe.badger.domain.Achievement;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

@Named
public class AchievementController {

    @Inject
    private AchievementBundle achievementBundle;
    @Inject
    private CounterDao counterDao;
    @Inject
    private AchievementDao achievementDao;
    private ResourceBundle resourceBundle;

    private String internationalizationBaseName;

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

    public void incrementCounter(final String id) {
        final Long currentValue = counterDao.increment(id);
        checkTrigger(id, currentValue);
    }

    private void checkTrigger(final String id, final Long currentValue) {
        final IAchievementBean<Long> counterAchievement = achievementBundle.getCounterAchievement(id);
        final Long[] triggers = counterAchievement.getTrigger();
        for (int triggerIndex = 0; triggerIndex < triggers.length; triggerIndex++) {
            final Long triggerValue = triggers[triggerIndex];
            if (currentValue.equals(triggerValue) && counterAchievement.getMaxLevel() >= triggerIndex) {
                achievementDao.unlockLevel(id, triggerIndex);
                final Achievement achievement = createAchievement(counterAchievement, triggerIndex);
                // TODO send event
            }
        }
    }

    public void unlock(final String id) {
        final IAchievementBean achievementBean = achievementBundle.getAchievement(id);
        achievementDao.unlock(achievementBean);
        final Achievement achievement = createAchievement(achievementBean);
        // TODO send event
    }

    private Achievement createAchievement(final IAchievementBean achievementBean, final Integer level) {
        final Achievement achievement = createAchievement(achievementBean);
        achievement.setLevel(level);
        return achievement;
    }

    private Achievement createAchievement(final IAchievementBean achievementBean) {
        final String title = resourceBundle.getString(achievementBean.getTitleKey());
        final String text = resourceBundle.getString(achievementBean.getTextKey());
        return new Achievement(title, text, new Date());
    }
}
