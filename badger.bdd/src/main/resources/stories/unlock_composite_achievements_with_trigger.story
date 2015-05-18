Narrative:
In order to handle achievement unlock and level up events
As an achievement based system
I want to be notified about unlock and level up events

Lifecycle:
Before:
Given there is subscription for achievement unlocked events

Scenario: Related event triggered so that composite achievement is unlocked - date & time

Given an achievement with <id> id and composite type
And has a child with <id> id and date type bounded to <event> event with trigger <date>
And has a child with <id> id and timerange type bounded to <event> event with start trigger <start> and end trigger <end>
And the achievement with <id> is not unlocked
And current date is <date> and current time is <time>
When event named <event> is triggered
Then achievement unlocked event is received
And achievement id is <id>
And the level of the unlocked achievement is 1

Examples:
|        id        |     event    | date|start| end | time|
|composite-dateTime|compositeEvent|05-04|04:00|05:00|04:10|
|composite-dateTime|compositeEvent|06-22|04:00|05:00|04:23|
