package com.noe.badger.repository;

import com.noe.badger.entity.CounterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CounterRepository extends JpaRepository<CounterEntity, String> {

    Optional<CounterEntity> findByName(final String name);

}
