package com.noe.badger.persistence;

import com.noe.badger.persistence.entity.ScoreEntity;
import com.noe.badger.persistence.repository.CounterRepository;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Counter DAO for database communication.
 *
 * @author Peter_Csongrady
 */
@Named
public class CounterDao {

    @Inject
    private CounterRepository counterRepository;

    public Long increment(final String event) {
        final Optional<ScoreEntity> score = counterRepository.findById(event);
        if (score.isPresent()) {
            final ScoreEntity scoreEntity = score.get();
            final Long newValue = scoreEntity.incrementScore();
            counterRepository.save(scoreEntity);
            return newValue;
        }
        final ScoreEntity newCounter = new ScoreEntity(event);
        counterRepository.save(newCounter);
        return newCounter.getScore();
    }

    public Long setScore(final String event, final Long newScore) {
        ScoreEntity scoreEntity;
        final Optional<ScoreEntity> score = counterRepository.findById(event);
        if (score.isPresent()) {
            scoreEntity = score.get();
            scoreEntity.setScore(newScore);
        } else {
            scoreEntity = new ScoreEntity(event, newScore);
        }
        counterRepository.save(scoreEntity);
        return scoreEntity.getScore();
    }

    public Long scoreOf(final String event) {
        final Optional<ScoreEntity> score = counterRepository.findById(event);
        if (score.isPresent()) {
            return score.get().getScore();
        }
        return Long.MIN_VALUE;
    }

    public void deleteAll() {
        counterRepository.deleteAll();
    }

}
