package net.csongradyp.badger.parser;

import net.csongradyp.badger.AchievementDefinition;

import java.io.File;
import java.net.URL;

public interface IAchievementDefinitionFileParser {

    AchievementDefinition parse(File achievementFile);

    AchievementDefinition parse(String achievementFileLocation);

    AchievementDefinition parse(URL achievementFile);

}
