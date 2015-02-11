package com.noe.badger.dao;

import com.noe.badger.entity.Counter;
import com.noe.badger.repository.CounterRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

/**
 * Counter DAO for database communication.
 * @author Peter_Csongrady
 */
@Named
public class CounterDao {

    @Inject
    private CounterRepository counterRepository;

    public Long increment(final String name) {
        final Optional<Counter> counter = counterRepository.findByName();
        if(counter.isPresent()) {
            final Counter counterEntity = counter.get();
            final Long newValue = counterEntity.incrementValue();
            counterRepository.save(counterEntity);
            return newValue;
        }
        throw new IllegalArgumentException("No counter registered with name: " + name);
    }

    public Long getValueOf(final String name) {
         final Optional<Counter> counter = counterRepository.findByName();
        if(counter.isPresent()) {
            return counter.get().getValue();
        }
        throw new IllegalArgumentException("No counter registered with name: " + name);
    }

}
