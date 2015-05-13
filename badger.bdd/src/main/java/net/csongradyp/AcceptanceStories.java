package net.csongradyp;

import java.text.SimpleDateFormat;
import java.util.List;
import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.model.ExamplesTableFactory;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.MarkUnmatchedStepsAsPending;
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.StepCollector;
import org.jbehave.core.steps.StepFinder;
import org.jbehave.core.steps.spring.SpringApplicationContextFactory;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.springframework.context.ApplicationContext;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;
import static org.jbehave.core.reporters.Format.TXT;
import static org.jbehave.core.reporters.Format.XML;

//@RunWith(SpringAnnotatedEmbedderRunner.class)
//@Configure
//@UsingEmbedder(embedder = Embedder.class, generateViewAfterStories = true, ignoreFailureInStories = false, ignoreFailureInView = false /*, stepsFactory = true*/)
//@UsingSpring(resources = "classpath:META-INF/config.xml", ignoreContextFailure = false)
//@UsingSteps
public class AcceptanceStories extends JUnitStories {

//    @Override
//    public Configuration configuration() {
//        Class<? extends Embeddable> embeddableClass = this.getClass();
//        return new MostUsefulConfiguration().useStoryLoader(new LoadFromClasspath(embeddableClass))
//                .useParameterConverters(new ParameterConverters().addConverters(customConverters()))
//                .useStoryReporterBuilder(
//                        new StoryReporterBuilder()
//                                .withCodeLocation(codeLocationFromClass(embeddableClass))
//                                .withDefaultFormats().withFormats(Format.CONSOLE, Format.TXT, Format.HTML));
//    }
//
//    private ParameterConverters.ParameterConverter[] customConverters() {
//        List<ParameterConverters.ParameterConverter> converters = new ArrayList<>();
//        converters.add(new ParameterConverters.NumberConverter(NumberFormat.getNumberInstance()));
//        converters.add(new ParameterConverters.DateConverter(new SimpleDateFormat("yyyy-MM-dd")));  // custom date pattern
////        converters.add(new ParameterConverters.ExamplesTableConverter(new ExamplesTableFactory(new LoadFromClasspath(this.getClass())))); // custom examples table loader
//        return converters.toArray(new ParameterConverters.ParameterConverter[converters.size()]);
//    }

    public AcceptanceStories() {
        configuredEmbedder().embedderControls()
                .doGenerateViewAfterStories(true)
                .doIgnoreFailureInStories(false)
                .doIgnoreFailureInView(false)
                .useThreads(1);
    }

    @Override
    public Configuration configuration() {
        Class<? extends Embeddable> embeddableClass = this.getClass();
        // Start from default ParameterConverters instance
        ParameterConverters parameterConverters = new ParameterConverters();
        // factory to allow parameter conversion and loading from external resources (used by StoryParser too)
        ExamplesTableFactory examplesTableFactory = new ExamplesTableFactory(new LocalizedKeywords(), new LoadFromClasspath(embeddableClass), parameterConverters);
        // add custom converters
        parameterConverters.addConverters(
                new ParameterConverters.DateConverter(new SimpleDateFormat("yyyy-MM-dd")),
                new ParameterConverters.ExamplesTableConverter(examplesTableFactory));
        ParameterControls parameterControls = new ParameterControls();
        parameterControls.useDelimiterNamedParameters(true);
        StepCollector stepCollector = new MarkUnmatchedStepsAsPending(new StepFinder(new StepFinder.ByLevenshteinDistance()));
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(embeddableClass))
                .usePendingStepStrategy(new FailingUponPendingStep())
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withFailureTrace(false)
                        .withCodeLocation(CodeLocations.codeLocationFromClass(embeddableClass))
                        .withDefaultFormats()
                        .withFormats(HTML, XML, CONSOLE, TXT))
                .useParameterControls(parameterControls)
                .useStepCollector(stepCollector);
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new SpringStepsFactory(configuration(), createContext());
    }

    protected ApplicationContext createContext() {
        return new SpringApplicationContextFactory("/META-INF/config.xml").createApplicationContext();
    }

    protected List<String> storyPaths() {
//        return new StoryFinder().findPaths(CodeLocations.codeLocationFromPath("../badger.bdd/src/main/java"),"stories/*.story", "");
        return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()), "**/*.story", "");
    }
}
