package com.noe.badger.dao;

import com.noe.badger.bundle.domain.IAchievementBean;
import com.noe.badger.entity.AchievementEntity;
import com.noe.badger.repository.AchievementRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.Date;

/**
 * DAO for achievements to database communication.
 *
 * @author Peter_Csongrady
 */
@Named
public class AchievementDao {

    @Inject
    private AchievementRepository achievementRepository;

    public void unlock(final IAchievementBean achievementBean) {
        final AchievementEntity achievement = new AchievementEntity(achievementBean.getId(), achievementBean.getTitleKey());
        achievementRepository.save(achievement);
    }

    public void unlockLevel(final String id, final Integer level) {
        final AchievementEntity achievement = achievementRepository.getOne(id);
        if (achievement != null) {
            achievement.setLevel(level);
        }
        achievementRepository.save(achievement);
    }

    public Long getNumberOfUnlocked() {
        return achievementRepository.count();
    }

    private Collection<AchievementEntity> getAll() {
        return achievementRepository.findAll();
    }

    /**
     * Is the achievement with the given name unlocked.
     *
     * @param id name of Achievement
     * @return {@code true} if the achievement is already unlocked.
     */
    public Boolean isUnlocked(final String id) {
        return achievementRepository.findOne(id) != null;
    }

    /**
     * Returns the date when the achievement was acquired.
     *
     * @param id - name of achievement.
     * @return {@link java.util.Date} of acquire.
     */
    public Date getAcquireDate(final String id) {
        final AchievementEntity achievement = achievementRepository.findOne(id);
        if (achievement != null) {
            return achievement.getAcquireDate();
        }
        return null;
    }

}
