package net.csongradyp.bdd.steps.common;

import javax.inject.Inject;
import net.csongradyp.badger.AchievementController;
import net.csongradyp.badger.parser.AchievementDefinitionFileParser;
import net.csongradyp.bdd.Steps;
import org.jbehave.core.annotations.BeforeStories;

@Steps
public class BeforeAfterSteps {

    @Inject
    private AchievementController controller;
    @Inject
    private AchievementDefinitionFileParser parser;

    @BeforeStories
    public void beforeStories() {
        controller.setAchievementDefinition(parser.parse("badger.bdd/src/main/resources/test.ini"));
    }

}
