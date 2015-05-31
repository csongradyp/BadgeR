# BadgeR achievement engine ![Image of BadgeR](https://dl.dropboxusercontent.com/u/6057082/BagdeR_icon.png)

[![Build Status](https://travis-ci.org/csongradyp/badgeR.svg)](https://travis-ci.org/csongradyp/badgeR)
[![Coverage Status](https://coveralls.io/repos/csongradyp/badgeR/badge.svg?branch=master)](https://coveralls.io/r/csongradyp/badgeR?branch=master)

#### *Keep the codebase clean by separating achievement handling from your code.* ####

* You can separate achievement operations from production code with annotations
* It needs only one \*.json or \*.ini file for achievement declaration
* Supports internationalization (i18n)

## Features ##

#### 1. Annotation support ####

You can avoid mixing the achievement related codes with the real functional codes.

|Annotation|Function|
|:-----|:-----|
|`@AchievementEventTrigger`|Triggers a defined event by incrementing the event counter by one and checks for possible unlocked achievements|
|`@AchievementScore`|Triggers a defined event by setting the event counter by the given score and checks for possible unlocked achievements|
|`@AchievementUnlock`|Unlock achievement manually|
|`@AchievementCheck`|Checks for possible unlocked achievements|

#### 2. Easy achievement declaration ####

Achievements can be given with a single **\*.ini** or **\*.json** file.
There are only few conventions to follow:

1. Achievements should have a unique id
2. Achievements should belong to one type

##### 2.1 Json definition #####

[Define achievement in JSon format](JSON.md)

##### 2.1 Ini definition #####

[Define achievement in Ini format](INI.md)


Achievement properties:
* id
* subscription
* trigger
* category

There are 6 predefined achievement types which can be used to define a the unlock behavior of the achievement:

|Type|Description|
|:------------:|:-----|
|**single**    |Achievement with no triggers, can be unlocked only directly.|
|**score**     |Unlocked when one of the subscribed event counter scores are meeting trigger requirements|
|**scoreRange**|Unlocked when one of the subscribed event counter scores are within defined score range|
|**date**      |Unlocked when one of the given events are triggered one of the given days|
|**time**      |Unlocked when one of the given events are triggered within the given minutes|
|**timeRange** |Unlocked when one of the given events are triggered within the given time ranges|

#### 3. Get notified of unlocked achievements or updated events ####

Badger will notify every time when an achievement is unlocked or an  event counter is updated.

To subscribe just implement a handler and pass is to Badger. See more at usage section.

# Usage #

## 1.  Achievement definition ##

Achievement can be defined in two formats: JSON or INI.

## 2. Internationalization, localization (i18n) ##

There is an option to get i18n messages for unlocked achievements. Just create the i18n message \*.properties and give the base name to Badger.
For i18n message properties files find some examples [here](https://docs.oracle.com/javase/tutorial/i18n/intro/steps.html)

#### 2.1 localization keys ####
To let BangeR give you the resolved messages in the achievement information you should follow the following naming convention:

* Achievement title key is: ***[ID]*.title**
* Achievement description key is: ***[ID]*.text**

BadgeR's locale can be changed at any time or just let it to use the default locale of your JVM.

Without adding i18n basename Badger will pass the keys as value.

## 3. Wire into code ##

#### 3.1 Instantiate BadgeR ####
Create only **one instance** of BadgeR!

Create BadgeR with definition file 'achievements.json' and i18n messages properties files starting with achievementsMsg (e.g.: achievementsMsg_en.properties):

```java
final InputStream inStream = getClass().getClassLoader().getResourceAsStream("achievements.ini");
final Badger badger = new Badger(inStream, "achievementsMsg");
```

Or just with the definition file:
```java
final Badger badger = new Badger("relative/path/to/definition/achievements.json");
```

#### 3.2 Subscribe for BadgeR events ####

Subscribe for unlock events with a simple callback method.

```java
badger.subscribeOnUnlock(new IAchievementUnlockedHandler() {
    @Override
    public void onUnlocked(final AchievementUnlockedEvent unlockEvent) {
        final String title = unlockEvent.getTitle(); // localized title
        final String text = unlockEvent.getText();  // localized description
        final Integer level = unlockEvent.getLevel(); // unlocked level
        final Date acquireDate = unlockEvent.getAcquireDate(); // unlock time stamp
        final String triggerValue = unlockEvent.getTriggerValue(); // submitted value which unlocked the achievement

        // show achievement notification
    }
});

badger.subscribeOnUnlock(new IScoreUpdateHandler() {
    @Override
    public void onUnlocked(final ScoreUpdatedEvent scoreUpdatedEvent) {
        final String event = unlockEvent.getEvent(); // event name
        final String newValue = unlockEvent.getValue();  // new value of event counter
        
        // update something ...
    }
});
```

with lambda expression

```java
badger.subscribeOnUnlock(unlockEvent -> {
    // show achievement notification
} );

badger.subscribeOnUnlock(scoreUpdatedEvent -> {
    // update something ...
} );
```

#### 3.3 Trigger events ####

**trigger simply**

Trigger an event and increment its counter by one.

```java
@AchievementEventTrigger(name = "myEvent")
public void myMethod() {
    // doesn't matter what the method do, only that it is called.
}
```

equivalent with 

```java
badger.triggerEvent("myEvent");
```

**Trigger with score**

Trigger an event and set the counter score with parameter annotation

```java
@AchievementScore(counter = "myEvent")
public void myMethod(final @AchievementScoreParam Long newScore) {
    // doesn't matter what the method do, only that it is called.
}
```

or in case of other Object arguments by giving the score getter method

```java
// myObject.getMyScore(); - returns a Long

@AchievementScore(counter = "myEvent")
public void myMethod(final @AchievementScoreParam(getter = "getMyScore") MyObject myObject) {
    // doesn't matter what the method do, only that it is called.
}
```

equivalent with 

```java
final Long newScore = 100L; // the new score to be set
badger.triggerEvent("myEvent", newScore);
```

#### 3.4 Unlock achievement directly ####

Unlocked achievement directly by id. Optionally a trigger value will be stored only to provide information during unlock procedure.

```java
// unlock single achievement with id 'achievementId'
@AchievementUnlock(achievement = "achievementId")
public void myMethod1() {
    // doesn't matter what the method do, only that it is called.
}

// unlock single achievement with id 'achievementId' and provide unlock information
@AchievementUnlock(achievement = "achievementId", triggerValue = "special thing happened")
public void myMethod2() {
    // doesn't matter what the method do, only that it is called.
}

// unlock counter achievement with id 'achievementId' with value 10
@AchievementUnlock(achievement = "achievementId", triggerValue = "10")
public void myMethod3() {
    // doesn't matter what the method do, only that it is called.
}

// unlock date achievement with id 'achievementId' with date value of 30th of January
@AchievementUnlock(achievement = "achievementId", triggerValue = "01-30")
public void myMethod4() {
    // doesn't matter what the method do, only that it is called.
}
```

equivalent with 

```java
// unlock achievement with id 'achievementId'
badger.unlock("achievementId");

// unlock achievement with id 'achievementId' and provide unlock information
badger.unlock("achievementId", "special thing happened");

// unlock achievement with id 'achievementId' with value 10
badger.unlock("achievementId", "10");

// unlock achievement with id 'achievementId' with date value of 30th of January
badger.unlock("achievementId", "01-30");
```

#### 3.5 Check for unlocked achievements ####

Achievement check runs every time when an event is triggered. Nevertheless the checking can be done directly.

```java
@AchievementCheck
public void myMethod() {
    // doesn't matter what the method do, only that it is called.
}
```

equivalent with 


```java
badger.check();
```

# Enable annotation processing #

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
                <goal>test-compile</goal>  <!-- for testing -->
            </goals>
        </execution>
    </executions>
    <configuration>
        <showWeaveInfo>false</showWeaveInfo>
        <verbose>true</verbose>
        <complianceLevel>1.8</complianceLevel>
        <weaveDependencies>
            <weaveDependency>
                <groupId>net.csongradyp</groupId>
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
([csongrady.p@gmail.com](csongrady.p@gmail.com))

[LinkedIn profile](hu.linkedin.com/in/csongradyp)