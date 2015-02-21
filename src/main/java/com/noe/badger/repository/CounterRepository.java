package com.noe.badger.repository;

import com.noe.badger.entity.ScoreEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository<ScoreEntity, String> {

    Optional<ScoreEntity> findById(String id);

}
