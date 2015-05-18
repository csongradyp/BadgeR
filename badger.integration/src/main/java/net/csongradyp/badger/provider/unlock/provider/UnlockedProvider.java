package net.csongradyp.badger.provider.unlock.provider;

import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.persistence.AchievementDao;
import net.csongradyp.badger.provider.unlock.IUnlockedProvider;

import javax.inject.Inject;

abstract class UnlockedProvider<TYPE extends IAchievement> implements IUnlockedProvider<TYPE> {

    @Inject
    protected AchievementDao achievementDao;

    public Boolean isUnlocked(final String achievementId) {
        return achievementDao.isUnlocked(achievementId);
    }

    void setAchievementDao(final AchievementDao achievementDao) {
        this.achievementDao = achievementDao;
    }
}
