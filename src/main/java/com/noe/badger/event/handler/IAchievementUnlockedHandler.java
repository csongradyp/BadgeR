package com.noe.badger.event.handler;

import com.noe.badger.event.domain.Achievement;

public interface IAchievementUnlockedHandler {

    void onUnlocked(Achievement achievement);
}
