package net.csongradyp.badger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.event.EventBus;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.event.message.ScoreUpdatedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.persistence.AchievementDao;
import net.csongradyp.badger.persistence.EventDao;
import net.csongradyp.badger.persistence.entity.AchievementEntity;
import net.csongradyp.badger.provider.unlock.AchievementUnlockProviderFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class AchievementController implements IAchievementController {

    private static final Logger LOG = LoggerFactory.getLogger(AchievementController.class);

    @Inject
    private EventDao eventDao;
    @Inject
    private AchievementDao achievementDao;
    @Inject
    private UnlockedEventFactory unlockedEventFactory;
    @Inject
    private AchievementUnlockProviderFacade achievementUnlockFinder;
    private ResourceBundle resourceBundle;

    private AchievementDefinition achievementDefinition;
    private String internationalizationBaseName;

    public AchievementController() {
        achievementDefinition = new AchievementBundle();
        EventBus.setController(this);
    }

    @Override
    public void setAchievementDefinition(final AchievementDefinition achievementDefinition) {
        this.achievementDefinition = achievementDefinition;
        achievementUnlockFinder.setAchievementDefinition(achievementDefinition);
    }

    @Override
    public void setInternationalizationBaseName(final String internationalizationBaseName) {
        resourceBundle = ResourceBundle.getBundle(internationalizationBaseName, Locale.ENGLISH);
        this.internationalizationBaseName = internationalizationBaseName;
    }

    @Override
    public void setResourceBundle(final ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        unlockedEventFactory.setResourceBundle(resourceBundle);
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    @Override
    public void setLocale(final Locale locale) {
        resourceBundle = ResourceBundle.getBundle(internationalizationBaseName, locale);
        unlockedEventFactory.setResourceBundle(resourceBundle);
    }

    @Override
    public Collection<IAchievement> getAll() {
        return achievementDefinition.getAll();
    }

    @Override
    public Collection<IAchievement> getAllUnlocked() {
        final Collection<IAchievement> unlocked = new ArrayList<>();
        final Collection<AchievementEntity> unlockedEntities = achievementDao.getAll();
        unlockedEntities.parallelStream().forEach(entity -> {
            final Optional<IAchievement> achievement = achievementDefinition.get(entity.getId());
            if (achievement.isPresent()) {
                unlocked.add(achievement.get());
            }
        });
        return unlocked;
    }

    @Override
    public Collection<IAchievement> getAllByOwner(final String owner) {
        final Collection<IAchievement> achievementsByOwner = new ArrayList<>();
        final Collection<AchievementEntity> achievementEntities = achievementDao.getAllByOwner(owner);
        for (AchievementEntity achievementEntity : achievementEntities) {
            final Optional<IAchievement> achievement = achievementDefinition.get(achievementEntity.getId());
            if (achievement.isPresent()) {
                achievementsByOwner.add(achievement.get());
            }
        }
        return achievementsByOwner;
    }

    @Override
    public Optional<IAchievement> get(final String id) {
        return achievementDefinition.get(id);
    }

    @Override
    public Map<String, Set<IAchievement>> getAllByEvents() {
        return achievementDefinition.getAllByEvents();
    }

    @Override
    public void checkAndUnlock() {
        final Collection<IAchievementUnlockedEvent> unlockableAchievements = achievementUnlockFinder.findAll();
        unlockableAchievements.forEach(this::unlock);
    }

    @Override
    public void triggerEventWithHighScore(final String event, final Long score) {
        if (isNewHighScore(event, score)) {
            LOG.debug("New highscore submitted!");
            triggerEvent(event, score);
        }
    }

    private boolean isNewHighScore(final String event, final Long score) {
        return eventDao.scoreOf(event) <= score;
    }

    @Override
    public void triggerEvent(final String event, final Long score) {
        if (isDifferentValueAsStored(event, score)) {
            LOG.debug("Achievement event triggered: {} with score {} ", event, score);
            publishUpdatedScore(event, score);
            final Collection<IAchievementUnlockedEvent> unlockables = achievementUnlockFinder.findUnlockables(event, score);
            unlockables.forEach(this::unlock);
        }
    }

    private void publishUpdatedScore(final String event, final Long score) {
        final Long currentValue = eventDao.setScore(event, score);
        final ScoreUpdatedEvent updatedEvent = new ScoreUpdatedEvent(event, currentValue);
        EventBus.publishScoreChanged(updatedEvent);
    }

    private boolean isDifferentValueAsStored(String event, Long score) {
        final Long currentValue = eventDao.scoreOf(event);
        return currentValue != score;
    }

    @Override
    public void triggerEvent(final String event, final Collection<String> owners) {
        LOG.debug("Achievement event triggered: {} with owners {} ", event, owners);
        publishIncremented(event);
        final Collection<IAchievementUnlockedEvent> unlockables = achievementUnlockFinder.findUnlockables(event, owners);
        unlockables.forEach(this::unlock);
    }

    @Override
    public void triggerEvent(final String event) {
        LOG.info("Achievement event triggered: {}", event);
        publishIncremented(event);
        final Collection<IAchievementUnlockedEvent> unlockables = achievementUnlockFinder.findUnlockables(event);
        unlockables.forEach(this::unlock);
    }

    private Long publishIncremented(final String event) {
        final Long currentValue = eventDao.increment(event);
        EventBus.publishScoreChanged(new ScoreUpdatedEvent(event, currentValue));
        return currentValue;
    }

    @Override
    public void unlock(final String achievementId, final String triggerValue, final Collection<String> owners) {
        final Optional<IAchievement> matchingAchievement = achievementDefinition.get(achievementId);
        if (matchingAchievement.isPresent()) {
            final AchievementUnlockedEvent achievementUnlockedEvent = unlockedEventFactory.createEvent(matchingAchievement.get(), triggerValue, owners);
            unlock(achievementUnlockedEvent);
        }
    }

    @Override
    public void unlock(final String achievementId, final String triggerValue) {
        final Optional<IAchievement> matchingAchievement = achievementDefinition.get(achievementId);
        if (matchingAchievement.isPresent()) {
            final AchievementUnlockedEvent achievementUnlockedEvent = unlockedEventFactory.createEvent(matchingAchievement.get(), triggerValue);
            unlock(achievementUnlockedEvent);
        }
    }

    private void unlock(final IAchievementUnlockedEvent achievement) {
        if (!isLevelUnlocked(achievement.getId(), achievement.getLevel())) {
            achievementDao.unlock(achievement.getId(), achievement.getLevel(), achievement.getOwners());
            EventBus.publishUnlocked(achievement);
        }
    }

    private Boolean isLevelUnlocked(final String id, final Integer level) {
        return achievementDao.isUnlocked(id, level);
    }

    @Override
    public Boolean isUnlocked(final String achievementId) {
        return achievementDao.isUnlocked(achievementId);
    }

    @Override
    public Boolean isUnlocked(final String achievementId, final Integer level) {
        return achievementDao.isUnlocked(achievementId, level);
    }

    @Override
    public Long getCurrentScore(final String id) {
        return eventDao.scoreOf(id);
    }

    @Override
    public void reset() {
        eventDao.deleteAll();
        achievementDao.deleteAll();
    }

    void setEventDao(final EventDao eventDao) {
        this.eventDao = eventDao;
    }

    void setAchievementDao(final AchievementDao achievementDao) {
        this.achievementDao = achievementDao;
    }

    void setAchievementUnlockFinder(AchievementUnlockProviderFacade achievementUnlockFinder) {
        this.achievementUnlockFinder = achievementUnlockFinder;
    }

    void setUnlockedEventFactory(UnlockedEventFactory unlockedEventFactory) {
        this.unlockedEventFactory = unlockedEventFactory;
    }

}
