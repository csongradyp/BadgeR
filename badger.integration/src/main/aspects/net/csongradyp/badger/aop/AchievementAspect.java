package net.csongradyp.badger.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.csongradyp.badger.annotations.AchievementEventParam;
import net.csongradyp.badger.annotations.AchievementOwnerParam;
import net.csongradyp.badger.annotations.AchievementScoreParam;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AchievementAspect {

    Logger LOG = LoggerFactory.getLogger(AchievementAspect.class);

    protected List<String> collectOwners(final JoinPoint joinPoint, final String[] annotationOwners) {
        final List<String> owners = getOwners(joinPoint);
        owners.addAll(Arrays.asList(annotationOwners));
        return owners;
    }

    private List<String> getOwners(final JoinPoint joinPoint) {
        final List<String> owners = new ArrayList<>();
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Object[] parameterValues = joinPoint.getArgs();
        final Class[] parameterTypes = signature.getParameterTypes();

        final Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            final AchievementOwnerParam paramAnnotation = getAnnotationByType(parameterAnnotations[i], AchievementOwnerParam.class);
            if (paramAnnotation != null) {
                if (paramAnnotation.getter().isEmpty()) {
                    if (String.class.equals(parameterTypes[i])) {
                        final String owner = (String) parameterValues[i];
                        owners.add(owner);
                    }
                } else {
                    final String owner = callGetter(parameterValues[i], paramAnnotation.getter(), String.class);
                    owners.add(owner);
                }
            }
        }
        return owners;
    }

    protected List<String> collectEvents(final JoinPoint joinPoint, final String[] eventNames) {
        final List<String> events = getEvents(joinPoint);
        events.addAll(Arrays.asList(eventNames));
        return events;
    }

    private List<String> getEvents(final JoinPoint joinPoint) {
        final List<String> events = new ArrayList<>();
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Object[] parameterValues = joinPoint.getArgs();
        final Class[] parameterTypes = signature.getParameterTypes();

        final Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            final AchievementEventParam paramAnnotation = getAnnotationByType(parameterAnnotations[i], AchievementEventParam.class);
            if (paramAnnotation != null && String.class.equals(parameterTypes[i])) {
                events.add(String.valueOf(parameterValues[i]));
            }
        }
        return events;
    }

    @SuppressWarnings("unchecked")
    protected <T extends Annotation> T getAnnotationByType(final Annotation[] annotations, final Class<T> clazz) {
        T result = null;
        for (final Annotation annotation : annotations) {
            if (clazz.isAssignableFrom(annotation.getClass())) {
                result = (T) annotation;
                break;
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private <T> T callGetter(final Object target, final String methodNameToCall, final Class<T> resultClass) {
        T result = null;
        try {
            final Method ownerResolverMethod = target.getClass().getDeclaredMethod(methodNameToCall);
            if (resultClass.equals(ownerResolverMethod.getReturnType())) {
                result = (T) ownerResolverMethod.invoke(target, null);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOG.error("Could not call getter method: {} for given object: {}", methodNameToCall, target);
            LOG.error("Could not call getter method", e);
        }
        return result;
    }

    protected Long getScore(final JoinPoint joinPoint) {
        Long score = Long.MIN_VALUE;
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Object[] parameterValues = joinPoint.getArgs();
        final Class[] parameterTypes = signature.getParameterTypes();

        final Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            final AchievementScoreParam paramAnnotation = getAnnotationByType(parameterAnnotations[i], AchievementScoreParam.class);
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
        return score;
    }
}
