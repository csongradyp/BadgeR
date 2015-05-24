package net.csongradyp.badger.provider.date;

import org.joda.time.LocalTime;

import java.util.Date;

public interface IDateProvider {

    String currentDateString();

    Date currentDate();

    String currentTimeString();

    Date currentTime();

    String getDate(Date date);

    Date parseDate(String date);

    String getTime(Date date);

    LocalTime parseTime(String time);
}
