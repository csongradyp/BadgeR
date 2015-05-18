Narrative:
In order to visualize unlocked achievement wiht internationalized messages
As an achievement based system
I want to be notified about unlock and level up events with localized properties
So that I can visualize unlocked achievement

Lifecycle:
Before:
Given there is subscription for achievement unlocked events

Scenario: Recieved unlock event is localized
Given internationalization message files for BadgeR with locale <locale>
When an achievement with <id> id is unlocked
Then unlocked event received for achievement <id>
And the title of the achievement is localized as <title>
And the desciption of the achievement is localized as <description>

Examples:
|  id  |locale| title |             description             |
|simple|    en|English|Jackdaws love my big sphinx of quartz|
|simple|    hu| Magyar|\u00C1rv\u00EDzt\u00FBr\u00F5 t\u00FCk\u00F6rf\u00FAr\u00F3g\u00E9p|
|simple|    de|Deutsch|Victor jagt zw\u00F6lf Boxk\u00E4mpfer quer \u00FCber den gro\u00DFen Sylter Deich|
