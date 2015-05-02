package com.noe.badger.bundle.parser;

import com.noe.badger.AchievementType;
import com.noe.badger.bundle.AchievementBundle;
import com.noe.badger.bundle.domain.IAchievement;
import com.noe.badger.bundle.domain.IAchievementBean;
import com.noe.badger.bundle.domain.achievement.*;
import com.noe.badger.bundle.relation.Relation;
import com.noe.badger.bundle.relation.RelationParser;
import com.noe.badger.exception.AchievementNotFoundException;
import com.noe.badger.exception.MalformedAchievementDefinition;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import org.ini4j.Ini;
import org.ini4j.Profile;

@Named
public class AchievementIniParser implements IAchievementDefinitionFileParser {

    @Inject
    private RelationParser relationParser;

    private Ini ini;

    @Override
    public AchievementBundle parse(final File achievementFile) {
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
    public AchievementBundle parse(final String achievementFileLocation) {
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
    public AchievementBundle parse(final URL achievementFile) {
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
    public AchievementBundle parse(final InputStream inputStream) {
        try {
            ini = new Ini(inputStream);
            ini.getConfig().setMultiOption(true);
            validateEventDefinition();
        } catch (IOException e) {
            throw new MalformedAchievementDefinition("Achievement ini file error!", e);
        }
        return createDefinitions();
    }

    private AchievementBundle createDefinitions() {
        final AchievementBundle achievementBundle = new AchievementBundle();
        achievementBundle.setEvents(parseEvents());
        achievementBundle.setRelations(parseRelations());
        achievementBundle.setAchievements(parseAchievements());
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

    public Collection<CompositeAchievementBean> parseRelations() {
        final Collection<CompositeAchievementBean> compositeAchievementBeans = new ArrayList<>();
        final Profile.Section relationSection = ini.get("relations");
        if (relationSection != null) {
            for (Map.Entry<String, String> relationEntry : relationSection.entrySet()) {
                final String id = relationEntry.getKey();
                final String relationDefinition = relationEntry.getValue();
                final Relation relation = relationParser.parse(id, relationDefinition);
                final CompositeAchievementBean bean = new CompositeAchievementBean(id, relation);
                compositeAchievementBeans.add(bean);
            }
        }
        return compositeAchievementBeans;
    }

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
                    achievementBean = parseSection(id, section, new CounterAchievementBean());
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
