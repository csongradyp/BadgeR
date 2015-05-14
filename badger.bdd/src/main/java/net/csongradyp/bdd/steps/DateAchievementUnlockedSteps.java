package net.csongradyp.bdd.steps;

import net.csongradyp.bdd.Steps;
import net.csongradyp.badger.Badger;
import net.csongradyp.bdd.provider.TestDateProvider;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Steps
public class DateAchievementUnlockedSteps {

    @Inject
    private Badger badger;

    private TestDateProvider dateProvider = new TestDateProvider();

    @Given("the current date is $date")
    public void setCurrentDate(final @Named("date") String date) {
        dateProvider.stubDate(date);
        assertThat(dateProvider.currentDate(), is(equalTo(date)));
        badger.getController().setDateProvider(dateProvider);
    }

}
