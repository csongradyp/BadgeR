package com.noe.badger.event.handler;

import com.noe.badger.event.message.Score;

public interface IScoreUpdateHandler {

    void onUpdate(Score score);
}
