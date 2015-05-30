package net.csongradyp.badger.parser.file;

import net.csongradyp.badger.parser.excpetion.UnsupportedAchievementDefinitionFileType;

public enum FileType {

    INI("ini"),
    JSON("json");

    private final String extension;

    FileType(final String mimeType) {
        this.extension = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public static FileType forExtension(final String extension) {
        for (FileType fileType : values()) {
            if(fileType.getExtension().equals(extension.toLowerCase())) {
                return fileType;
            }
        }
        throw new UnsupportedAchievementDefinitionFileType(extension);
    }
}
