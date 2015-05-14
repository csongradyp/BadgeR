package net.csongradyp.badger.persistence;

import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.persistence.entity.EventEntity;
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
        final Optional<EventEntity> score = eventRepository.findById(event);
        if (score.isPresent()) {
            final EventEntity eventEntity = score.get();
            final Long newValue = eventEntity.incrementScore();
            eventRepository.save(eventEntity);
            return newValue;
        }
        final EventEntity newCounter = new EventEntity(event);
        eventRepository.save(newCounter);
        return newCounter.getScore();
    }

    public Long setScore(final String event, final Long newScore) {
        EventEntity eventEntity;
        final Optional<EventEntity> score = eventRepository.findById(event);
        if (score.isPresent()) {
            eventEntity = score.get();
            eventEntity.setScore(newScore);
        } else {
            eventEntity = new EventEntity(event, newScore);
        }
        eventRepository.save(eventEntity);
        return eventEntity.getScore();
    }

    public Long scoreOf(final String event) {
        final Optional<EventEntity> score = eventRepository.findById(event);
        if (score.isPresent()) {
            return score.get().getScore();
        }
        return Long.MIN_VALUE;
    }

    public void deleteAll() {
        eventRepository.deleteAll();
    }

}
