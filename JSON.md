# JSon Definition format #

In the ini based achievement definition there should be sections to be declared:
* **event** section: Define all events what can be triggered.
* **achievement** section: Define achievement arrays based on types: **score, scoreRange, time, timeRange, date, single** and **composite**.

### 1. Events ###

Most of the achievements (single achievements not) should be subscribe one or more events. If an event is triggered all BadgeR will check for unlock from all the subscribed achievements
All the events are also a counter. They can be triggered with a concrete value.

```json
"events" : [
    "sample",
    "myEvent",
    "other Event"
]
```

### 2. Achievements ###

All Achievements are JSon objects.
```json
{ "id" : "myAchievement",
  "category": "myGroup1"
  "subscription" : [ ... ],
  "trigger" : [ ... ] 
}
```
Within each achievement section can be the event subscriptions, triggers, category.

#### 2.1 Event subscriptions ####

An achievement can subscribe for one or more events. By following the ini structure in case of multiple event subscription it should be given in multiple lines.
Make sure that all used event is defined in the **events** section!

```json
{ "id" : "myAchievement",
  "subscription" : [ "sample", "myEvent"],
  "trigger" : [ ... ] 
}
```

#### 2.2 Triggers ####

An achievement can have multiple trigger definitions. Triggers are defined differently for every types.
Note: Single achievements does not have any triggers. If it is defined, it will be ignored.

|       Trigger|Description|
|-------------:|:-----|
|**score**     |Should be given a number or a number post-fixed with '**+**' or '**-**'. (e.g.: 20- or 30 or 50+) With no post-fix: trigger will fire only when the counter is equal to the given number. With '+' post-fix the trigger will fire only when the counter is greater than the given number. With '-' post-fix the trigger will fire only when the counter is less than the given number.|
|**scoreRange**|Should be given the start and the end score (inclusively). If start score is greater than the end score the range will be reversed and used exclusively.E.g.: {"start":10, "end":0} will recognized as all numbers but 1-9.|
|**date**      |Should be given with pattern ***MM-dd*** (e.g.: 12-24 is 24th of december)|
|**time**      |Should be given with pattern ***HH:mm*** (e.g.: 23:42)|
|**timeRange** |Should be given the start and the end time (inclusively). If the start time stamp is a time after the end trigger, they will be recognized as one trigger from the start trigger to midnight and another one from midnight to the end trigger. (e.g.: 20:00 - 08:00 will be used as 20:00 - 00:00 and 00:00 - 08:00 Exclusively)

#### 2.3. Relations ####

There is an option to create composite achievements with multiple triggers with different types.
The composite achievements have a special **"relation"** section where you can add the relation between the trigger types.
Allowed operators are **&** (and), **|** (or) and simple brackets **(** and **)**. Make sure to use brackets for proper precedence evaluation.

Examples:
```json
"relation" : timeRange & score
```
or
```json
"relation" : score & (time | date)
```

## **Examples** ##

**Complete example**
```
{
	"events" : ["sample", "sample2", "sample3", "compositeEvent", "dateEvent", "timeEvent"],
	"achievements" : {
		"single":[
		  { "id" : "simple"}
		],
        "score":[
			{ "id" : "first", "subscription" : ["sample"], "trigger" : ["3","7"] },
			{ "id" : "second", "subscription" : ["sample2"], "trigger" : ["2","5","10"] }
		],
		"scoreRange":[
			{ "id" : "score-range", "subscription" : ["sample2"], "trigger" : [{"start":0, "end":10}] },
			{ "id" : "score-reverse", "subscription" : ["sample3"], "trigger" : [{"start":10, "end":0}] }
		],
		"date":[
			{ "id" : "date-test-05-05", "subscription" : ["dateEvent"], "trigger" : ["05-05"] },
			{ "id" : "date-test-12-30", "subscription" : ["dateEvent"], "trigger" : ["12-30"] }
		],
		"time":[
			{ "id" : "time-test-0815", "subscription" : ["timeEvent"], "trigger" : ["08:15"] },
			{ "id" : "time-test-2357", "subscription" : ["timeEvent"], "trigger" : ["23:57"] }
		],
		"timeRange":[
			{ "id" : "time-01-02", "subscription" : ["timeEvent"], "trigger" : [{"start":"01:30", "end":"02:00"}] },
			{ "id" : "time-14-15", "subscription" : ["timeEvent"], "trigger" : [{"start":"14:59", "end":"15:00"}] },
            { "id" : "time-reverse", "subscription" : ["timeEvent"], "trigger" : [{"start":"23:00", "end":"22:00"}] }
		],
		"composite":[
			{ 	"id" : "composite-dateTime", "subscription" : ["compositeEvent"],
				"relation" : "timeRange & date",
				"dateTrigger" : ["05-04", "06-22"],
				"timeRangeTrigger" : [{"start":"04:00", "end":"05:00"}]
			},
			{ 	"id" : "composite-dateCounter", "subscription" : ["compositeEvent"],
				"relation" : "date & score",
				"scoreTrigger" : ["100+"],
				"dateTrigger" : ["01-23"]
			}
		]
	}
}
```