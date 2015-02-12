package com.noe.badger.aspect;

import com.noe.badger.bundle.AchievementBundle;
import com.noe.badger.dao.CounterDao;
import javax.inject.Inject;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AchievementAspect {

    @Inject
    private CounterDao counterDao;
    @Inject
    private AchievementBundle achievementBundle;

    @Pointcut(value = "@annotation(achievementCountIncrement)", argNames = "achievementCountIncrement")
    public void achievementActivity(AchievementCountIncrement achievementCountIncrement) {
        achievementCountIncrement.counter();
    }

    @After("achievementActivity(achievementEvent)")
    public void increment(AchievementCountIncrement achievementEvent) {
        final String counter = achievementEvent.counter();
        counterDao.increment(counter);
//        checkForAchievement(counter);
    }

//    public void checkForAchievement(final String counter) {
//        List<Profile.Section> achievements = achievementBundle.getCounterAchievement( counter );
//        for (Profile.Section achievement : achievements) {
//            if (isFound(counter, achievement)) {
//                unlock(achievement);
//            }
//        }
//    }
//
//    protected boolean isFound(String counterName, Profile.Section achievement) {
//        Integer trigger = achievementBundle.getCounterValue(achievement);
//        return !isUnlocked(achievement) && isEqual(counterName, trigger);
//    }

//    private boolean isEqual(CounterName counterName, Integer achievementTriggerNumber) {
//        return achievementTriggerNumber.intValue() == counterDao.getValueOf(counterName);
//    }

}
