package net.csongradyp.badger.event.handler;

import net.csongradyp.badger.event.message.Score;

/**
 * Interface to handle triggered event score update events.
 */
public interface IScoreUpdateHandler {

    /**
     * Callback method to receive update information.
     *
     * @param score Updated event counter/score as a {@link Score} instance.
     */
    void onUpdate(Score score);
}
