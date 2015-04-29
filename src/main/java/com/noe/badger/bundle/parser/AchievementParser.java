package com.noe.badger.bundle.parser;

import com.noe.badger.bundle.AchievementBundle;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class AchievementParser {

    @Inject
    private AchievementIniParser iniParser;

    public AchievementParser() {
    }

    public AchievementBundle parse(final File achievementFile) {
        return iniParser.parseSource(achievementFile);
    }

    public AchievementBundle parse(final String achievementFileLocation) {
        return iniParser.parseSource(achievementFileLocation);
    }
    
    public AchievementBundle parse(final URL achievementFile) {
        return iniParser.parseSource(achievementFile);
    }

    public AchievementBundle parse(final InputStream inputStream) {
        return iniParser.parseSource(inputStream);
    }
    
}
