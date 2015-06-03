Narrative:
In order to unlock achievements directly from code
As an achievement based system
I want to use unlock annotation
So that I can manipulate achievements manually

Lifecycle:
Before:
Given there is subscription for achievement unlocked events

Scenario: Unlock achievement directly with annotation

Given an achievement with simple id and single type
When an achievement with simple id is unlocked via annotation
Then unlocked event received for achievement simple
And the level of the unlocked achievement is 1

Scenario: Unlock achievement directly with annotation with owner

Given an achievement with simple id and single type
When an achievement with simple id is unlocked via annotation with owner Doe
Then unlocked event received for achievement simple
And the level of the unlocked achievement is 1
And the owner of the unlocked achievement is Doe

Scenario: Unlock achievement directly with annotation with value

Given an achievement with simple id and single type
When an achievement with simple id is unlocked via annotation with trigger value myValue
Then unlocked event received for achievement simple
And the level of the unlocked achievement is 1
And the trigger value of the unlocked achievement is myValue

Scenario: Unlock multiple achievements directly with annotation

Given an achievement with simple id and single type
And an achievement with first id and score type
When an achievement with ids: simple, first is unlocked via annotation
Then unlocked event received for achievement simple
And the level of the unlocked achievement is 1
And unlocked event received for achievement first
And the level of the unlocked achievement is 1
