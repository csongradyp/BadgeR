package net.csongradyp.badger;

import java.util.Date;

public interface IDateProvider {

    String now();

    String currentTime();

    String getDate(Date date);

    Boolean isCurrentTimeBefore(Date date);

    Boolean isCurrentTimeAfter(Date date);

    String getTime(Date date);
}
