package net.csongradyp.badger.persistence.repository;

import net.csongradyp.badger.persistence.entity.AchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Set;

public interface AchievementRepository extends JpaRepository<AchievementEntity, String> {

    Collection<AchievementEntity> findByOwnersIn(final Set<String> owners);

}
