package com.noe.badger.event.handler;

import com.noe.badger.event.domain.CheckEvent;
import net.engio.mbassy.listener.Handler;

public interface IAchievementUpdateHandler {

    @Handler(rejectSubtypes = true)
    void onUpdate(CheckEvent event);
}
