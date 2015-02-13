package com.noe.badger.event;

import com.noe.badger.event.domain.Achievement;

public interface AchievementHandler {

    void onUnlocked(Achievement achievement);
}
