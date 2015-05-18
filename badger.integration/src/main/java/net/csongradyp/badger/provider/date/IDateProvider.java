package net.csongradyp.badger.provider.date;

import java.util.Date;

public interface IDateProvider {

    String currentDate();

    String currentTime();

    String getDate(Date date);

    Boolean isCurrentTimeBefore(Date date);

    Boolean isCurrentTimeAfter(Date date);

    String getTime(Date date);
}
