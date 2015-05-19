package net.csongradyp.badger.parser.excpetion;

public class UnsupportedAchievementDefinitionFileType extends RuntimeException {

    public UnsupportedAchievementDefinitionFileType(final String invalidExtension) {
        super("File type is not supported: " + invalidExtension);
    }
}
