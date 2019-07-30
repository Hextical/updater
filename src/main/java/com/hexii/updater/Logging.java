package com.hexii.updater;

import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Logging {

  private static final Logger LOGGER = LogManager.getLogger(Logging.class);

  
  private Logging() {}

  public static void extraLogs(long timeStart, long timeEnd, long updates) {

    LOGGER.info("There are " + updates + " available mod updates.");
    LOGGER.info("Program execution time: " + TimeUnit.NANOSECONDS.toSeconds(timeEnd - timeStart)
        + " seconds.");

  }

}
