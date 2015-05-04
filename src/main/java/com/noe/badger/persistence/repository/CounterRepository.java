package com.noe.badger.persistence.repository;

import com.noe.badger.persistence.entity.ScoreEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository<ScoreEntity, String> {

    Optional<ScoreEntity> findById(String id);

}
