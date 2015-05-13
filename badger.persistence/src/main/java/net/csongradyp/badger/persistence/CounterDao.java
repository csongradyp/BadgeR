package net.csongradyp.badger.persistence;

import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.persistence.entity.ScoreEntity;
import net.csongradyp.badger.persistence.repository.CounterRepository;

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
