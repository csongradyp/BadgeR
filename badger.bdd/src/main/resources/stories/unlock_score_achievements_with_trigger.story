Narrative:
In order to handle achievement unlock and level up events
As an achievement based system
I want to be notified about unlock and level up events
So that I can visualize unlocked achievement or use for further processes

Lifecycle:
Before:
Given there is subscription for achievement unlocked events

Scenario: Related event triggered multiple times so that score based achievement is unlocked

Given an achievement with <id> id and score type bounded to <event> event with trigger <trigger>
And the current <event> event score is <score>
When event named <event> is triggered <trigger> times
Then unlocked event received for achievement <id>
And the level of the unlocked achievement is <level>

Examples:
|  id  | event |score|trigger|level|
|first |sample |    0|      3|    1|
|second|sample2|    0|      2|    1|

Scenario: Related event is triggered multiple times so that score based unlocked achievement is leveled up

Given an achievement with <id> id and score type bounded to <event> event with trigger <trigger>
And the current <event> event score is <score>
And the achievement with <id> id is already unlocked with level <level>
When event named <event> is triggered <times> times
Then unlocked event received for achievement <id>
And the level of the unlocked achievement is <expected>

Examples:
|  id  | event |score|trigger|times|level| expected|
|first |sample |    3|      7|    4|    1|        2|
|second|sample2|    2|      5|    3|    1|        2|
|second|sample2|    5|     10|    5|    2|        3|

