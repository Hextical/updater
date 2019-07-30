package com.hexii.updater;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Logging {

    private static final Logger log = LogManager.getLogger(Logging.class);

    public static void extraLogs(long timeStart, long timeEnd, long updates) {
	
	log.info("There are " + updates + " available mod updates.");
	log.info("Program execution time: " + TimeUnit.NANOSECONDS.toSeconds(timeEnd - timeStart) + " seconds.");

    }

}
