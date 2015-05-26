package net.csongradyp.badger.factory;

import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;
import javax.inject.Named;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.IAchievementBean;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class UnlockedEventFactory {

    private static final Logger LOG = LoggerFactory.getLogger(UnlockedEventFactory.class);

    private ResourceBundle resourceBundle;

    public AchievementUnlockedEvent createEvent(final IAchievementBean achievementBean, final Integer level, final Long triggeredValue) {
        final AchievementUnlockedEvent achievementUnlockedEvent = createEvent(achievementBean, String.valueOf(triggeredValue));
        achievementUnlockedEvent.setLevel(level);
        LOG.info("Achievement created with id: {} level: {}", achievementBean.getId(), level);
        return achievementUnlockedEvent;
    }

    public AchievementUnlockedEvent createEvent(IAchievement achievementBean, String triggeredValue, String[] owners) {
        return createEvent(achievementBean, triggeredValue, Arrays.asList(owners));
    }

    public AchievementUnlockedEvent createEvent(IAchievement achievementBean, String triggeredValue, Collection<String> owners) {
        final AchievementUnlockedEvent achievementUnlockedEvent = createEvent(achievementBean, triggeredValue);
        achievementUnlockedEvent.addOwners(owners);
        return achievementUnlockedEvent;
    }

    public AchievementUnlockedEvent createEvent(final IAchievement achievementBean) {
        return createEvent(achievementBean, "");
    }

    public AchievementUnlockedEvent createEvent(final IAchievement achievementBean, final String triggeredValue) {
        final String title;
        final String text;
        if (resourceBundle != null) {
            title = resourceBundle.getString(achievementBean.getTitleKey());
            text = resourceBundle.getString(achievementBean.getTextKey());
        } else {
            title = achievementBean.getTitleKey();
            text = achievementBean.getTextKey();
        }
        LOG.info("Achievement created with id: {}", achievementBean.getId());
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(achievementBean.getId(), title, text, triggeredValue);
        unlockedEvent.setCategory(achievementBean.getCategory());
        return unlockedEvent;
    }

    public void setResourceBundle(final ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

}
