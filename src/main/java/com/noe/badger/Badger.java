package com.noe.badger;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Badger {

    private static final String CONTEXT_XML_PATH = "META-INF/beans.xml";

    private final BadgerBean badger;

    public Badger() {
        final ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext(CONTEXT_XML_PATH);
        applicationContext.registerShutdownHook();
        badger = applicationContext.getBean(BadgerBean.class);
    }

    public Badger(final File achievementIni, final List<Properties> internationalizationFiles) {
        this();
        badger.setBundleSource(achievementIni);
    }

    public Badger(final String achievementIniLocation, final List<String> internationalizationFileLocation) throws IOException {
        this();
        final List<Properties> internationalizationFiles = new ArrayList<>(internationalizationFileLocation.size());
        for (String fileLocation : internationalizationFileLocation) {
            final Properties properties = new Properties();
            try (InputStream inputStream = Badger.class.getClassLoader().getResourceAsStream(fileLocation)) {
                properties.load(inputStream);
                internationalizationFiles.add(properties);
            }
        }
        badger.setBundleSource(new File(achievementIniLocation));
    }

}
