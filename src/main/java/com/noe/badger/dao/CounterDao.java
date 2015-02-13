package com.noe.badger.dao;

import com.noe.badger.entity.ScoreEntity;
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
        final Optional<ScoreEntity> counter = counterRepository.findByName(name);
        if(counter.isPresent()) {
            final ScoreEntity scoreEntity = counter.get();
            final Long newValue = scoreEntity.incrementScore();
            counterRepository.save( scoreEntity );
            return newValue;
        }
        final ScoreEntity newCounter = new ScoreEntity(name);
        counterRepository.save(newCounter);
        return newCounter.getScore();
    }

    public Long setScore(final String name, final Long newScore) {
        final Optional<ScoreEntity> counter = counterRepository.findByName(name);
        if(counter.isPresent()) {
            final ScoreEntity scoreEntity = counter.get();
            scoreEntity.setScore(newScore);
            counterRepository.save( scoreEntity );
            return scoreEntity.getScore();
        }
        final ScoreEntity newCounter = new ScoreEntity(name, newScore);
        counterRepository.save(newCounter);
        return newCounter.getScore();
    }

    public Long getValueOf(final String id) {
         final Optional<ScoreEntity> counter = counterRepository.findByName(id);
        if(counter.isPresent()) {
            return counter.get().getScore();
        }
        throw new IllegalArgumentException("No counter registered with id: " + id );
    }

}
