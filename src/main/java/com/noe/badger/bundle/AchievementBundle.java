package com.noe.badger.bundle;

import com.noe.badger.AchievementType;
import com.noe.badger.bundle.domain.CounterAchievementBean;
import com.noe.badger.bundle.domain.DateAchievementBean;
import com.noe.badger.bundle.domain.IAchievementBean;
import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile;

import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Named
public class AchievementBundle {

    private Ini achievements;

    public void setSource(final File achievementFile) {
        try {
            achievements = new Ini(achievementFile);

            achievements.getConfig().setMultiOption(true);
            final Config config = new Config();
            config.setMultiOption(true);
            achievements.setConfig(config);
        } catch ( IOException e ) {
            throw new IllegalStateException("Ini file not found!");
        }
    }

    public void setSource(final String achievementFileLocation) {
        try {
            achievements = new Ini(new File(achievementFileLocation));
        } catch ( IOException e ) {
            throw new IllegalStateException("Ini file not found!");
        }
    }

    public void setSource(final URL achievementFile) {
        try {
            achievements = new Ini(achievementFile);
        } catch ( IOException e ) {
            throw new IllegalStateException("Ini file not found!");
        }
    }

    public void setSource(final InputStream inputStream) {
        try {
            achievements = new Ini(inputStream);
        } catch ( IOException e ) {
            throw new IllegalStateException("Ini file not found!");
        }
    }

    public IAchievementBean<Long> getCounterAchievement(final String id) {
        return getCounterAchievement(AchievementType.COUNTER, id);
    }

    public IAchievementBean<Long> getSingleAchievement(final String id) {
        return getCounterAchievement(AchievementType.SINGLE, id);
    }

    public IAchievementBean<Date> getDateAchievement(final String id) {
        return getDateAchievement( AchievementType.DATE, id );
    }

    public IAchievementBean<Date> getTimeAchievement(final String id) {
        return getDateAchievement(AchievementType.TIME, id);
    }

    public IAchievementBean<Long> getCounterAchievement(final AchievementType type, final String id) {
        Ini.Section section = achievements.get(type.getType()).getChild(id);
        if (section != null) {
            final IAchievementBean<Long> counterAchievement = new CounterAchievementBean();
            return parseSection(id, section, counterAchievement);
        }
        return null;
    }

    public IAchievementBean<Date> getDateAchievement(final AchievementType type, final String id) {
        Ini.Section section = achievements.get(type.getType()).getChild(id);
        if (section != null) {
            final IAchievementBean<Date> counterAchievement = new DateAchievementBean();
            return parseSection(id, section, counterAchievement);
        }
        return null;
    }

    public IAchievementBean getAchievement(final String id) {
        final AchievementType[] values = AchievementType.values();
        for ( AchievementType value : values ) {
            final Profile.Section section = achievements.get(value.getType());
            if( section.keySet().contains(id)) {
                switch ( value ) {
                case DATE:
                    return parseSection(id, section, new DateAchievementBean());
                case TIME:
                    return parseSection(id, section, new DateAchievementBean());
                case COUNTER:
                    return parseSection(id, section, new CounterAchievementBean());
                case DATE_COUNTER:
                    break;
                case TIMED_COUNTER:
                    break;
                case SINGLE:
                    break;
                }
            }
        }
        return null;
    }

    private IAchievementBean parseSection(String id, Profile.Section section, IAchievementBean achievement ) {
        section.to(achievement);
        achievement.setId(id);
        achievement.setTrigger(section.getAll("trigger", String[].class));
        return achievement;
    }
}
