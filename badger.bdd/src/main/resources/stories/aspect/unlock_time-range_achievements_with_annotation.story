Narrative:
In order to handle time triggered achievement unlock and level up events
As an achievement based system
I want to be notified about unlock and level up events
So that I can visualize unlocked achievement or use for further processes

Lifecycle:
Before:
Given there is subscription for achievement unlocked events

Scenario: Related event triggered so that time based achievement is unlocked

Given an achievement with <id> id and timerange type bounded to <event> event with start trigger <start> and end trigger <end>
And the achievement with <id> is not unlocked
And current time is <time>
When event named <event> is triggered via annotation
Then unlocked event received for achievement <id>
And the level of the unlocked achievement is 1

Examples:
|     id     |  event  |start| end | time|
|time-01-02  |timeEvent|01:30|02:00|01:59|
|time-14-15  |timeEvent|14:59|15:00|15:00|
|time-reverse|timeEvent|23:00|22:00|07:11|

Scenario: Related event triggered so that date based achievement is unlocked

Given an achievement with <id> id and timerange type bounded to <event> event with start trigger <start> and end trigger <end>
And the achievement with <id> id is unlocked
And current time is <time>
When event named <event> is triggered via annotation
Then no achievement unlocked event received related to <id>

Examples:
|    id    |  event  |start| end | time|
|time-01-02|timeEvent|01:30|02:00|01:59|
|time-14-15|timeEvent|14:59|15:00|15:00|
