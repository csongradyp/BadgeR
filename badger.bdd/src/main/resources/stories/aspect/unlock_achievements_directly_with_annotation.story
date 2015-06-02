Narrative:
In order to handle achievement unlock and level up events
As an achievement based system
I want to be notified about unlock and level up events
So that I can visualize unlocked achievement or use for further processes

Lifecycle:
Before:
Given there is subscription for achievement unlocked events

Scenario: Unlock achievement manually with annotation

Given an achievement with simple id and single type
When an achievement with simple id is unlocked via annotation
Then unlocked event received for achievement simple
And the level of the unlocked achievement is 1

Scenario: Unlock achievement manually with annotation with owner

Given an achievement with simple id and single type
When an achievement with simple id is unlocked via annotation with owner Doe
Then unlocked event received for achievement simple
And the level of the unlocked achievement is 1
And the owner of the unlocked achievement is Doe

Scenario: Unlock multiple achievements manually with annotation

Given an achievement with simple id and single type
And an achievement with first id and score type
When an achievement with ids: simple, first is unlocked via annotation
Then unlocked event received for achievement simple
And the level of the unlocked achievement is 1
And unlocked event received for achievement first
And the level of the unlocked achievement is 1
