package net.csongradyp.bdd.steps;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.annotation.Resource;
import javax.inject.Inject;
import net.csongradyp.badger.AchievementController;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.bdd.Steps;
import net.csongradyp.bdd.provider.TriggerChecker;
import org.apache.commons.lang.StringEscapeUtils;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Steps
public class InternationalizationSteps {

    @Inject
    private AchievementController controller;
    private TriggerChecker triggerChecker;
    @Resource
    private List<IAchievementUnlockedEvent> unlockEventList;

    private IAchievementUnlockedEvent receivedEvent;

    @BeforeScenario
    public void beforeI18n() {
        controller.reset();
    }

    @AfterScenario
    public void afterI18n() {
        controller.setResourceBundle(null);
    }

    @Given("internationalization message files for BadgeR with locale $locale")
    public void givenInternationalizationFiles(final String locale) {
        controller.setInternationalizationBaseName("msg");
        controller.setLocale(Locale.forLanguageTag(locale));
    }

    @Given("there is no internationalization message files for BadgeR")
    public void givenNoInternationalizationFiles() {
        controller.setResourceBundle(null);
    }

    @Then("the title of the achievement is localized as $title")
    public void chechTitle(final String title) {
        final Optional<IAchievementUnlockedEvent> relatedEvent = unlockEventList.stream().filter(event -> event.getTitle().equals(title)).findAny();
        assertThat(relatedEvent.isPresent(), is(true));
        receivedEvent = relatedEvent.get();
        assertThat(receivedEvent.getTitle(), is(equalTo(title)));
    }

    @Then("the title of the achievement is the message property key $titleKey")
    public void chechTitleKey(final String titleKey) {
        final Optional<IAchievementUnlockedEvent> relatedEvent = unlockEventList.stream().filter(event -> event.getTitle().equals(titleKey)).findAny();
        assertThat(relatedEvent.isPresent(), is(true));
        receivedEvent = relatedEvent.get();
        assertThat(receivedEvent.getTitle(), is(equalTo(titleKey)));
    }

    @Then("the desciption of the achievement is localized as $description")
    public void checkDescription(final String description) {
        final String i18nDescription = StringEscapeUtils.unescapeJava(description);
        assertThat(receivedEvent.getText(), is(equalTo(i18nDescription)));
    }

    @Then("the desciption of the achievement is the message property key $descriptionKey")
    public void checkDescriptionKey(final String descriptionKey) {
        assertThat(receivedEvent.getText(), is(equalTo(descriptionKey)));
    }
}
