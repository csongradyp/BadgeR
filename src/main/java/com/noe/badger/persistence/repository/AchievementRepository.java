package com.noe.badger.persistence.repository;

import com.noe.badger.persistence.entity.AchievementEntity;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<AchievementEntity, String> {

    Optional<AchievementEntity> findById(final String id);

    Collection<AchievementEntity> findByOwnersIn(final Set<String> owners);

}
