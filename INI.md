# Ini Definition format #

In the ini based achievement definition there should be sections to be declared:
* **[event]** section: Define all events what can be triggered.
* achievement sections: Define achievements based on types: **[score], [scoreRange], [time], [timeRange], [date], [single]** and **[composite]**.

### 1. Events ###

Most of the achievements (single achievements not) should be subscribe one or more events. If an event is triggered all BadgeR will check for unlock from all the subscribed achievements
All the events are also a counter. They can be triggered with a concrete value.

```ini
[events]
event=level completed
event=earned score per level
```

### 2. Achievements ###

All Achievements have its own section.
The achievement section is defined with its type and its unique id: **[type/id]**. E.g.: [scoreRange/MyAchievement]

Within each achievement section can be the event subscriptions, triggers, category.

#### 2.1 Event subscriptions ####

An achievement can subscribe for one or more events. By following the ini structure in case of multiple event subscription it should be given in multiple lines.
Make sure that all used event is defined in the **[events]** section!

```ini
[counter/sample]
event=event one
event=event two
event=event three
```

#### 2.2 Triggers ####

An achievement can have multiple trigger definitions. Triggers are defined differently for every types.
Note: Single achievements does not have any triggers. If it is defined, it will be ignored.

|       Trigger|Description|
|-------------:|:-----|
|**score**     |Should be given a number or a number post-fixed with '**+**' or '**-**'. (e.g.: 20- or 30 or 50+) With no post-fix: trigger will fire only when the counter is equal to the given number. With '+' post-fix the trigger will fire only when the counter is greater than the given number. With '-' post-fix the trigger will fire only when the counter is less than the given number.|
|**scoreRange**|Should be given even lines of triggers as a number. All odd trigger will be a start trigger and all even triggers will be an end trigger.|
|**date**      |Should be given with pattern ***MM-dd*** (e.g.: 12-24 is 24th of december)|
|**time**      |Should be given with pattern ***HH:mm*** (e.g.: 23:42)|
|**timeRange** |Should be given even lines of triggers defined with the same pattern as the time trigger. All odd trigger will be a start trigger and all even triggers will be an end trigger. If the start time stamp is a time after the end trigger, they will be recognized as one trigger from the start trigger to midnight and another one from midnight to the end trigger. (e.g.: 20:00 - 08:00 will be used as 20:00 - 00:00 and 00:00 - 08:00)

#### 2.3. Relations ####

There is an option to create complex achievements by making multiple different typed achievements with the same ID and telling the relationship between them.
 In the "relations" section you can add the relation definition of the achievements with same ID. There should be only one relation definition for an achievement Id.
Allowed operators are **&** (and) **|** (or) and simple brackets **(** and **)**. Make sure to use brackets for proper precedence evaluation.

```ini
[relations]
achievementId= timeRange & counter
otherAchievementId= counter & (time | date)
```

## **Examples** ##

**Counter achievement examples**
```ini
[counter/sample]
event=my event
trigger=3
trigger=5
```

A counter achievement is defined with id "sample". Badger will check whether it can be unlocked or not if "my event" event is triggered. Achievement (level 1) will be unlocked if 'my event' was triggered 3 times all together. (Level 2 when it was triggered 5 times all together)

```ini
[counter/postfixsample]
event=my event
trigger=10-
trigger=20
trigger=30+
```

A counter achievement is defined with id "postfixsample". Badger will check whether it can be unlocked or not if "my event" event is triggered. Achievement (level 1) will be unlocked if the event is triggered with a score less than 10. If 'my event' is only triggered without any score, level 1 achievement will be unlocked because the counter will set automatically to 1 at the first time (in this case). Level 2 will be unlocked if the counter is equal to 20 and level 3 will be unlocked if the counter will be greater or equal to 30.

**Date achievement examples**

```ini
[date/dateExample]
event=event1
event=event2
trigger=01-25
trigger=01-30
```

A date achievement is defined with id "dateExample". It will be unlocked if one of the given events ('event1' or 'event2') is triggered on the 25th or 30th of January (Doesn't matter which year).

**Time achievement examples**

```ini
[date/timeExample]
event=event1
event=event2
trigger=08:00
trigger=20:00
```

A time achievement is defined with id "timeExample". It will be unlocked if one of the given events ('event1' or 'event2') is triggered at 08:00 or 20:00 (Doesn't matter which year, month or day).

**Time range achievement examples**

```ini
[date/timeRangeExample]
event=event1
event=event2
trigger=08:00
trigger=20:00
trigger=21:00
trigger=07:00
```

A time achievement is defined with id "timeRangeExample". It will be unlocked if one of the given events ('event1' or 'event2') is triggered between 08:00 and 20:00, 21:00 and 00:00 or 00:00 and 07:00 (Doesn't matter which year, month or day).

**Composite achievement examples**

```ini
[composite/composite-dateCounter]
event = compositeEvent
relation = date & score
dateTrigger = 01-23
scoreTrigger = 100+
```

"composite-dateCounter" will be unlocked if composite event is triggered and its counter is over 100 and the current date is 23th of January.