package com.noe.badger;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

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

    public void unlock(final String id) {
        controller.unlock(id);
    }

}
