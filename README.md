# BadgeR achievement engine Overview#

BadgeR is a java based achievement engine.

* It makes achiement registration and handling easier
* Developers can separate achivement operations from production code with the provided annotations
* It needs only one file for achievement declaration and language (i18n) files for internationalization

## Features ##

* **Annotation driven**

You can avoid mixing the achievement related codes with the real functional codes.

|Annotation|Function|
|:-----|:-----|
|`@AchievementEventTrigger`|Triggers a defined event by incrementing the event counter by one and checks for possible unlocked achievements|
|`@AchievementScore`|Triggers a defined event by setting the event counter by the given score and checks for possible unlocked achievements|
|`@AchievementCheck`|Checks for possible unlocked achievements|
|`@AchievementUnlock`|Unlock achievement manually|

* **Easy achievement definition**

Achievements can be given with a single ini file.
There are only few conventions to follow:

1. All achievements should have a type
2. Achievement should have a unique id within a type
3. If there are more than one achievement with the same id with different types there should be a relation defined.

There are 5 predefined achievement types which can be used to define a the unlock behavior of the achievement:

|Type|Description|
|:------|:-----|
|**single**|Achievement with no triggers. They can be unlocked by directly telling to unlock.|
|**counter**|Achievements with score based triggers. These achievements are bonded with the defined events. Event counters can be incremented by one or with a given score.|
|**date**|Achievements with date trigger. These achievements are bonded with the defined events. If one of the given events are triggered within (one of) the given day(s), the achievement will be unlocked.|
|**time**|Achievements with time trigger. These achievements are bonded with the defined events. If one of the given events are triggered within (one of) the given minutes(s), the achievement will be unlocked.|
|**timeRange**|Achievements with time trigger. These achievements are bonded with the defined events. If one of the given events are triggered within the given time ranges the achievement will be unlocked.|

* **Get notified of unlocked achivements or updated events**
Badger will notify every time when an achievement is unlocked or a counter is updated.

To subscribe just implement a handler and pass is to Badger. See more at usage section.

# Usage #

## 1.  Achievement definition ##

There are Two sections which are not directly defining the achievement but the events (or counters) and optionally the relation between achievements.

### 1.1. Events ###

Most of the achievements (single achievements not) should be subscribe one or more events. If an event is triggered all BadgeR will check for unlock from all the subscribed achievements
All the events are also a counter. They can be triggered with a concrete value.
```
[events]
event=level completed
event=earned score per level
```

### 1.2. Relations ###

There is an option to create complex achievements by making multiple diffferent typed achiecements with the same ID and telling the relationship between them.
 In the "relations" section you can add the relation definition of the achievements with same ID. There should be only one relation definition for an achievement Id.
Allowed oprators are **&** (and) **|** (or) and simple brackets **(** and **)**. Make shure to use brackets for proper precedency evaluation.

```
[relations]
achievementId= timeRange & counter
otherAchievementId= counter & (time | date)
```

### 1.3. Achievements ###

All Achievements have its own section.
The achievement section is defined with its type and its unique id: *[type/id]*
Within the section are the event subscriptions and the triggers.

**1.3.1 Event subscriptions**

An achievement can subscribe for one or more events. By following the ini strucure multiple evet subscription should be given in multiple lines.
Make sure that all event is defined in the **[events]** section!

```
[counter/sample]
event=event one
event=event two
event=event three

```

**1.3.1 Triggers**

An achievement can have multiple trigger definitions. Triggers are defined differently for every types.
Note: Single achievements does not have any triggers.

|Trigger|Properties|
|:------|:-----|
|**counter**|Should be given a number or a number post-fixed with '**+**' or '**-**'. (e.g.: 20- or 30 or 50+) With no post-fix: trigger will fire only when the counter is equal to the given number. With '+' post-fix the trigger will fire only when the counter is greater than the given number. With '-' post-fix the trigger will fire only when the counter is less thanthe given number.|
|**date**|Should be given with pattern ***MM-dd*** (e.g.: 12-24 is 24th of december)|
|**time**|Should be given with pattern ***HH:mm*** (e.g.: 23:42)|
|**timeRange**|Should be given even lines of triggers defined with the same pattern as the time trigger. All odd trigger will be a start trigger and all even triggers will be an end trigger. If the start time stamp is a time after the end trigger, they will be recognized as one trigger from the start trigger to midnight and another one from midnight to the end trigger. (e.g.: 20:00 - 08:00 will be used as 20:00 - 00:00 and 00:00 - 08:00)


## **Examples** ##

**Counter achievement examples**
```
[counter/sample]
event=my event
trigger=3
trigger=5
```

A counter achievement is defined with id "sample". Badger will check whether it can be unlocked or not if "my event" event is triggered. Achievement (level 1) will be unlocked if 'my event' was triggered 3 times all together. (Level 2 when it was triggered 5 times all together)

```
[counter/postfixsample]
event=my event
trigger=10-
trigger=20
trigger=30+
```

A counter achievement is defined with id "postfixsample". Badger will check whether it can be unlocked or not if "my event" event is triggered. Achievement (level 1) will be unlocked if the event is triggered with a score less than 10. If 'my event' is only triggered without any score, level 1 achievement will be unlocked because the counter will set automatically to 1 at the first time (in this case). Level 2 will be unlocked if the counter is equal to 20 and level 3 will be unlocked if the counter will be greater or equal to 30.

**Date achievement examples**

```
[date/dateExample]
event=event1
event=event2
trigger=01-25
trigger=01-30
```

A date achievement is defined with id "dateExample". It will be unlocked if one of the given events ('event1' or 'event2') is triggered on the 25th or 30th of January (Doesn't matter which year).

**Time achievement examples**

```
[date/timeExample]
event=event1
event=event2
trigger=08:00
trigger=20:00
```

A time achievement is defined with id "timeExample". It will be unlocked if one of the given events ('event1' or 'event2') is triggered at 08:00 or 20:00 (Doesn't matter which year, month or day).

**Time range achievement examples**

```
[date/timeRangeExample]
event=event1
event=event2
trigger=08:00
trigger=20:00
trigger=21:00
trigger=07:00
```

A time achievement is defined with id "timeRangeExample". It will be unlocked if one of the given events ('event1' or 'event2') is triggered between 08:00 and 20:00, 21:00 and 00:00 or 00:00 and 07:00 (Doesn't matter which year, month or day).

## 2. Internatiolazation, localization (i18n) ##

There is an option to get i18n messages directly when achievements are unlocked. For this you should create the i18n message properties and give the base name to Badger.
Find some examples [here](https://docs.oracle.com/javase/tutorial/i18n/intro/steps.html)

By following BangeR convention to give messages with key *achieventId* **.title** and *achieventId* **.text** in the messages properties file BadgeR will give directly the resolved messages in the achievement information. 

BadgeR's locale can be changed at any time or just let it to use the default locale of your JVM.

## 3. Wire into code ##

### 3.1 Instantiate BadgeR ###
Create only** one instance** of BadgeR

The following example requires an achievement definition file 'achievements.ini' and i18n messages properties files starting with achievementsMsg (e.g.: achievementsMsg_en.properties)

```
final InputStream inStream = getClass().getClassLoader().getResourceAsStream("achievements.ini");
final Badger badger = new Badger(inStream, "achievementsMsg");
```

### 3.1 Subscribe for BadgeR events ###

Subscribe for unlock events with a simple callback method.

```
badger.subscribeOnUnlock(new IAchievementUnlockedHandler() {
    @Override
    public void onUnlocked(final Achievement achievement) {
        final String title = achievement.getTitle(); // localized title
        final String text = achievement.getText();  // localized description
        final Integer level = achievement.getLevel(); // unlocked level
        final Date acquireDate = achievement.getAcquireDate(); // unlock time stamp
        final String triggerValue = achievement.getTriggerValue(); // submitted value which unlocked the achievement

        // show achievement notification
    }
});
```

with lambda expression


```
badger.subscribeOnUnlock((IAchievementUnlockedHandler) achievement -> {
    final String title = achievement.getTitle(); // localized title
    final String text = achievement.getText();  // localized description
    final Integer level = achievement.getLevel(); // unlocked level
    final Date acquireDate = achievement.getAcquireDate(); // unlock time stamp
    final String triggerValue = achievement.getTriggerValue(); // submitted value which unlocked the achievement

    // show achievement notification
} );
```

### 3.1 Trigger events ###

**Simple trigger**

Trigger an event and increment its counter by one.

```
@AchievementEventTrigger(name = "myEvent")
public void myMethod() {
    // doesn't matter what the method do, only that it is called.
}
```

equivalent with 

```
badger.triggerEvent("myEvent");
```

**Score trigger**

Trigger an event and set the counter score.

```
@AchievementScore(counter = "myEvent")
public void myMethod(final @AchievementScoreParam Long newScore) {
    // doesn't matter what the method do, only that it is called.
}
```

equivalent with 

```
final Long newScore = 100L; // the new score to be set
badger.triggerEvent("myEvent", newScore);
```

You can retrieve score value from an Object argument by telling the getter method for it. It can be useful when the annotated methods are event receivers or some other methods with complex type arguments.

```
@AchievementScore(counter = "myEvent")
public void myMethod(final @AchievementScoreParam(getter = "getHighscore") Player player) {
    // doesn't matter what the method do, only that it is called.
}
```

equivalent with 

```
badger.triggerEvent("myEvent", player.getHighscore());
```
**Unlock achievement directly**

Achievements can be unlocked directly.

The type, id of the achievement should be given and optionally a trigger/score value as a String can be stored. The given "triggerValue" will not be evaluated, it will be stored only to provide information during unlock procedure.

Achievements without trigger can be only unlocked with the following way:

```
// unlock single achievement with id 'achievementId' that has no triggers
@AchievementUnlock(type = AchievementType.SINGLE, achievement = "achievementId")
public void myMethod1() {
    // doesn't matter what the method do, only that it is called.
}

// unlock single achievement with id 'achievementId' that has no triggers and provide unlock information
@AchievementUnlock(type = AchievementType.SINGLE, achievement = "achievementId", triggerValue = "special thing happened")
public void myMethod2() {
    // doesn't matter what the method do, only that it is called.
}

// unlock counter achievement with id 'achievementId' with value 10
@AchievementUnlock(type = AchievementType.COUNTER, achievement = "achievementId", triggerValue = "10")
public void myMethod3() {
    // doesn't matter what the method do, only that it is called.
}

// unlock date achievement with id 'achievementId' with value/date 30th of January
@AchievementUnlock(type = AchievementType.DATE, achievement = "achievementId", triggerValue = "01-30")
public void myMethod4() {
    // doesn't matter what the method do, only that it is called.
}
```

equivalent with 

```
// unlock single achievement with id 'achievementId' that has no triggers
badger.unlock(AchievementType.SINGLE, "achievementId");

// unlock single achievement with id 'achievementId' that has no triggers and provide unlock information
badger.unlock(AchievementType.SINGLE, "achievementId", "special thing happened");

// unlock counter achievement with id 'achievementId' with value 10
badger.unlock(AchievementType.COUNTER, "achievementId", "10");

// unlock date achievement with id 'achievementId' with value/date 30th of January
badger.unlock(AchievementType.DATE, "achievementId", "01-30");
```

**Check for unlocked achievements**

Achievement check runs every time when an event is triggered. Nevertheless the checking can be done directly.

```
@AchievementCheck
public void myMethod() {
    // doesn't matter what the method do, only that it is called.
}
```

equivalent with 


```
badger.check();
```

# Make it work #

To enable annotation driven features make sure you use AspectJ veawing.

Here is my maven example from my other project where I use BadgeR:

```
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>aspectj-maven-plugin</artifactId>
    <version>1.7</version>
    <dependencies>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjrt</artifactId>
            <version>1.8.5</version>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjtools</artifactId>
            <version>1.8.5</version>
        </dependency>
    </dependencies>
    <executions>
        <execution>
            <phase>process-classes</phase>
            <goals>
                <goal>compile</goal>
                <goal>test-compile</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <showWeaveInfo>false</showWeaveInfo>
        <verbose>true</verbose>
        <complianceLevel>1.8</complianceLevel>
        <weaveDependencies>
            <weaveDependency>
                <groupId>badger</groupId>
                <artifactId>badger</artifactId>
            </weaveDependency>
        </weaveDependencies>
        <aspectLibraries>
            <dependency>
                <groupId>org.aspectj</groupId>
                <artifactId>aspectjrt</artifactId>
            </dependency>
        </aspectLibraries>
        <source>1.8</source>
        <target>1.8</target>
    </configuration>
</plugin>
```


## Author ##

**Peter Csongrady**

[csongrady.p@gmail.com](csongrady.p@gmail.com)

[LinkedIn profile](hu.linkedin.com/in/csongradyp)