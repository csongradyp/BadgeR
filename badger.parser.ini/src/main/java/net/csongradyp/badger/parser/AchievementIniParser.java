package net.csongradyp.badger.parser;

import net.csongradyp.badger.AchievementBundle;
import net.csongradyp.badger.AchievementDefinition;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.IAchievementBean;
import net.csongradyp.badger.domain.IRelationalAchievement;
import net.csongradyp.badger.domain.achievement.*;
import net.csongradyp.badger.domain.achievement.relation.Relation;
import net.csongradyp.badger.exception.AchievementNotFoundException;
import net.csongradyp.badger.exception.MalformedAchievementDefinition;
import net.csongradyp.badger.parser.ini.RelationParser;
import org.ini4j.Ini;
import org.ini4j.Profile;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Named
public class AchievementIniParser implements IAchievementDefinitionFileParser {

    @Inject
    private RelationParser relationParser;

    private Ini ini;

    @Override
    public AchievementDefinition parse(final File achievementFile) {
        try {
            ini = new Ini(achievementFile);
            ini.getConfig().setMultiOption(true);
            validateEventDefinition();
        } catch (IOException e) {
            throw new MalformedAchievementDefinition("Achievement ini file error!", e);
        }
        return createDefinitions();
    }

    @Override
    public AchievementDefinition parse(final String achievementFileLocation) {
        try {
            ini = new Ini(new File(achievementFileLocation));
            ini.getConfig().setMultiOption(true);
            validateEventDefinition();
        } catch (IOException e) {
            throw new MalformedAchievementDefinition("Achievement ini file error!", e);
        }
        return createDefinitions();
    }

    @Override
    public AchievementDefinition parse(final URL achievementFile) {
        try {
            ini = new Ini(achievementFile);
            ini.getConfig().setMultiOption(true);
            validateEventDefinition();
        } catch (IOException e) {
            throw new MalformedAchievementDefinition("Achievement ini file error!", e);
        }
        return createDefinitions();
    }

    @Override
    public AchievementDefinition parse(final InputStream inputStream) {
        try {
            ini = new Ini(inputStream);
            ini.getConfig().setMultiOption(true);
            validateEventDefinition();
        } catch (IOException e) {
            throw new MalformedAchievementDefinition("Achievement ini file error!", e);
        }
        return createDefinitions();
    }

    private AchievementDefinition createDefinitions() {
        final AchievementDefinition achievementBundle = new AchievementBundle();
        achievementBundle.setEvents(parseEvents());
        final Collection<IAchievement> achievements = parseAchievements();
        achievementBundle.setRelations(parseRelations(achievements));
        achievementBundle.setAchievements(achievements);
        return achievementBundle;
    }

    private void validateEventDefinition() {
        final Profile.Section eventSection = ini.get("events");
        if (eventSection == null || eventSection.childrenNames() == null) {
            throw new MalformedAchievementDefinition("[events] declaration is missing in achievement descriptor ini file!");
        }
    }

    private String[] parseEvents() {
        return ini.get("events").getAll("event", String[].class);
    }

    public Collection<IRelationalAchievement> parseRelations(Collection<IAchievement> achievements) {
        final Collection<IRelationalAchievement> compositeAchievementBeans = new ArrayList<>();
        final Profile.Section relationSection = ini.get("relations");
        if (relationSection != null) {
            for (Map.Entry<String, String> relationEntry : relationSection.entrySet()) {
                final String id = relationEntry.getKey();
                final String relationDefinition = relationEntry.getValue();
                final Relation relation = relationParser.parse(id, relationDefinition, achievements);
                final CompositeAchievementBean bean = new CompositeAchievementBean(id, relation);
                compositeAchievementBeans.add(bean);
            }
        }
        return compositeAchievementBeans;
    }

    @Override
    public IAchievementBean parse(final AchievementType type, final String achievementId) {
        final Profile.Section typeSection = ini.get(type.getType());
        if (exists(typeSection)) {
            return parse(achievementId, type, typeSection.getChild(achievementId));
        }
        throw new AchievementNotFoundException(type, achievementId);
    }

    private Collection<IAchievement> parseAchievements() {
        final Collection<IAchievement> achievements = new ArrayList<>();
        final AchievementType[] types = AchievementType.values();
        for (AchievementType type : types) {
            final Profile.Section typeSection = ini.get(type.getType());
            if (exists(typeSection)) {
                final String[] AchievementIds = typeSection.childrenNames();
                for (String achievementId : AchievementIds) {
                    IAchievement achievementBean = parse(achievementId, type, typeSection.getChild(achievementId));
                    achievements.add(achievementBean);
                }
            }
        }
        return achievements;
    }

    private IAchievementBean parse(final String id, final AchievementType type, final Profile.Section section) {
        if (section != null) {
            IAchievementBean achievementBean = null;
            switch (type) {
                case DATE:
                    achievementBean = parseSection(id, section, new DateAchievementBean());
                    break;
                case TIME:
                    achievementBean = parseSection(id, section, new TimeAchievementBean());
                    break;
                case TIME_RANGE:
                    achievementBean = parseSection(id, section, new TimeRangeAchievementBean());
                    break;
                case COUNTER:
                    achievementBean = parseSection(id, section, new CounterAchievementBean());
                    break;
                case SINGLE:
                    achievementBean = parseSection(id, section, new SingleAchievementBean());
                    break;
            }
            return achievementBean;
        }
        throw new AchievementNotFoundException(type, id);
    }

    private IAchievementBean parseSection(final String id, final Profile.Section section, final IAchievementBean achievement) {
        section.to(achievement);
        achievement.setId(id);
        final String[] triggers = section.getAll("trigger", String[].class);
        achievement.setTrigger(triggers);
        final String[] events = section.getAll("event", String[].class);
        achievement.setEvent(events);
        return achievement;
    }

    private boolean exists(Profile.Section section) {
        return section != null && section.childrenNames() != null;
    }
}
