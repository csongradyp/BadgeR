package net.csongradyp.badger.parser.json;

import net.csongradyp.badger.AchievementBundle;
import net.csongradyp.badger.AchievementDefinition;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.IAchievementBean;
import net.csongradyp.badger.domain.ITriggerableAchievementBean;
import net.csongradyp.badger.domain.achievement.CompositeAchievementBean;
import net.csongradyp.badger.domain.achievement.ScoreRangeAchievementBean;
import net.csongradyp.badger.domain.achievement.SingleAchievementBean;
import net.csongradyp.badger.domain.achievement.TimeRangeAchievementBean;
import net.csongradyp.badger.domain.achievement.relation.Relation;
import net.csongradyp.badger.domain.achievement.trigger.ITrigger;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTriggerPair;
import net.csongradyp.badger.domain.achievement.trigger.TimeTriggerPair;
import net.csongradyp.badger.exception.MalformedAchievementDefinition;
import net.csongradyp.badger.parser.IAchievementDefinitionFileParser;
import net.csongradyp.badger.parser.api.AchievementFactory;
import net.csongradyp.badger.parser.api.RelationParser;
import net.csongradyp.badger.parser.api.trigger.ITriggerParser;
import net.csongradyp.badger.parser.json.domain.*;
import net.csongradyp.badger.provider.date.DateProvider;
import org.codehaus.jackson.map.ObjectMapper;

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
import java.util.stream.Collectors;

@Named
public class AchievementJsonParser implements IAchievementDefinitionFileParser {

    public static final String FILE_ERROR = "Achievement JSon file read error.";
    @Inject
    private DateProvider dateProvider;
    @Resource(name = "jsonTriggerParsers")
    private Map<AchievementType, ITriggerParser> jsonTriggerParsers;
    private final ObjectMapper mapper;
    @Inject
    private RelationParser relationParser;

    public AchievementJsonParser() {
        mapper = new ObjectMapper();
    }

    @Override
    public AchievementDefinition parse(final File achievementFile) {
        final AchievementDefinition achievementDefinition = new AchievementBundle();
        try {
            final AchievementDefinitionJson achievementDefinitionJson = mapper.readValue(achievementFile, AchievementDefinitionJson.class);
            achievementDefinition.setEvents(achievementDefinitionJson.getEvents());
            final AchievementsJson achievements = achievementDefinitionJson.getAchievements();
            achievementDefinition.setAchievements(parseAchievements(achievements));
        } catch (IOException e) {
            throw new MalformedAchievementDefinition(FILE_ERROR, e);
        }
        return achievementDefinition;
    }

    @Override
    public AchievementDefinition parse(final String achievementFileLocation) {
        final AchievementDefinition achievementDefinition = new AchievementBundle();
        try {
            final AchievementDefinitionJson achievementDefinitionJson = mapper.readValue(new File(achievementFileLocation), AchievementDefinitionJson.class);
            achievementDefinition.setEvents(achievementDefinitionJson.getEvents());
            final AchievementsJson achievements = achievementDefinitionJson.getAchievements();
            achievementDefinition.setAchievements(parseAchievements(achievements));
        } catch (IOException e) {
            throw new MalformedAchievementDefinition(FILE_ERROR, e);
        }
        return achievementDefinition;
    }

    @Override
    public AchievementDefinition parse(final URL achievementFile) {
        final AchievementDefinition achievementDefinition = new AchievementBundle();
        try {
            final AchievementDefinitionJson achievementDefinitionJson = mapper.readValue(achievementFile, AchievementDefinitionJson.class);
            achievementDefinition.setEvents(achievementDefinitionJson.getEvents());
            final AchievementsJson achievements = achievementDefinitionJson.getAchievements();
            achievementDefinition.setAchievements(parseAchievements(achievements));
        } catch (IOException e) {
            throw new MalformedAchievementDefinition(FILE_ERROR, e);
        }
        return achievementDefinition;
    }

    private Collection<IAchievement> parseAchievements(final AchievementsJson achievements) {
        final Collection<IAchievement> achievementBeans = new ArrayList<>();
        for (AchievementType type : AchievementType.values()) {
            if (type == AchievementType.SCORE || type == AchievementType.TIME || type == AchievementType.DATE) {
                final List<IAchievementBean> beans = mapTriggerAchievements(type, (List<? extends ISimpleTriggerAchievementJson>) achievements.get(type));
                achievementBeans.addAll(beans);
            } else if (type == AchievementType.SINGLE) {
                achievementBeans.addAll(mapSingleAchievementBeans(achievements.get(type)));
            }
        }
        if (achievements.getScoreRange() != null) {
            achievementBeans.addAll(mapScoreRangeAchievements(achievements.getScoreRange()));
        }
        if (achievements.getTimeRange() != null) {
            achievementBeans.addAll(mapTimeRangeAchievements(achievements.getTimeRange()));
        }
        if (achievements.getComposite() != null) {
            achievementBeans.addAll(mapCompositeAchievementBeans(achievements.getComposite()));
        }
        return achievementBeans;
    }

    private List<CompositeAchievementBean> mapCompositeAchievementBeans(final List<CompositeAchievementJson> achievements) {
        return achievements.stream().map(json -> {
            final CompositeAchievementBean bean = new CompositeAchievementBean();
            mapBasicAttributes(json, bean);

            final List<ITrigger> triggers = new ArrayList<>();
            if(json.getScoreTrigger() != null) {
                final ITriggerParser<ITrigger> triggerParser = jsonTriggerParsers.get(AchievementType.SCORE);
                triggers.addAll(triggerParser.parse(json.getScoreTrigger()));
            }
            if(json.getDateTrigger() != null) {
                final ITriggerParser<ITrigger> triggerParser = jsonTriggerParsers.get(AchievementType.DATE);
                triggers.addAll(triggerParser.parse(json.getDateTrigger()));
            }
            if(json.getTimeTrigger() != null) {
                final ITriggerParser<ITrigger> triggerParser = jsonTriggerParsers.get(AchievementType.TIME);
                triggers.addAll(triggerParser.parse(json.getTimeTrigger()));
            }
            if(json.getScoreRangeTrigger() != null) {
                triggers.addAll(getScoreTriggerPairs(json.getScoreRangeTrigger()));
            }

            if(json.getTimeRangeTrigger() != null) {
                triggers.addAll(getTimeTriggerPairs(json.getTimeRangeTrigger()));
            }

            if(json.getRelation() == null) {
                throw new MalformedAchievementDefinition("Missing relation definition for: " + json.getId());
            }
            bean.setTrigger(triggers);
            final Relation relation = relationParser.parse(json.getRelation(), triggers);
            bean.setRelation(relation);

            return bean;
        }).collect(Collectors.toList());
    }

    private List<SingleAchievementBean> mapSingleAchievementBeans(final List<? extends IAchievementJson> achievements) {
        return achievements.stream().map(json -> {
            final SingleAchievementBean bean = new SingleAchievementBean();
            mapBasicAttributes(json, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    private List<IAchievementBean> mapTriggerAchievements(final AchievementType type, final List<? extends ISimpleTriggerAchievementJson> achievementJsons) {
        return achievementJsons.parallelStream().map(json -> {
            final ITriggerableAchievementBean bean = (ITriggerableAchievementBean) AchievementFactory.create(type);
            mapBasicAttributes(json, bean);
            final ITriggerParser<ITrigger> triggerParser = jsonTriggerParsers.get(type);
            bean.setTrigger(triggerParser.parse(json.getTrigger()));
            return bean;
        }).collect(Collectors.toList());
    }

    private Collection<IAchievement> mapScoreRangeAchievements(final List<AchievementJson<RangeTrigger<Long>>> scoreRange) {
        return scoreRange.parallelStream().map(json -> {
            final ScoreRangeAchievementBean bean = new ScoreRangeAchievementBean();
            mapBasicAttributes(json, bean);
            final List<ScoreTriggerPair> triggers = getScoreTriggerPairs(json.getTrigger());
            bean.setTrigger(triggers);
            return bean;
        }).collect(Collectors.toList());
    }

    private List<ScoreTriggerPair> getScoreTriggerPairs(List<RangeTrigger<Long>> trigger) {
        return trigger.stream().map(t -> new ScoreTriggerPair(t.getStart(), t.getEnd())).collect(Collectors.toList());
    }

    private Collection<IAchievement> mapTimeRangeAchievements(final List<AchievementJson<RangeTrigger<String>>> timeRange) {
        return timeRange.parallelStream().map(json -> {
            final TimeRangeAchievementBean bean = new TimeRangeAchievementBean();
            mapBasicAttributes(json, bean);
            final List<TimeTriggerPair> triggers = getTimeTriggerPairs(json.getTrigger());
            bean.setTrigger(triggers);
            return bean;
        }).collect(Collectors.toList());
    }

    private List<TimeTriggerPair> getTimeTriggerPairs(List<RangeTrigger<String>> trigger) {
        return trigger.stream().map(t -> new TimeTriggerPair(dateProvider.parseTime(t.getStart()), dateProvider.parseTime(t.getEnd()))).collect(Collectors.toList());
    }

    private void mapBasicAttributes(final IAchievementJson json, final IAchievementBean bean) {
        bean.setId(json.getId());
        bean.setCategory(json.getCategory());
        bean.setSubscription(json.getSubscription());
    }

    void setJsonTriggerParsers(final Map<AchievementType, ITriggerParser> jsonTriggerParsers) {
        this.jsonTriggerParsers = jsonTriggerParsers;
    }

    void setDateProvider(final DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    void setRelationParser(RelationParser relationParser) {
        this.relationParser = relationParser;
    }
}
