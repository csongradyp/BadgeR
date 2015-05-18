package net.csongradyp.badger.event.handler.wrapper;

import net.csongradyp.badger.event.handler.IAchievementUnlockedHandler;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;

/**
 * Wrapper implementation for proper registration of handlers to {@link net.engio.mbassy.bus.MBassador} event bus.
 */
@Listener(references = References.Strong)
public class AchievementUnlockedHandlerWrapper implements IAchievementUnlockedHandler {

    private final IAchievementUnlockedHandler wrapped;

    public AchievementUnlockedHandlerWrapper(final IAchievementUnlockedHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    @Handler(rejectSubtypes = true)
    public void onUnlocked(final AchievementUnlockedEvent achievementUnlockedEvent) {
        wrapped.onUnlocked(achievementUnlockedEvent);
    }

    /**
     * Returns the originally registered handler which was wrapped.
     *
     * @return originally registered handler.
     */
    public IAchievementUnlockedHandler getWrapped() {
        return wrapped;
    }
}
