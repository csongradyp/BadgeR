Narrative:
In order to handle achievement unlock and level up events
As an achievement based system
I want to be notified about unlock and level up events
So that I can visualize unlocked achievement or use for further processes

Lifecycle:
Before:
Given there is subscription for score events

Scenario: Related event triggered

Given the current sample event score is 11
When event named sample triggered via annotation
Then the score event is received
And is related to sample
And the value of the score is 12
And the current sample event score is 12

Scenario: Related event triggered multiple times

Given the current <event> event score is <score>
When event named <event> is triggered <trigger> times via annotation
Then the number of received score event is <trigger>
And the current <event> event score is <current>

Examples:
|  id  | event |score|trigger|current|
|first |sample |    1|      3|      4|
|second|sample2|   10|      2|     12|

Scenario: Achievement event is triggered with specified score

Given the current <event> event score is <score>
When <event> event is triggered via annotation with <input-score> as a score given with annotation parameter
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
When <event> event is triggered via annotation with <input-score> as a highscore given with annotation parameter
Then the score event is received
And is related to <event>
And the value of the score is <expected-score>

Examples:
| event |    score  |input-score|expected-score|
|sample |          0|         10|            10|
|sample |         12|         13|            13|
|sample |        -62|        999|           999|
|sample |        -64|        -20|           -20|

Scenario: Achievement event is triggered with specified score from an object

Given the current <event> event score is <score>
When <event> event is triggered via annotation with <input-score> as a score given with object parameter
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

Scenario: Achievement event is triggered with specified score as a real highscore from an object

Given the current <event> event score is <score>
When <event> event is triggered via annotation with <input> as a highscore given with object parameter
Then the score event is received
And is related to <event>
And the value of the score is <expected-score>

Examples:
| event |    score  |input|expected-score|
|sample |          0|   10|            10|
|sample |         12|   13|            13|
|sample |        -62|  999|           999|
|sample |        -64|  -20|           -20|
