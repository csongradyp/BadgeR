package com.noe.badger;

import com.noe.badger.bundle.domain.IAchievement;
import com.noe.badger.bundle.parser.AchievementParser;
import com.noe.badger.event.EventBus;
import com.noe.badger.event.handler.AchievementUnlockedHandlerWrapper;
import com.noe.badger.event.handler.IAchievementUnlockedHandler;
import com.noe.badger.event.handler.IScoreUpdateHandler;
import com.noe.badger.event.handler.ScoreUpdateHandlerWrapper;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Badger {

    private static final String CONTEXT_XML_PATH = "META-INF/beans.xml";

    private final AchievementParser parser;
    private final AchievementController controller;

    private Badger() {
        final ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext(CONTEXT_XML_PATH);
        applicationContext.registerShutdownHook();
        parser = applicationContext.getBean(AchievementParser.class);
        controller = applicationContext.getBean(AchievementController.class);
    }

    public Badger(final InputStream inputStream, final String baseName) {
        this();
        controller.setDefinition(parser.parse(inputStream));
        controller.setInternationalizationBaseName(baseName);
    }

    public Badger(final File achievementIni, final String baseName) {
        this();
        controller.setDefinition(parser.parse(achievementIni));
        controller.setInternationalizationBaseName(baseName);
    }

    public Badger(final String achievementIniLocation, final String baseName) {
        this();
        controller.setDefinition(parser.parse(new File(achievementIniLocation)));
        controller.setInternationalizationBaseName(baseName);
    }

    public Badger(final InputStream inputStream) {
        this();
        controller.setDefinition(parser.parse(inputStream));
    }

    public Badger(final File achievementIni) {
        this();
        controller.setDefinition(parser.parse(achievementIni));
    }

    public Badger(final String achievementIniLocation) {
        this();
        controller.setDefinition(parser.parse(new File(achievementIniLocation)));
    }

    /**
     * Set the language to use for resolving unlocked achievements title and description.
     * @param locale {@link java.util.Locale} instance.
     */
    public void setLocale(final Locale locale) {
        controller.setLocale(locale);
    }

    public void unlock(final AchievementType type, final String id, final String triggeredValue) {
        controller.unlock(type, id, triggeredValue);
    }

    public void unlock(final AchievementType type, final String id, final String triggeredValue, final String... owners) {
        controller.unlock(type, id, triggeredValue, owners);
    }

    public Collection<IAchievement> getAllAchivement() {
        return controller.getAll();
    }

    public Collection<IAchievement> getAchievementsByOwner(final String owner) {
        return controller.getByOwner(owner);
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

    public void triggerEvent(final String event, final Collection<String> owners) {
        controller.triggerEvent(event, owners);
    }

    public void triggerEvent(final String event, final String... owners) {
        controller.triggerEvent(event, owners);
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

    public void subscribeOnScoreChanged(final IScoreUpdateHandler achievementUpdateHandler) {
        EventBus.subscribeOnScoreChanged(new ScoreUpdateHandlerWrapper(achievementUpdateHandler));
    }

    public void unSubscribeOnScoreChanged(final IScoreUpdateHandler achievementUpdateHandler) {
        EventBus.unSubscribeOnScoreChanged(achievementUpdateHandler);
    }

    public void reset() {
        controller.reset();
    }

}
