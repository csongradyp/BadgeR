package net.csongradyp.badger.persistence;

import net.csongradyp.badger.persistence.entity.AchievementEntity;
import net.csongradyp.badger.persistence.exception.UnlockedAchievementNotFoundException;
import net.csongradyp.badger.persistence.repository.AchievementRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AchievementDaoTest {

    public static final String ACHIEVEMENT_ID = "test";
    @Mock
    private AchievementRepository mockAchievementRepository;
    @Captor
    private ArgumentCaptor<HashSet<String>> captor;

    private AchievementDao underTest;

    @Before
    public void setUp() {
        underTest = new AchievementDao();
        underTest.setAchievementRepository(mockAchievementRepository);
    }

    @Test
    public void testDeleteAllDeletesAllEntriesFromRepository() {
        underTest.deleteAll();
        verify(mockAchievementRepository).deleteAll();
    }

    @Test
    public void testGetAllReturnsAllPersistedEntities() {
        final ArrayList<AchievementEntity> expected = new ArrayList<>();
        expected.add(new AchievementEntity());
        given(mockAchievementRepository.findAll()).willReturn(expected);

        final Collection<AchievementEntity> result = underTest.getAll();

        verify(mockAchievementRepository).findAll();
        assertThat(result, is(expected));
    }

    @Test
    public void testIsUnlockedReturnsTrueWhenAchievementIsPersistedWithGivenId() {
        given(mockAchievementRepository.findOne(ACHIEVEMENT_ID)).willReturn(new AchievementEntity());

        final Boolean result = underTest.isUnlocked(ACHIEVEMENT_ID);

        verify(mockAchievementRepository).findOne(ACHIEVEMENT_ID);
        assertThat(result, is(true));
    }

    @Test
    public void testIsUnlockedReturnsTrueWhenAchievementIsPersistedWithGivenIdAndGivenLevelIsLessThanOrEqualToThePersistedOne() {
        final AchievementEntity unlocked = new AchievementEntity();
        unlocked.setLevel(6);
        given(mockAchievementRepository.findOne(ACHIEVEMENT_ID)).willReturn(unlocked);

        final Boolean resultLessLevel = underTest.isUnlocked(ACHIEVEMENT_ID, 3);

        verify(mockAchievementRepository).findOne(ACHIEVEMENT_ID);
        assertThat(resultLessLevel, is(true));

        unlocked.setLevel(6);

        final Boolean resultEqualLevel = underTest.isUnlocked(ACHIEVEMENT_ID, 6);

        verify(mockAchievementRepository, times(2)).findOne(ACHIEVEMENT_ID);
        assertThat(resultEqualLevel, is(true));
    }

    @Test
    public void testIsUnlockedReturnsFalseWhenAchievementIsPersistedWithGivenIdAndGivenLevelIsGreaterThanThePersistedOne() {
        final AchievementEntity unlocked = new AchievementEntity();
        unlocked.setLevel(6);
        given(mockAchievementRepository.findOne(ACHIEVEMENT_ID)).willReturn(unlocked);

        final Boolean result = underTest.isUnlocked(ACHIEVEMENT_ID, 7);

        verify(mockAchievementRepository).findOne(ACHIEVEMENT_ID);
        assertThat(result, is(false));
    }

    @Test
    public void testIsUnlockedReturnsFalseWhenAchievementIsNotPersistedWithGivenId() {
        given(mockAchievementRepository.findOne(ACHIEVEMENT_ID)).willReturn(null);

        final Boolean result = underTest.isUnlocked(ACHIEVEMENT_ID);

        verify(mockAchievementRepository).findOne(ACHIEVEMENT_ID);
        assertThat(result, is(false));
    }

    @Test
    public void testGetNumberOfUnlockedReturnsTheNumberOfPersistedUnlockedAchievements() {
        long expectedValue = 5L;
        given(mockAchievementRepository.count()).willReturn(expectedValue);

        Long numberOfUnlocked = underTest.getNumberOfUnlocked();

        verify(mockAchievementRepository).count();
        assertThat(numberOfUnlocked, is(equalTo(expectedValue)));
    }

    @Test
    public void testUnlockSavesAchievementWithGivenIdAndWithLevelOne() {
        ArgumentCaptor<AchievementEntity> argumentCaptor = ArgumentCaptor.forClass(AchievementEntity.class);
        when(mockAchievementRepository.save(argumentCaptor.capture())).thenReturn(new AchievementEntity());

        underTest.unlock(ACHIEVEMENT_ID);

        verify(mockAchievementRepository).save(any(AchievementEntity.class));
        AchievementEntity entity = argumentCaptor.getValue();
        assertThat(entity, notNullValue());
        assertThat(entity.getId(), is(equalTo(ACHIEVEMENT_ID)));
        assertThat(entity.getLevel(), is(equalTo(1)));
        assertThat(entity.getOwners(), is(Collections.emptySet()));
    }

    @Test
    public void testUnlockSavesAchievementWithGivenIdAndLevel() {
        final Integer level = 5;
        ArgumentCaptor<AchievementEntity> argumentCaptor = ArgumentCaptor.forClass(AchievementEntity.class);
        when(mockAchievementRepository.save(argumentCaptor.capture())).thenReturn(new AchievementEntity());

        underTest.unlock(ACHIEVEMENT_ID, level);

        verify(mockAchievementRepository).save(any(AchievementEntity.class));
        AchievementEntity entity = argumentCaptor.getValue();
        assertThat(entity, notNullValue());
        assertThat(entity.getId(), is(equalTo(ACHIEVEMENT_ID)));
        assertThat(entity.getLevel(), is(equalTo(level)));
        assertThat(entity.getOwners(), is(Collections.emptySet()));
    }

    @Test
    public void testGetAcquireDateReturnsPersistedEntityAcquireDate() {
        final Date acquireDate = new Date();
        final AchievementEntity entity = new AchievementEntity();
        entity.setAcquireDate(acquireDate);
        when(mockAchievementRepository.findOne(ACHIEVEMENT_ID)).thenReturn(entity);

        underTest.getAcquireDate(ACHIEVEMENT_ID);

        verify(mockAchievementRepository).findOne(ACHIEVEMENT_ID);
        assertThat(entity, notNullValue());
        assertThat(entity.getAcquireDate(), is(equalTo(acquireDate)));
    }

    @Test(expected = UnlockedAchievementNotFoundException.class)
    public void testGetAcquireDateThrowsExceptionWhenNoAchievementIsPersistedWithGivenId() {
        when(mockAchievementRepository.findOne(ACHIEVEMENT_ID)).thenReturn(null);
        underTest.getAcquireDate(ACHIEVEMENT_ID);
        verify(mockAchievementRepository).findOne(ACHIEVEMENT_ID);
    }

    @Test
    public void testGetAllByOwner() {
        final String testOwner = "test owner";
        final ArrayList<AchievementEntity> expectedResult = new ArrayList<>();
        when(mockAchievementRepository.findByOwnersIn(captor.capture())).thenReturn(expectedResult);

        final Collection<AchievementEntity> result = underTest.getAllByOwner(testOwner);

        final HashSet<String> owners = captor.getValue();
        verify(mockAchievementRepository).findByOwnersIn(owners);
        assertThat(owners.contains(testOwner), is(true));
        assertThat(result, is(expectedResult));
    }

}
