package com.noe.badger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Badger {

    private static final String CONTEXT_XML_PATH = "META-INF/beans.xml";

    private final BadgerBean badger;

    private Badger() {
        final ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext(CONTEXT_XML_PATH);
        applicationContext.registerShutdownHook();
        badger = applicationContext.getBean(BadgerBean.class);
    }

    public Badger(final InputStream inputStream, final String baseName) {
        this();
        badger.setBundleSource(inputStream);
        badger.setInternationalizationBaseName(baseName);
    }

    public Badger(final File achievementIni, final String baseName) {
        this();
        badger.setBundleSource(achievementIni);
        badger.setInternationalizationBaseName(baseName);
    }

    public Badger(final String achievementIniLocation, final String baseName) throws IOException {
        this();
        badger.setBundleSource(new File(achievementIniLocation));
        badger.setInternationalizationBaseName(baseName);
    }

}
