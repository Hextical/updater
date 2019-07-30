package com.hexii.updater;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Time {
    
    private Time() {}

    private static final Logger log = LogManager.getLogger(Time.class);

    // Takes two dates with times and returns the difference in minutes.
    public static long timeDifferenceMinutes(String dateEnd) {

	Date date = new Date();
	SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	String dateStart = format1.format(date);

	Date startDate = null;
	Date endDate = null;

	try {
	    startDate = format1.parse(dateStart);
	    endDate = format1.parse(dateEnd);
	} catch (ParseException e1) {

	    try {
		dateStart = format2.format(date);
		startDate = format2.parse(dateStart);
		endDate = format2.parse(dateEnd);
	    } catch (ParseException e2) {
		log.error("Error parsing dates.");
	    }

	}

	// Excludes start and end dates; only the days between those dates will be
	// counted
	long duration = startDate.getTime() - endDate.getTime();

	// Milliseconds to minute conversion
	return TimeUnit.MILLISECONDS.toMinutes(duration);

    }

}
