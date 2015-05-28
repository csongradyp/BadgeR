package net.csongradyp.badger.parser.ini;

import net.csongradyp.badger.AchievementBundle;
import net.csongradyp.badger.AchievementDefinition;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.IAchievementBean;
import net.csongradyp.badger.domain.ITriggerableAchievementBean;
import net.csongradyp.badger.domain.achievement.*;
import net.csongradyp.badger.domain.achievement.relation.Relation;
import net.csongradyp.badger.domain.achievement.trigger.ITrigger;
import net.csongradyp.badger.exception.AchievementNotFoundException;
import net.csongradyp.badger.exception.MalformedAchievementDefinition;
import net.csongradyp.badger.parser.IAchievementDefinitionFileParser;
import net.csongradyp.badger.parser.ini.trigger.ITriggerParser;
import org.ini4j.Ini;
import org.ini4j.Profile;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Named
public class AchievementIniParser implements IAchievementDefinitionFileParser {

    private static final String ACHIEVEMENT_INI_FILE_ERROR = "Achievement ini file error!";
    @Inject
    private RelationParser relationParser;
    @Resource(name = "triggerParsers")
    private Map<AchievementType, ITriggerParser> triggerParsers;

    private Ini ini;

    @Override
    public AchievementDefinition parse(final File achievementFile) {
        try {
            ini = new Ini(achievementFile);
            ini.getConfig().setMultiOption(true);
            validateEventDefinition();
        } catch (IOException e) {
            throw new MalformedAchievementDefinition(ACHIEVEMENT_INI_FILE_ERROR, e);
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
            throw new MalformedAchievementDefinition(ACHIEVEMENT_INI_FILE_ERROR, e);
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
            throw new MalformedAchievementDefinition(ACHIEVEMENT_INI_FILE_ERROR, e);
        }
        return createDefinitions();
    }

    private AchievementDefinition createDefinitions() {
        final AchievementDefinition achievementBundle = new AchievementBundle();
        achievementBundle.setEvents(parseEvents());
        final Collection<IAchievement> achievements = parseAchievements();
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

    private IAchievement parse(final String id, final AchievementType type, final Profile.Section section) {
        if (section != null) {
            IAchievement achievementBean = null;
            switch (type) {
                case DATE:
                    achievementBean = parseBean(id, section, new DateAchievementBean());
                    break;
                case TIME:
                    achievementBean = parseBean(id, section, new TimeAchievementBean());
                    break;
                case TIME_RANGE:
                    achievementBean = parseBean(id, section, new TimeRangeAchievementBean());
                    break;
                case SCORE:
                    achievementBean = parseBean(id, section, new ScoreAchievementBean());
                    break;
                case SCORE_RANGE:
                    achievementBean = parseBean(id, section, new ScoreRangeAchievementBean());
                    break;
                case COMPOSITE:
                    achievementBean = parseCompositeSection(id, section, new CompositeAchievementBean());
                    break;
                case SINGLE:
                    achievementBean = parseSingle(id, section);
                    break;
            }
            return achievementBean;
        }
        throw new AchievementNotFoundException(type, id);
    }

    private SingleAchievementBean parseSingle(String id, Profile.Section section) {
        final SingleAchievementBean achievement = new SingleAchievementBean();
        achievement.setId(id);
        achievement.setCategory(section.get("category"));
        return achievement;
    }

    private CompositeAchievementBean parseCompositeSection(final String id, final Profile.Section section, final CompositeAchievementBean achievement) {
        achievement.setId(id);
        parseEvents(section, achievement);
        parseTriggers(section, achievement);
        final Relation relation = parseRelation(section, achievement.getTrigger());
        achievement.setRelation(relation);
        achievement.setCategory(section.get("category"));
        return achievement;
    }

    private void parseEvents(Profile.Section section, IAchievementBean achievement) {
        final String[] events = section.getAll("event", String[].class);
        achievement.setEvent(events);
    }

    private void parseTriggers(Profile.Section section, CompositeAchievementBean achievement) {
        AchievementType type;
        final String[] scoreTriggers = section.getAll("scoreTrigger", String[].class);
        type = AchievementType.SCORE;
        achievement.addTrigger(triggerParsers.get(type).parse(scoreTriggers));
        final String[] dateTriggers = section.getAll("dateTrigger", String[].class);
        type = AchievementType.DATE;
        achievement.addTrigger(triggerParsers.get(type).parse(dateTriggers));
        final String[] timeTriggers = section.getAll("timeTrigger", String[].class);
        type = AchievementType.TIME;
        achievement.addTrigger(triggerParsers.get(type).parse(timeTriggers));
        final String[] timeRangeTriggers = section.getAll("timeRangeTrigger", String[].class);
        type = AchievementType.TIME_RANGE;
        achievement.addTrigger(triggerParsers.get(type).parse(timeRangeTriggers));
    }

    private Relation parseRelation(Profile.Section section, List<ITrigger> triggers) {
        final String relationExpression = section.get("relation");
        return relationParser.parse(relationExpression, triggers);
    }

    private IAchievementBean parseBean(final String id, final Profile.Section section, final ITriggerableAchievementBean achievement) {
        achievement.setId(id);
        final String[] triggers = section.getAll("trigger", String[].class);
        final List<ITrigger> parsedTriggers = triggerParsers.get(achievement.getType()).parse(triggers);
        achievement.setTrigger(parsedTriggers);
        parseEvents(section, achievement);
        achievement.setCategory(section.get("category"));
        return achievement;
    }

    private boolean exists(Profile.Section section) {
        return section != null && section.childrenNames() != null;
    }

    void setTriggerParsers(final Map<AchievementType, ITriggerParser> triggerParsers) {
        this.triggerParsers = triggerParsers;
    }

    void setRelationParser(RelationParser relationParser) {
        this.relationParser = relationParser;
    }
}
