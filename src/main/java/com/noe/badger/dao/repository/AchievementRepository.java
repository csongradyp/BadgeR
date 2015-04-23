package com.noe.badger.dao.repository;

import com.noe.badger.entity.AchievementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface AchievementRepository extends JpaRepository<AchievementEntity, String> {

    Optional<AchievementEntity> findById(final String id);

    Collection<AchievementEntity> findByOwnersIn(final Set<String> owners);

}
