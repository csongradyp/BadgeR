package com.noe.badger.dao;

import com.noe.badger.entity.CounterEntity;
import com.noe.badger.repository.CounterRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

/**
 * Counter DAO for database communication.
 *
 * @author Peter_Csongrady
 */
@Named
public class CounterDao {

    @Inject
    private CounterRepository counterRepository;

    public Long increment(final String name) {
        final Optional<CounterEntity> counter = counterRepository.findByName(name);
        if (counter.isPresent()) {
            final CounterEntity counterEntity = counter.get();
            final Long newValue = counterEntity.incrementValue();
            counterRepository.save(counterEntity);
            return newValue;
        }
        final CounterEntity newCounter = new CounterEntity(name);
        counterRepository.save(newCounter);
        return newCounter.getValue();
    }

    public Long getValueOf(final String name) {
        final Optional<CounterEntity> counter = counterRepository.findByName(name);
        if (counter.isPresent()) {
            return counter.get().getValue();
        }
        throw new IllegalArgumentException("No counter registered with name: " + name);
    }

}
