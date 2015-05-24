Narrative:
In order to handle time triggered achievement unlock and level up events
As an achievement based system
I want to be notified about unlock and level up events
So that I can visualize unlocked achievement or use for further processes

Lifecycle:
Before:
Given there is subscription for achievement unlocked events

Scenario: Related event triggered so that time based achievement is unlocked

Given an achievement with <id> id and time type bounded to <event> event with trigger <time>
And the achievement with <id> is not unlocked
And current time is <time>
When event named <event> is triggered
Then unlocked event received for achievement <id>
And the level of the unlocked achievement is 1

Examples:
|      id      |  event  | time|
|time-test-0815|timeEvent|08:15|
|time-test-2357|timeEvent|23:57|

Scenario: Related event triggered so that date based achievement is unlocked

Given an achievement with <id> id and time type bounded to <event> event with trigger <time>
And the achievement with <id> id is already unlocked with level 1
And current time is <time>
When event named <event> is triggered
Then no achievement unlocked event received related to <id>

Examples:
|      id      |  event  | time|
|time-test-0815|timeEvent|08:15|
|time-test-2357|timeEvent|23:57|



