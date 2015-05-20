package net.csongradyp.badger.persistence;

import net.csongradyp.badger.persistence.entity.EventEntity;
import net.csongradyp.badger.persistence.exception.MissingEventCounterException;
import net.csongradyp.badger.persistence.repository.EventRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EventDaoTest {

    private static final String EVENT_ID = "test";
    @Mock
    private EventRepository mockEventRepository;
    @Captor
    ArgumentCaptor<EventEntity> argumentCaptor;

    private EventDao underTest;

    @Before
    public void setUp() {
        underTest = new EventDao();
        underTest.setEventRepository(mockEventRepository);
    }

    @Test
    public void testDeleteAllDeletesAllEntriesFromRepository() {
        underTest.deleteAll();
        verify(mockEventRepository).deleteAll();
    }

    @Test
    public void testIncrementSavesAndReturnsIncrementedValueOfTheEventWithTheGivenId() {
        final EventEntity eventEntity = new EventEntity();
        final long currentScore = 72L;
        eventEntity.setScore(currentScore);
        given(mockEventRepository.findOne(EVENT_ID)).willReturn(eventEntity);

        final Long resultScore = underTest.increment(EVENT_ID);

        verify(mockEventRepository).findOne(EVENT_ID);
        verify(mockEventRepository).save(eventEntity);
        assertThat(resultScore, is(equalTo(currentScore + 1)));
    }

    @Test
    public void testIncrementReturnsOneWhenTheEventIsTriggeredAtFirstTime() {
        given(mockEventRepository.findOne(EVENT_ID)).willReturn(null);
        given(mockEventRepository.save(argumentCaptor.capture())).willReturn(null);

        final Long resultScore = underTest.increment(EVENT_ID);

        verify(mockEventRepository).findOne(EVENT_ID);
        final EventEntity savedEntity = argumentCaptor.getValue();
        verify(mockEventRepository).save(savedEntity);
        assertThat(resultScore, is(equalTo(1L)));
        assertThat(savedEntity.getScore(), is(equalTo(1L)));
    }

    @Test
    public void testSetScoreReturnsGivenScoreAfterPersistingNewValue() {
        final EventEntity eventEntity = new EventEntity();
        final Long currentScore = 0L;
        eventEntity.setScore(currentScore);
        given(mockEventRepository.findOne(EVENT_ID)).willReturn(eventEntity);
        final long newScore = 11L;

        final Long resultScore = underTest.setScore(EVENT_ID, newScore);

        verify(mockEventRepository).findOne(EVENT_ID);
        verify(mockEventRepository).save(eventEntity);
        assertThat(resultScore, is(equalTo(newScore)));
        assertThat(eventEntity.getScore(), is(equalTo(newScore)));
    }

    @Test
    public void testSetScoreReturnsGivenScoreAfterPersistingNewlyCreatedEventScore() {
        given(mockEventRepository.findOne(EVENT_ID)).willReturn(null);
        given(mockEventRepository.save(argumentCaptor.capture())).willReturn(null);
        final long newScore = 11L;

        final Long resultScore = underTest.setScore(EVENT_ID, newScore);

        verify(mockEventRepository).findOne(EVENT_ID);
        final EventEntity savedEntity = argumentCaptor.getValue();
        verify(mockEventRepository).save(savedEntity);
        assertThat(resultScore, is(equalTo(newScore)));
        assertThat(savedEntity.getScore(), is(equalTo(newScore)));
    }

    @Test(expected = MissingEventCounterException.class)
    public void testScoreOfThrowsExceptionWhenEventIsNotPresentWithGivenId() {
        given(mockEventRepository.findOne(EVENT_ID)).willReturn(null);
        underTest.scoreOf(EVENT_ID);
    }

    @Test
    public void testScoreOfReturnsCurrentValueOfThePersistedEventScore() {
        final EventEntity entity = new EventEntity();
        final long currentValue = 42L;
        entity.setScore(currentValue);
        given(mockEventRepository.findOne(EVENT_ID)).willReturn(entity);

        final Long result = underTest.scoreOf(EVENT_ID);

        assertThat(result, is(equalTo(currentValue)));
    }
}
