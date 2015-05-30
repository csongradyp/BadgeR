package net.csongradyp.badger.parser.json.domain;

import net.csongradyp.badger.domain.AchievementType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AchievementsJson {

    private List<AchievementJson<RangeTrigger<Long>>> scoreRange;
    private List<AchievementJson<RangeTrigger<String>>> timeRange;
    private List<CompositeAchievementJson> composite;
    private Map<AchievementType, List<? extends IAchievementJson>> achievementMap;

    public AchievementsJson() {
        achievementMap = new HashMap<>(6);
    }

    public List<? extends IAchievementJson> get(AchievementType type) {
        return achievementMap.get(type);
    }

    public void setScore(List<AchievementJson<String>> score) {
        achievementMap.put(AchievementType.SCORE, score);
    }

    public List<AchievementJson<RangeTrigger<Long>>> getScoreRange() {
        return scoreRange;
    }

    public void setScoreRange(List<AchievementJson<RangeTrigger<Long>>> scoreRange) {
        this.scoreRange = scoreRange;
        achievementMap.put(AchievementType.SCORE_RANGE, scoreRange);
    }

    public void setDate(List<AchievementJson<String>> date) {
        achievementMap.put(AchievementType.DATE, date);
    }

    public void setTime(List<AchievementJson<String>> time) {
        achievementMap.put(AchievementType.TIME, time);
    }

    public List<AchievementJson<RangeTrigger<String>>> getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(List<AchievementJson<RangeTrigger<String>>> timeRange) {
        this.timeRange = timeRange;
        achievementMap.put(AchievementType.TIME_RANGE, timeRange);
    }

    public List<CompositeAchievementJson> getComposite() {
        return composite;
    }

    public void setComposite(List<CompositeAchievementJson> composite) {
        this.composite = composite;
        achievementMap.put(AchievementType.COMPOSITE, composite);
    }

    public void setSingle(List<AchievementJson<String>> single) {
        achievementMap.put(AchievementType.SINGLE, single);
    }
}
