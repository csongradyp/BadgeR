package com.noe.badger.dao;

import com.noe.badger.entity.Achievement;
import com.noe.badger.repository.AchievementRepository;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Date;

/**
 * DAO for achievements to database communication.
 *
 * @author Peter_Csongrady
 */
public class AchievementDao {

    @Inject
    private AchievementRepository achievementRepository;

    /**
     * Unlocks the achievement with given id.
     *
     * @param id - id of achievement to unlock.
     */
    public void unlock(final String id) {
        // TODO get achie from ini and save to database
    }

    public Long getNumberOfUnlocked() {
        return achievementRepository.count();
    }

    private Collection<Achievement> getAll() {
        return achievementRepository.findAll();
    }

    /**
     * Is the achievement with the given name unlocked.
     *
     * @param id - name of Achievement
     * @return <code>true</code> if the achievement is already unlocked.
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
        final Achievement achievement = achievementRepository.findOne(id);
        if (achievement != null) {
            return achievement.getAcquireDate();
        }
        return null;
    }

}
