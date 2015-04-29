package com.noe.badger.event.handler;

import com.noe.badger.event.message.Score;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;

@Listener(references = References.Strong)
public class ScoreUpdateHandlerWrapper implements IScoreUpdateHandler {

    private final IScoreUpdateHandler wrapped;

    public ScoreUpdateHandlerWrapper(final IScoreUpdateHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    @Handler(rejectSubtypes = true)
    public void onUpdate(final Score score) {
        wrapped.onUpdate(score);
    }

    public IScoreUpdateHandler getWrapped() {
        return wrapped;
    }
}
