package com.noe.badger.event.handler;

import com.noe.badger.event.message.Achievement;

public interface IAchievementUnlockedHandler {

    void onUnlocked(Achievement achievement);
}
