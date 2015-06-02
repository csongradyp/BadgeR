Narrative:
In order to handle score triggered achievement unlock and level up events
As an achievement based system
I want to be notified about unlock and level up events
So that I can visualize unlocked achievement or use for further processes

Lifecycle:
Before:
Given there is subscription for achievement unlocked events

Scenario: Related event triggered so that score range achievement is unlocked

Given an achievement with <id> id and scorerange type bounded to <event> event with start trigger <start> and end trigger <end>
And the achievement with <id> is not unlocked
And the current <event> event score is <score>
When event named <event> is triggered via annotation
Then unlocked event received for achievement <id>
And the level of the unlocked achievement is 1

Examples:
|      id     | event |start| end |score|
|score-range  |sample2|    0|   10|    7|
|score-reverse|sample2|   10|    0|   22|

Scenario: When Related event triggered and achievement is already unlocked score range achievement will not unlocked again

Given an achievement with <id> id and scorerange type bounded to <event> event with start trigger <start> and end trigger <end>
And the achievement with <id> id is unlocked
And the current <event> event score is <score>
When event named <event> is triggered via annotation
Then no achievement unlocked event received related to <id>

Examples:
|      id     | event |start| end |score|
|score-range  |sample2|    0|   10|    7|
|score-range  |sample2|    0|   10|   10|
|score-range  |sample2|    0|   10|    0|
|score-reverse|sample2|   10|    0|   22|

Scenario: When Related event triggered and event score value does not meet trigger unlock requirements then score range achievement will not unlocked

Given an achievement with <id> id and scorerange type bounded to <event> event with start trigger <start> and end trigger <end>
And the achievement with <id> id is unlocked
And the current <event> event score is <score>
When event named <event> is triggered via annotation
Then no achievement unlocked event received related to <id>

Examples:
|      id     | event |start| end |score|
|score-range  |sample2|    0|   10|   11|
|score-reverse|sample2|   10|    0|    5|
