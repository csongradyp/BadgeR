package com.noe.badger.dao;

import com.noe.badger.entity.ScoreEntity;
import com.noe.badger.repository.CounterRepository;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Counter DAO for database communication.
 * @author Peter_Csongrady
 */
@Named
public class CounterDao {

    @Inject
    private CounterRepository counterRepository;

    public Long increment(final String name) {
        final ScoreEntity scoreEntity = counterRepository.findByName(name);
        if(scoreEntity != null) {
            final Long newValue = scoreEntity.incrementScore();
            counterRepository.save( scoreEntity );
            return newValue;
        }
        final ScoreEntity newCounter = new ScoreEntity(name);
        counterRepository.save(newCounter);
        return newCounter.getScore();
    }

    public Long setScore(final String name, final Long newScore) {
        final ScoreEntity scoreEntity = counterRepository.findByName(name);
        if(scoreEntity != null) {
            scoreEntity.setScore(newScore);
            counterRepository.save( scoreEntity );
            return scoreEntity.getScore();
        }
        final ScoreEntity newCounter = new ScoreEntity(name, newScore);
        counterRepository.save(newCounter);
        return newCounter.getScore();
    }

    public Long getValueOf(final String id) {
         final ScoreEntity scoreEntity = counterRepository.findByName(id);
        if(scoreEntity != null) {
            return scoreEntity.getScore();
        }
        return 0L;
    }

    public void setCounterRepository( CounterRepository counterRepository ) {
        this.counterRepository = counterRepository;
    }
}
