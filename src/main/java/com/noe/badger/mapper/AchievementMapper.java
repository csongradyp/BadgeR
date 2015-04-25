package com.noe.badger.mapper;

import com.noe.badger.bundle.AchievementBundle;
import com.noe.badger.bundle.domain.IAchievement;
import com.noe.badger.entity.AchievementEntity;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class AchievementMapper extends Mapper<AchievementEntity, IAchievement> {

    @Inject
    private AchievementBundle achievementBundle;

    @Override
    public IAchievement map(final AchievementEntity achievementEntity) {
        final IAchievement achievement = achievementBundle.get(achievementEntity.getId());
        return achievement;
    }
}
