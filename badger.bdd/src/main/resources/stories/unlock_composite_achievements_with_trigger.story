Narrative:
In order to handle achievement unlock and level up events
As an achievement based system
I want to be notified about unlock and level up events

Lifecycle:
Before:
Given there is subscription for achievement unlocked events

Scenario: Related event triggered so that composite achievement is unlocked - date & time

Given an achievement with <id> id and composite type
And an achievement with <id> id is subscribed to <event> event
And composite achievement with <id> id and with trigger <date>
And composite achievement with <id> id and with start trigger <start> and end trigger <end>
And the achievement with <id> is not unlocked
And current date is <date> and current time is <time>
When event named <event> is triggered
Then unlocked event received for achievement <id>
And the level of the unlocked achievement is 1

Examples:
|        id        |     event    | date|start| end | time|
|composite-dateTime|compositeEvent|05-04|04:00|05:00|04:10|
|composite-dateTime|compositeEvent|06-22|04:00|05:00|04:23|


Scenario: Related event triggered so that composite achievement is unlocked - date & score

Given composite achievement with <id> id and with trigger <date>
And composite achievement with <id> id and with trigger <trigger>
And the achievement with <id> is not unlocked
And the current <event> event score is 0
And the current date is <date>
When <event> event is triggered with <input-score> as a score
Then unlocked event received for achievement <id>
And the level of the unlocked achievement is 1

Examples:
|          id         |     event    | date|trigger|input-score|
|composite-dateCounter|compositeEvent|01-23|   100+|        123|
