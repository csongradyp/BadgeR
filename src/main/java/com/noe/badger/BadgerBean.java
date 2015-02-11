package com.noe.badger;

import com.noe.badger.bundle.AchievementBundle;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

public class BadgerBean {

    @Inject
    private AchievementBundle achievementBundle;

    public void setBundleSource(final File achievementFile) {
        try {
            achievementBundle.setSource(achievementFile);
        } catch (IOException e) {
            throw new IllegalStateException("Ini file not found!");
        }
    }

    public void unlock(final String id) {

    }

}
