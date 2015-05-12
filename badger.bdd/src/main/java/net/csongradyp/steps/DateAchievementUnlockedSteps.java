package net.csongradyp.steps;

import javax.inject.Inject;
import net.csongrady.badger.domain.AchievementType;
import net.csongradyp.Steps;
import net.csongradyp.badger.Badger;
import net.csongradyp.data.ReceivedAchievementUnlockedEvents;
import net.csongradyp.provider.TestDateProvider;
import org.jbehave.core.annotations.AsParameterConverter;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Steps
public class DateAchievementUnlockedSteps {

    @Inject
    private Badger badger;
    @Inject
    private ReceivedAchievementUnlockedEvents receivedEvent;

    private TestDateProvider dateProvider = new TestDateProvider();

    @Given("the current date is $date")
    public void setCurrentDate(final @Named("date") String date) {
        dateProvider.setStubbedDate(dateProvider.asDate(date));
        assertThat(dateProvider.now(), is(equalTo(date)));
        badger.getController().setDateProvider(dateProvider);
    }

    @AsParameterConverter
    public AchievementType getType(String type) {
        return AchievementType.parse(type);
    }
}
