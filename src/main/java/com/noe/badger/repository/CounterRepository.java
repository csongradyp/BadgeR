package com.noe.badger.repository;

import com.noe.badger.entity.ScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CounterRepository extends JpaRepository<ScoreEntity, String> {

    Optional<ScoreEntity> findByName(String name);

}
