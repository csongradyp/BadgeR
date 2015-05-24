package net.csongradyp.badger.parser.ini;


import javax.inject.Named;
import net.csongradyp.badger.exception.MalformedAchievementRelationDefinition;
import org.springframework.util.StringUtils;

@Named
public class RelationValidator {

    private static final String SYNTACTICS_REGEX = "^(?:(?![&|]).)(\\(*\\w*[&|]+\\w*\\)*)*(?:(?![&|]).)$";

    void validate(final String relation) {
        String normalizedRelation = relation.toLowerCase().replaceAll("\\s", "");
        ValidateSyntactics(normalizedRelation);
        validateSemantics(normalizedRelation);
    }

    private void ValidateSyntactics(final String normalizedRelation) {
        if (!normalizedRelation.matches(SYNTACTICS_REGEX)) {
            throw new MalformedAchievementRelationDefinition("Relation contains unwanted characters.");
        }
        validateBracketCount(normalizedRelation);
    }

    private void validateSemantics(final String normalizedRelation) {
        final String sequenceString = normalizedRelation.toLowerCase()
                                                        .replaceAll("\\s", "")
                                                        .replaceAll( "(date|timerange|time|score|single)", "")
                                                        .replaceAll("[&|()]", "");
        if(!sequenceString.isEmpty()) {
            throw new MalformedAchievementRelationDefinition("Relation contains unwanted characters.");
        }
    }

    private void validateBracketCount(final String normalizedRelation) {
        final int openBracketNumber = StringUtils.countOccurrencesOf(normalizedRelation, "(");
        final int closeBracketNumber = StringUtils.countOccurrencesOf(normalizedRelation, ")");
        if (openBracketNumber > closeBracketNumber) {
            throw new MalformedAchievementRelationDefinition("Missing close bracket");
        } else if (openBracketNumber < closeBracketNumber) {
            throw new MalformedAchievementRelationDefinition("Missing open bracket");
        }
    }
}
