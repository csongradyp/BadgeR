package net.csongradyp.badger.parser;

import java.io.File;
import java.net.URL;
import java.util.Map;
import javax.annotation.Resource;
import javax.inject.Named;
import net.csongradyp.badger.AchievementDefinition;
import net.csongradyp.badger.parser.file.FileType;
import org.apache.commons.io.FilenameUtils;

@Named
public class AchievementDefinitionFileParser implements IAchievementDefinitionFileParser {

    @Resource(name = "definitionFileParsers")
    private Map<FileType, IAchievementDefinitionFileParser> definitionFileParsers;

    @Override
    public AchievementDefinition parse(final URL achievementFile) {
        return parse(achievementFile.getFile());
    }

    @Override
    public AchievementDefinition parse(final String achievementFileLocation) {
        final File file = new File(achievementFileLocation);
        return parse(file);
    }

    @Override
    public AchievementDefinition parse(final File achievementFile) {
        FileType fileType = getType(achievementFile);
        return definitionFileParsers.get(fileType).parse(achievementFile);
    }

    private FileType getType(final File achievementFile) {
        final String extension = FilenameUtils.getExtension(achievementFile.getPath());
        return FileType.forExtension(extension);
    }

    public void setDefinitionFileParsers(final Map<FileType, IAchievementDefinitionFileParser> definitionFileParsers) {
        this.definitionFileParsers = definitionFileParsers;
    }
}
