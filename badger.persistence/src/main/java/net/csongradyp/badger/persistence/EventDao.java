package net.csongradyp.badger.persistence;

import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.persistence.entity.EventEntity;
import net.csongradyp.badger.persistence.exception.MissingEventCounterException;
import net.csongradyp.badger.persistence.repository.EventRepository;

/**
 * Counter DAO for database communication.
 *
 * @author Peter_Csongrady
 */
@Named
public class EventDao {

    @Inject
    private EventRepository eventRepository;

    public Long increment(final String event) {
        EventEntity score = getEventEntity(event);
        score.incrementScore();
        eventRepository.save(score);
        return score.getScore();
    }

    public Long setScore(final String event, final Long newScore) {
        EventEntity score = getEventEntity(event);
        score.setScore(newScore);
        eventRepository.save(score);
        return score.getScore();
    }

    private EventEntity getEventEntity(String event) {
        EventEntity score = eventRepository.findOne(event);
        if (score == null) {
            score = new EventEntity();
            score.setId(event);
            score.setScore(0L);
        }
        return score;
    }

    public Long scoreOf(final String event) {
        final EventEntity score = eventRepository.findOne(event);
        if (score != null) {
            return score.getScore();
        }
        throw new MissingEventCounterException(event);
    }

    public void deleteAll() {
        eventRepository.deleteAll();
    }

    void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
}
