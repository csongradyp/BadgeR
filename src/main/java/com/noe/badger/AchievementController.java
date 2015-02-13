package com.noe.badger;

import com.noe.badger.bundle.AchievementBundle;
import com.noe.badger.bundle.domain.IAchievementBean;
import com.noe.badger.dao.AchievementDao;
import com.noe.badger.dao.CounterDao;
import com.noe.badger.event.domain.Achievement;
import com.noe.badger.event.AchievementHandler;
import com.noe.badger.event.EventBus;

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
    @Inject
    private EventBus eventBus;
    private ResourceBundle resourceBundle;

    private String internationalizationBaseName;

    public void setInternationalizationBaseName(final String internationalizationBaseName) {
        this.internationalizationBaseName = internationalizationBaseName;
        resourceBundle = ResourceBundle.getBundle( internationalizationBaseName, Locale.getDefault() );
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

    public void incrementAndCheck( final String id ) {
        final Long currentValue = counterDao.increment(id);
        checkTrigger( id, currentValue );
    }

    public void setScoreAndCheck(final String id, final Long score) {
        final Long currentValue = counterDao.setScore( id, score );
        checkTrigger(id, currentValue);
    }

    private void checkTrigger(final String id, final Long currentValue) {
        final IAchievementBean<Long> counterAchievement = achievementBundle.getCounterAchievement(id);
        final Long[] triggers = counterAchievement.getTrigger();
        for (int triggerIndex = 0; triggerIndex < triggers.length; triggerIndex++) {
            final Long triggerValue = triggers[triggerIndex];
            if( isTriggered(currentValue, triggerValue) && isLevelValid(counterAchievement, triggerIndex) && !isLevelUnlocked(id, triggerIndex) ) {
                achievementDao.unlockLevel(id, triggerIndex);
                final Achievement achievement = createAchievement(counterAchievement, triggerIndex, currentValue);
                eventBus.publishUnlocked(achievement);
            }
        }
    }

    private boolean isTriggered(final Long currentValue, final Long triggerValue ) {
        return currentValue > triggerValue;
    }

    private boolean isLevelValid(final IAchievementBean<Long> counterAchievement, final Integer triggerIndex ) {
        return counterAchievement.getMaxLevel() >= triggerIndex;
    }

    private Boolean isLevelUnlocked(final String id, final Integer level) {
        return achievementDao.isUnlocked(id, level);
    }

    public void unlock(final String id) {
        final IAchievementBean achievementBean = achievementBundle.getAchievement(id);
        achievementDao.unlock(achievementBean);
        final Achievement achievement = createAchievement(achievementBean, "");
        eventBus.publishUnlocked(achievement);
    }

    private Achievement createAchievement(final IAchievementBean achievementBean, final Integer level, final Long triggerWith) {
        final Achievement achievement = createAchievement(achievementBean, String.valueOf(triggerWith));
        achievement.setLevel(level);
        return achievement;
    }

    private Achievement createAchievement( final IAchievementBean achievementBean, final String triggerWith) {
        final String title = resourceBundle.getString( achievementBean.getTitleKey() );
        final String text = resourceBundle.getString(achievementBean.getTextKey());
        return new Achievement(title, text, new Date(), triggerWith);
    }

    public void subscribe(final AchievementHandler achievementHandler) {
        eventBus.subscribe(achievementHandler);
    }

    public void unSubscribe(final AchievementHandler achievementHandler) {
        eventBus.unSubscribe( achievementHandler);
    }
}
