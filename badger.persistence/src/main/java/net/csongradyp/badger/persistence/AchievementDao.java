package net.csongradyp.badger.persistence;

import net.csongradyp.badger.persistence.entity.AchievementEntity;
import net.csongradyp.badger.persistence.repository.AchievementRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * DAO for achievements to database communication.
 *
 * @author Peter_Csongrady
 */
@Named
public class AchievementDao {

    @Inject
    private AchievementRepository achievementRepository;

     public void unlock(final String achievementId) {
        final AchievementEntity achievement = new AchievementEntity(achievementId);
         achievementRepository.save(achievement);
     }

    public void unlock(final String achievementId, final Integer level) {
        final AchievementEntity achievement = new AchievementEntity(achievementId);
        achievement.setLevel(level);
        achievementRepository.save(achievement);
    }

     public void unlock(final String achievementId, final String... owners) {
        final AchievementEntity achievement = new AchievementEntity(achievementId);
         achievement.addOwners(Arrays.asList(owners));
        achievementRepository.save(achievement);
    }

    public void unlock(final String achievementId, final Integer level, final Set<String> owners) {
        final Optional<AchievementEntity> achievement = achievementRepository.findById(achievementId);
        if (achievement.isPresent()) {
            final AchievementEntity achievementEntity = achievement.get();
            achievementEntity.setLevel(level);
            achievementEntity.addOwners(owners);
            achievementRepository.save(achievementEntity);
        } else {
            final AchievementEntity achievementEntity = new AchievementEntity(achievementId);
            achievementEntity.setOwners(owners);
            achievementRepository.save(achievementEntity);
        }
    }

    public void deleteAll() {
        achievementRepository.deleteAll();
    }

    public Collection<AchievementEntity> getByOwner(final String owner) {
        final Set<String> owners = new HashSet<>();
        owners.add(owner);
        return achievementRepository.findByOwnersIn(owners);
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

    void setAchievementRepository(final AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }
}
