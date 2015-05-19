package net.csongradyp.badger.parser;

import java.io.File;
import java.net.URL;
import net.csongradyp.badger.AchievementDefinition;

public interface IAchievementDefinitionFileParser {

    AchievementDefinition parse(File achievementFile);

    AchievementDefinition parse(String achievementFileLocation);

    AchievementDefinition parse(URL achievementFile);

}
