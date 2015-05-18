package net.csongradyp.badger.parser;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.AchievementDefinition;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievementBean;

@Named
public class AchievementDefinitionFileParser implements IAchievementDefinitionFileParser {

    @Inject
    private AchievementIniParser iniParser;

    public AchievementDefinitionFileParser() {
    }

    @Override
    public AchievementDefinition parse(final File achievementFile) {
        return iniParser.parse(achievementFile);
    }

    @Override
    public AchievementDefinition parse(final String achievementFileLocation) {
        return iniParser.parse(achievementFileLocation);
    }

    @Override
    public AchievementDefinition parse(final URL achievementFile) {
        return iniParser.parse(achievementFile);
    }

    @Override
    public AchievementDefinition parse(final InputStream inputStream) {
        return iniParser.parse(inputStream);
    }

    @Override
    public IAchievementBean parse(AchievementType type, String achievementId) {
        return iniParser.parse(type, achievementId);
    }

}