package net.csongradyp.badger.aop;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import net.csongradyp.badger.IAchievementController;
import net.csongradyp.badger.annotations.EventTrigger;
import net.csongradyp.badger.annotations.TriggerValue;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class TriggerAspect extends AchievementAspect {

    @Inject
    private IAchievementController achievementController;

    @Pointcut(value = "execution(* *(..)) && @annotation(achievementScore)", argNames = "achievementScore")
    public void triggerEntryPoint(final EventTrigger achievementScore) {
    }

    @After(value = "triggerEntryPoint(achievementScore)", argNames = "joinPoint, achievementScore")
    public void trigger(final JoinPoint joinPoint, final EventTrigger achievementScore) {
        final List<String> owners = collectOwners(joinPoint, achievementScore.owners());
        final List<String> events = collectEvents(joinPoint, achievementScore.events());
        final Optional<Long> score = getScore(joinPoint);
        for (String event : events) {
            if(score.isPresent()) {
                achievementController.triggerEvent(event, score.get(), owners);
            } else {
                achievementController.triggerEvent(event, owners);
            }
        }
    }

    protected Optional<Long> getScore(final JoinPoint joinPoint) {
        Long score = null;
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Object[] parameterValues = joinPoint.getArgs();
        final Class[] parameterTypes = signature.getParameterTypes();

        final Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            final TriggerValue paramAnnotation = getAnnotationByType(parameterAnnotations[i], TriggerValue.class);
            if (paramAnnotation != null) {
                if (paramAnnotation.getter().isEmpty()) {
                    if (Long.class.equals(parameterTypes[i])) {
                        score = (Long) parameterValues[i];
                    }
                } else {
                    score = callGetter(parameterValues[i], paramAnnotation.getter(), Long.class);
                }
            }
        }
        return Optional.ofNullable(score);
    }
}
