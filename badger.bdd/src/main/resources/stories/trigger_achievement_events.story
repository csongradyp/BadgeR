Story: Notify user about event trigger updates

Narrative:
In order to notify the system of event counter/score updates
As an achievement based system
I want to be notified about event score changes for further process

Lifecycle:
Before:
Given there is subscription for score events

Scenario: Achievement event is triggered withous defined score

Given the current <event> event score is <score>
When event named <event> is triggered
Then the score event is received
And is related to <event>
And the value of the score is <expected-score>

Examples:
|event  |score|expected-score|
|sample |    0|             1|
|sample |    1|             2|
|sample2|   -1|             0|
|sample3|   24|            25|

Scenario: Achievement event is triggered with same score as stored

Given the current <event> event score is <score>
When <event> event is triggered with <score> as a <score-type>
Then no event is received

Examples:
|event  |score|score-type|
|sample |    2|     score|
|sample3|    3| highscore|

Scenario: Achievement event is triggered with specified score

Given the current <event> event score is <score>
When <event> event is triggered with <input-score> as a score
Then the score event is received
And is related to <event>
And the value of the score is <expected-score>

Examples:
|event  |      score|input-score|expected-score|score-type|
|sample |          0|         10|            10|     score|
|sample |         12|          9|             9|     score|
|sample2|        -20|         40|            40|     score|
|sample2|         66|         30|            30|     score|
|sample2|         12|         -5|            -5|     score|

Scenario: Achievement event is triggered with specified score as a real highscore

Given the current <event> event score is <score>
When <event> event is triggered with <input-score> as a highscore
Then the score event is received
And is related to <event>
And the value of the score is <expected-score>

Examples:
|event  |      score|input-score|expected-score|
|sample |          0|         10|            10|
|sample |         12|         13|            13|
|sample |        -62|        999|           999|
|sample |        -64|        -20|           -20|


Scenario: Achievement event is triggered as a highscore with lower score as the current highscore

Given the current <event> event score is <score>
When <event> event is triggered with <input-score> as a highscore
Then no event is received
And the current <event> event score is <score>

Examples:
|event  |      score|input-score|
|sample |        100|         10|
|sample |         12|          9|
|sample |         11|          9|
|sample |         66|         65|
|sample |         66|        -12|
|sample2|         43|        -20|
|sample2|          0|         -1|
