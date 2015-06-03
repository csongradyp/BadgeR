package net.csongradyp.badger.aop;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import net.csongradyp.badger.IAchievementController;
import net.csongradyp.badger.annotations.AchievementId;
import net.csongradyp.badger.annotations.AchievementTriggerValue;
import net.csongradyp.badger.annotations.AchievementUnlock;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class UnlockAspect extends AchievementAspect {

    @Inject
    private IAchievementController achievementController;

    @Pointcut(value = "execution(* *(..)) && @annotation(achievementUnlock)", argNames = "achievementUnlock")
    public void unlockEntryPoint(final AchievementUnlock achievementUnlock) {
    }

    @After(value = "unlockEntryPoint(achievementUnlock)", argNames = "joinPoint, achievementUnlock")
    public void unlock(final JoinPoint joinPoint, final AchievementUnlock achievementUnlock) {
        final List<String> owners = collectOwners(joinPoint, achievementUnlock.owners());
        final List<String> achievementIds = CollectIds(joinPoint, achievementUnlock.achievements());
        String triggerValue = getTriggerValue(joinPoint, achievementUnlock.triggerValue());
        for (String achievementId : achievementIds) {
            achievementController.unlock(achievementId, triggerValue, owners);
        }
    }

    private List<String> CollectIds(final JoinPoint joinPoint, final String[] annotationIds) {
        final List<String> achievementIds = getAchievementIds(joinPoint);
        achievementIds.addAll(Arrays.asList(annotationIds));
        return achievementIds;
    }

    private List<String> getAchievementIds(final JoinPoint joinPoint) {
        final List<String> achievementIds = new ArrayList<>();
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Object[] parameterValues = joinPoint.getArgs();
        final Class[] parameterTypes = signature.getParameterTypes();

        final Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            final AchievementId paramAnnotation = getAnnotationByType(parameterAnnotations[i], AchievementId.class);
            if (paramAnnotation != null && String.class.equals(parameterTypes[i])) {
                achievementIds.add(String.valueOf(parameterValues[i]));
            }
        }
        return achievementIds;
    }

    private String getTriggerValue(final JoinPoint joinPoint, final String annotationTriggerValue) {
        if (!annotationTriggerValue.isEmpty()) {
            return annotationTriggerValue;
        }
        return getTriggerValue(joinPoint);
    }

    private String getTriggerValue(final JoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Object[] parameterValues = joinPoint.getArgs();

        final Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            final AchievementTriggerValue paramAnnotation = getAnnotationByType(parameterAnnotations[i], AchievementTriggerValue.class);
            if (paramAnnotation != null) {
                return String.valueOf(parameterValues[i]);
            }
        }
        return "";
    }
}
