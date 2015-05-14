package net.csongradyp.badger.parser;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import net.csongradyp.badger.AchievementDefinition;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievementBean;

public interface IAchievementDefinitionFileParser {

    AchievementDefinition parse(File achievementFile);

    AchievementDefinition parse(String achievementFileLocation);

    AchievementDefinition parse(URL achievementFile);

    AchievementDefinition parse(InputStream inputStream);

    IAchievementBean parse(final AchievementType type, final String achievementId);
}
