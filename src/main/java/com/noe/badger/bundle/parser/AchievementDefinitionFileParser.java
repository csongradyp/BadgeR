package com.noe.badger.bundle.parser;

import com.noe.badger.bundle.AchievementBundle;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class AchievementDefinitionFileParser implements IAchievementDefinitionFileParser {

    @Inject
    private AchievementIniParser iniParser;

    public AchievementDefinitionFileParser() {
    }

    @Override
    public AchievementBundle parse(final File achievementFile) {
        return iniParser.parse(achievementFile);
    }

    @Override
    public AchievementBundle parse(final String achievementFileLocation) {
        return iniParser.parse(achievementFileLocation);
    }

    @Override
    public AchievementBundle parse(final URL achievementFile) {
        return iniParser.parse(achievementFile);
    }

    @Override
    public AchievementBundle parse(final InputStream inputStream) {
        return iniParser.parse(inputStream);
    }
    
}
