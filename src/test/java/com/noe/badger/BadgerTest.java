package com.noe.badger;

import com.noe.badger.aspect.AchievementCounterIncrement;
import com.noe.badger.event.AchievementHandler;
import com.noe.badger.event.domain.Achievement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith( MockitoJUnitRunner.class)
public class BadgerTest {

    @Mock
    private AchievementHandler mockAchievementHandler;
    private Badger badger;

    @Before
    public void setUp(){
        final InputStream stream = Main.class.getClassLoader().getResourceAsStream( "test.ini" );
        badger = new Badger(stream, "msg");
        badger.subscribe(mockAchievementHandler);
    }

    @Test
    public void testName() throws Exception {
        for ( int i = 0; i < 12; i++ ) {
            increment(i);
        }
        verify(mockAchievementHandler).onUnlocked(any(Achievement.class));
    }

    @AchievementCounterIncrement(counter = "first")
    public void increment( int i ){
        System.out.println(i);
    }

    @After
    public void tearDown() {
        badger.unSubscribe( mockAchievementHandler );
    }
}
