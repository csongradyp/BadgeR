package com.noe.badger.repository;

import com.noe.badger.entity.AchievementEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<AchievementEntity, String> {

    Optional<AchievementEntity> findById(final String id);

}
