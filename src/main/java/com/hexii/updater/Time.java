package com.hexii.updater;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public final class Time {

  private Time() {}

  // Takes two dates with times and returns the difference in minutes.
  public static long timeDifferenceMinutes(String dateTimeEnd) {

    ZonedDateTime startDateTime = ZonedDateTime.now(ZoneOffset.UTC);
    ZonedDateTime endDateTime = ZonedDateTime.parse(dateTimeEnd);

    return ChronoUnit.MINUTES.between(endDateTime, startDateTime);

  }

}
