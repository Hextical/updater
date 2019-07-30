package com.hexii.updater;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public final class Time {

  private Time() {}

  // Takes two dates with times and returns the difference in minutes.
  public static long timeDifferenceMinutes(String dateTimeEnd) {
    
    ZonedDateTime startDateTime = ZonedDateTime.now(ZoneOffset.UTC);
    ZonedDateTime endDateTime = ZonedDateTime.parse(dateTimeEnd);

    // Excludes start and end dates; only the days between those dates will be
    // counted
    long duration = ChronoUnit.MINUTES.between(endDateTime, startDateTime);

    // Milliseconds to minute conversion
    return TimeUnit.MILLISECONDS.toMinutes(duration);

  }

}
