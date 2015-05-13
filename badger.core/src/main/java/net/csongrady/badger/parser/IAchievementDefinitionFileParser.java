package net.csongrady.badger.parser;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import net.csongrady.badger.AchievementDefinition;
import net.csongrady.badger.domain.AchievementType;
import net.csongrady.badger.domain.IAchievementBean;

public interface IAchievementDefinitionFileParser {

    AchievementDefinition parse(File achievementFile);

    AchievementDefinition parse(String achievementFileLocation);

    AchievementDefinition parse(URL achievementFile);

    AchievementDefinition parse(InputStream inputStream);

    IAchievementBean parse(final AchievementType type, final String achievementId);
}
