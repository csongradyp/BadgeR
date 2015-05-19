package net.csongradyp.badger.persistence;

import java.util.Optional;
import net.csongradyp.badger.persistence.entity.AchievementEntity;
import net.csongradyp.badger.persistence.repository.AchievementRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AchievementDaoTest {

    public static final String ACHIEVEMENT_ID = "test";
    @Mock
    private AchievementRepository mockAchievementRepository;

    private AchievementDao underTest;

    @Before
    public void setUp() {
        underTest = new AchievementDao();
        underTest.setAchievementRepository(mockAchievementRepository);
    }

    @Test
    public void testDeleteAllDeletesAllEntriesFromReposotory() throws Exception {
        underTest.deleteAll();
        verify(mockAchievementRepository).deleteAll();
    }

    @Test
    public void testUnlockCreatesNewEntityWhenNoAchievementIsPresentWithTheGivenId() throws Exception {
        given(mockAchievementRepository.findById(ACHIEVEMENT_ID)).willReturn(Optional.empty());
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

}
