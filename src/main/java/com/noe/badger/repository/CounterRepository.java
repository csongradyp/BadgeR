package com.noe.badger.repository;

import com.noe.badger.entity.ScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository<ScoreEntity, String> {

    ScoreEntity findByName(String name);

}
