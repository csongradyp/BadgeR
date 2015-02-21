package com.noe.badger.event;

import com.noe.badger.event.domain.CheckEvent;

public interface AchievementCheckHandler {

    void onCheck(CheckEvent event);
}
