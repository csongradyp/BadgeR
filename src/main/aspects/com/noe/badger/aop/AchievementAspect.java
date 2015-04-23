package com.noe.badger.aop;

import com.noe.badger.annotations.AchievementCheck;
import com.noe.badger.annotations.AchievementEventTrigger;
import com.noe.badger.annotations.AchievementScore;
import com.noe.badger.annotations.AchievementUnlock;
import com.noe.badger.annotations.AchievementOwnerParam;
import com.noe.badger.event.EventBus;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

@Aspect
public class AchievementAspect {

    Logger LOG = LoggerFactory.getLogger(AchievementAspect.class);

    @Pointcut(value = "execution(* *(..)) && @annotation(achievementCheck)", argNames = "achievementCheck")
    public void checkEntryPoint(final AchievementCheck achievementCheck) {
    }

    @After(value = "checkEntryPoint(achievementCheck)", argNames = "achievementCheck")
    public void check(final AchievementCheck achievementCheck) {
        LOG.debug("Checking for achievements");
        EventBus.checkAll();
    }

    @Pointcut(value = "execution(* *(..)) && @annotation(achievementUnlock)", argNames = "achievementUnlock")
    public void unlockEntryPoint(final AchievementUnlock achievementUnlock) {
    }

    @After(value = "unlockEntryPoint(achievementUnlock)", argNames = "joinPoint, achievementUnlock")
    public void unlock(final JoinPoint joinPoint, final AchievementUnlock achievementUnlock) {
        final ArrayList<String> owners = getOwners(joinPoint);
        Collections.addAll(owners, achievementUnlock.owners());

        final String achievement = achievementUnlock.achievement();
        final String scoreParam = achievementUnlock.scoreParam();
        String triggerValue = "";
        if (!scoreParam.isEmpty()) {
            final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            final String[] parameterNames = signature.getParameterNames();
            final Object[] parameterValues = joinPoint.getArgs();
            for (int i = 0, parameterNamesLength = parameterNames.length; i < parameterNamesLength; i++) {
                if (scoreParam.equals(parameterNames[i])) {
                    triggerValue = String.valueOf(parameterValues[i]);
                }
            }
        }
        LOG.debug("Achievement {} unlocked with value: {}", achievement, triggerValue);
        EventBus.unlock(achievement, triggerValue);
    }

    @Pointcut(value = "(execution(* *(..)) || execution(*.new(..))) && @annotation(achievementEventTrigger)", argNames = "achievementEventTrigger")
    public void triggerEntryPoint(final AchievementEventTrigger achievementEventTrigger) {
    }

    @After(value = "triggerEntryPoint(achievementEventTrigger)", argNames = "joinPoint, achievementEventTrigger")
    public void trigger(final JoinPoint joinPoint, final AchievementEventTrigger achievementEventTrigger) {
        final ArrayList<String> owners = getOwners(joinPoint);
        Collections.addAll(owners, achievementEventTrigger.owners());

        final String[] events = achievementEventTrigger.name();
        for (String event : events) {
            LOG.debug("Achievement event triggered: {} by {}", event, owners);
            EventBus.triggerEvent(event, owners);
        }
    }

    private ArrayList<String> getOwners(JoinPoint joinPoint) {
        final ArrayList<String> owners = new ArrayList<>();
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Object[] parameterValues = joinPoint.getArgs();
        final Class[] parameterTypes = signature.getParameterTypes();

        final Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();
        for(int i = 0; i < parameterAnnotations.length; i++){
            final AchievementOwnerParam paramAnnotation = getAnnotationByType(parameterAnnotations[i], AchievementOwnerParam.class);
            if(paramAnnotation != null){
                if(paramAnnotation.getter().isEmpty()) {
                    if(String.class.equals(parameterTypes[i])) {
                        final String owner = (String) parameterValues[i];
                        owners.add(owner);
                    }
                } else {
                    final String owner = callOwnerGetter(parameterValues[i], paramAnnotation.getter());
                    owners.add(owner);
                }
            }
        }
        return owners;
    }

    @SuppressWarnings("unchecked")
    private <T extends Annotation> T getAnnotationByType(final Annotation[] annotations, final Class<T> clazz){
        T result = null;
        for(final Annotation annotation : annotations){
            if(clazz.isAssignableFrom(annotation.getClass())){
                result = (T) annotation;
                break;
            }
        }
        return result;
    }

    private String callOwnerGetter(final Object ownerObject, final String methodNameToCall ) {
        String owner = null;
        try {
            final Method ownerResolverMethod = ownerObject.getClass().getDeclaredMethod(methodNameToCall);
            if(String.class.equals(ownerResolverMethod.getReturnType())) {
                owner = (String) ownerResolverMethod.invoke(ownerObject, null);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOG.error("Could not call getter method: {} for given Owner object: {}", methodNameToCall, ownerObject);
            LOG.error("Could not call getter method", e);
        }
        return owner;
    }

    @Pointcut(value = "(execution(* *(..)) || execution(*.new(..))) && @annotation(achievementScore)", argNames = "achievementScore")
    public void scoreEntryPoint(final AchievementScore achievementScore) {
    }

    @After(value = "scoreEntryPoint(achievementScore)", argNames = "joinPoint, achievementScore")
    public void score(final JoinPoint joinPoint, final AchievementScore achievementScore) {
        final ArrayList<String> owners = getOwners(joinPoint);
        Collections.addAll(owners, achievementScore.owners());

        final String id = achievementScore.counter();
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final String[] parameterNames = signature.getParameterNames();
        final Object[] parameterValues = joinPoint.getArgs();
        final Class[] parameterTypes = signature.getParameterTypes();
        for (int i = 0, parameterNamesLength = parameterNames.length; i < parameterNamesLength; i++) {
            if (achievementScore.scoreParam().equals(parameterNames[i]) && (long.class.equals(parameterTypes[i]) || Long.class.equals(parameterTypes[i]))) {
                final Long score = (Long) parameterValues[i];
                LOG.debug("Achievement event triggered: {} by {} with score: {}", id, owners, score);
                EventBus.triggerEvent(id, score);
            }
        }
    }

}
