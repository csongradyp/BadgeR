Narrative:
In order to handle achievement unlock and level up events
As an achievement based system
I want to be notified about unlock and level up events

Lifecycle:
Before:
Given there is subscription for achievement unlocked events

Scenario: Related event triggered so that date based achievement is unlocked

Given an achievement with <id> id and date type bounded to <event> event with trigger <date>
And the achievement with <id> is not unlocked
And the current date is <date>
When event named <event> is triggered
Then achievement unlocked event is received
And achievement id is <id>
And the level of the unlocked achievement is 1

Examples:
|      id      | event | date|
|date-test-05-05|sample |05-05|
|date-test-12-30|sample3|12-30|

Scenario: Related event triggered so that date based achievement is unlocked

Given an achievement with <id> id and date type bounded to <event> event with trigger <date>
And the achievement with <id> is not unlocked
And the current date is <date>
When event named <event> is triggered
Then achievement unlocked event is received
And achievement id is <id>
And the level of the unlocked achievement is 1

Examples:
|      id       | event | date|
|date-test-05-05|sample |05-05|
|date-test-12-30|sample3|12-30|



