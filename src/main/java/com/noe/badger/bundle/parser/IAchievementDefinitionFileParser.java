package com.noe.badger.bundle.parser;

import com.noe.badger.bundle.AchievementBundle;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

public interface IAchievementDefinitionFileParser {

    AchievementBundle parse(File achievementFile);

    AchievementBundle parse(String achievementFileLocation);

    AchievementBundle parse(URL achievementFile);

    AchievementBundle parse(InputStream inputStream);
}
