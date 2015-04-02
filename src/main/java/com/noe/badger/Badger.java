package com.noe.badger;

import com.noe.badger.bundle.domain.IAchievement;
import com.noe.badger.event.EventBus;
import com.noe.badger.event.handler.AchievementUnlockedHandlerWrapper;
import com.noe.badger.event.handler.AchievementUpdateHandlerWrapper;
import com.noe.badger.event.handler.IAchievementUnlockedHandler;
import com.noe.badger.event.handler.IAchievementUpdateHandler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Badger {

    private static final String CONTEXT_XML_PATH = "META-INF/beans.xml";

    private final AchievementController controller;

    private Badger() {
        final ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext(CONTEXT_XML_PATH);
        applicationContext.registerShutdownHook();
        controller = applicationContext.getBean(AchievementController.class);
    }

    public Badger(final InputStream inputStream, final String baseName) {
        this();
        controller.setSource(inputStream);
        controller.setInternationalizationBaseName(baseName);
    }

    public Badger(final File achievementIni, final String baseName) {
        this();
        controller.setSource(achievementIni);
        controller.setInternationalizationBaseName(baseName);
    }

    public Badger(final String achievementIniLocation, final String baseName) throws IOException {
        this();
        controller.setSource(new File(achievementIniLocation));
        controller.setInternationalizationBaseName(baseName);
    }

    public void setLocale(final Locale locale) {
        controller.setLocale(locale);
    }

    public void unlock(final AchievementType type, final String id, final String triggeredValue) {
        controller.unlock(type, id, triggeredValue);
    }

    public Collection<IAchievement> getAllAchivement() {
        return controller.getAll();
    }

    public Map<String, Set<IAchievement>> getAllAchievementByEvent() {
        return controller.getAllByEvents();
    }

    public void triggerEvent(final String event) {
        controller.triggerEvent(event);
    }

    public void triggerEvent(final String id, final Long score) {
        controller.triggerEvent(id, score);
    }

    public Long getCurrentScore(final String id) {
        return controller.getCurrentScore(id);
    }

    public void subscribeOnUnlock(final IAchievementUnlockedHandler achievementUnlockedHandler) {
        EventBus.subscribeOnUnlock(new AchievementUnlockedHandlerWrapper(achievementUnlockedHandler));
    }

    public void unSubscribeOnUnlock(final IAchievementUnlockedHandler achievementUnlockedHandler) {
        EventBus.unSubscribeOnUnlock(achievementUnlockedHandler);
    }

    public void subscribeOnUnlock(final IAchievementUpdateHandler achievementUpdateHandler) {
        EventBus.subscribeOnUpdate(new AchievementUpdateHandlerWrapper(achievementUpdateHandler));
    }

    public void unSubscribeOnUpdate(final IAchievementUpdateHandler achievementUpdateHandler) {
        EventBus.unSubscribeOnUpdate(achievementUpdateHandler);
    }

}
