package net.csongradyp.badger.persistence.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import net.csongradyp.badger.persistence.entity.AchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<AchievementEntity, String> {

    Optional<AchievementEntity> findById(final String id);

    Collection<AchievementEntity> findByOwnersIn(final Set<String> owners);

}
