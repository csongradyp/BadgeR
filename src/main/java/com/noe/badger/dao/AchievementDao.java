package com.noe.badger.dao;

import com.noe.badger.bundle.domain.IAchievementBean;
import com.noe.badger.entity.AchievementEntity;
import com.noe.badger.repository.AchievementRepository;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;

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
        final AchievementEntity achievement = new AchievementEntity(achievementBean.getId());
        achievementRepository.save(achievement);
    }

//    public void unlockLevel(final String id, final Integer level) {
//        final AchievementEntity achievement = achievementRepository.getOne(id);
//        if (achievement != null) {
//            achievement.setLevel(level);
//        }
//        achievementRepository.save(achievement);
//    }

    public void unlockLevel(final String id, final Integer level) {
        final Optional<AchievementEntity> achievement = achievementRepository.findById(id);
        if (achievement.isPresent()) {
            final AchievementEntity achievementEntity = achievement.get();
            achievementEntity.setLevel(level);
            achievementRepository.save(achievementEntity);
        } else {
            achievementRepository.save(new AchievementEntity(id));
        }
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

    public Boolean isUnlocked(final String id, final Integer level) {
        final AchievementEntity achievementEntity = achievementRepository.findOne(id);
        return achievementEntity != null && level <= achievementEntity.getLevel();
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
