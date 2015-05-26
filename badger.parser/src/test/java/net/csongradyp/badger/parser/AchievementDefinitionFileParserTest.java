package net.csongradyp.badger.parser;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import net.csongradyp.badger.AchievementDefinition;
import net.csongradyp.badger.parser.excpetion.UnsupportedAchievementDefinitionFileType;
import net.csongradyp.badger.parser.file.FileType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AchievementDefinitionFileParserTest {

    @Mock
    private IAchievementDefinitionFileParser mockFileParser;
    @Mock
    private AchievementDefinition mockAchievementDefinition;

    private AchievementDefinitionFileParser underTest;

    @Before
    public void setUp() throws Exception {
        final Map<FileType, IAchievementDefinitionFileParser> definitionFileParsers = givenFileParsers();
        underTest = new AchievementDefinitionFileParser();
        underTest.setDefinitionFileParsers(definitionFileParsers);
    }

    private Map<FileType, IAchievementDefinitionFileParser> givenFileParsers() {
        final Map<FileType, IAchievementDefinitionFileParser> definitionFileParsers = new HashMap<>();
        definitionFileParsers.put(FileType.INI, mockFileParser);
        return definitionFileParsers;
    }

    @Test
    public void testParseReturnsDefinitionFileParsedByUnderlyingParsersWhenInputFilePathIsGivenAsAString() throws Exception {
        given(mockFileParser.parse(Matchers.any(File.class))).willReturn(mockAchievementDefinition);
        AchievementDefinition result = underTest.parse("definitionfile.ini");
        assertThat(result, is(mockAchievementDefinition));
    }

    @Test
    public void testParseReturnsDefinitionFileParsedByUnderlyingParsersWhenInputFilePathIsGivenAsAnURL() throws Exception {
        given(mockFileParser.parse(Matchers.any(File.class))).willReturn(mockAchievementDefinition);
        AchievementDefinition result = underTest.parse(new URL("http", "localhost", "definitionfile.ini"));
        assertThat(result, is(mockAchievementDefinition));
    }

    @Test
    public void testParseReturnsDefinitionFileParsedByUnderlyingParsersWhenInputFileIsGivenDirectly() throws Exception {
        given(mockFileParser.parse(Matchers.any(File.class))).willReturn(mockAchievementDefinition);
        AchievementDefinition result = underTest.parse(new File("definitionfile.ini"));
        assertThat(result, is(mockAchievementDefinition));
    }

    @Test(expected = UnsupportedAchievementDefinitionFileType.class)
    public void testParseThrowsExceptionWhenGivenFileTypeIsNotsupported() throws Exception {
        underTest.parse("definitionfile.txt");
    }
}
