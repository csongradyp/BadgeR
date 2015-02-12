package com.noe.badger.repository;

import com.noe.badger.entity.Counter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CounterRepository extends JpaRepository<Counter, String> {

    Optional<Counter> findByName(String name);

}
