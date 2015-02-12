package com.noe.badger;

import com.noe.badger.bundle.AchievementBundle;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class BadgerBean {

    @Inject
    private AchievementBundle achievementBundle;
    private String internationalizationBaseName;
//    @Inject
//    private LanguageBundle languageBundle;

    public void setBundleSource(final File achievementFile) {
        try {
            achievementBundle.setSource(achievementFile);
        } catch (IOException e) {
            throw new IllegalStateException("Ini file not found!");
        }
    }

    public void setBundleSource(final InputStream inputStream) {
        try {
            achievementBundle.setSource(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Ini file not found!");
        }
    }

    public String getInternationalizationBaseName() {
        return internationalizationBaseName;
    }

    public void setInternationalizationBaseName(String internationalizationBaseName) {
        this.internationalizationBaseName = internationalizationBaseName;
    }

    //    public void setLanguageFiles(final List<Properties> internationalizationFiles) {
//        languageBundle.setInternationalizationFiles(internationalizationFiles);
//    }

    public void unlock(final String id) {

    }

}
