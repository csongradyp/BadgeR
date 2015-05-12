package net.csongradyp.steps.common;

import javax.inject.Inject;
import net.csongradyp.Steps;
import net.csongradyp.badger.Badger;
import org.jbehave.core.annotations.AfterStories;

@Steps
public class BeforeAfterSteps {

    @Inject
    private Badger badger;

    @AfterStories
    public void afterStories() {
        badger.reset();
    }

}
