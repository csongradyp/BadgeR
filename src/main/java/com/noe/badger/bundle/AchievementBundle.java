package com.noe.badger.bundle;

import com.noe.badger.AchievementType;
import org.ini4j.Ini;
import org.ini4j.Profile;

import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Named
public class AchievementBundle {

    private Ini achievements;

    public void setSource(final File achievementFile) throws IOException {
        achievements = new Ini(achievementFile);
        achievements.getConfig().setMultiOption(true);
    }

    public void setSource(final String achievementFileLocation) throws IOException {
        achievements = new Ini(new File(achievementFileLocation));
    }

    public void setSourcee(final URL achievementFile) throws IOException {
        achievements = new Ini(achievementFile);
    }

    public Profile.Section getAchievements(final AchievementType type) {
        return achievements.get(type.toString());
    }

//    public List<Profile.Section> getCounterAchievement( String counter ) {
//        return achievements.get;
//    }
}
