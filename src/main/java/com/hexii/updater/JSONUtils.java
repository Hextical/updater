package com.hexii.updater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public final class JSONUtils {
  
  private static final Logger LOGGER = LogManager.getLogger(JSONUtils.class);
  
  private static List<Long> brokenHashes = Collections.synchronizedList(new ArrayList<>());

  private JSONUtils() {}

  public static List<JSONObject> removeIrrelevantGameVersions(JSONArray ja,
      String userGameVersion) {

    ArrayList<JSONObject> newList = new ArrayList<>();

    for (int i = 0; i < ja.length(); i++) {

      JSONObject jo = (JSONObject) ja.get(i);
      JSONArray gv = (JSONArray) jo.get("gameVersion");

      for (int j = 0; j < gv.length(); j++) {

        String fileGameVersion = (String) gv.get(j);

        if (fileGameVersion.equals(userGameVersion)) {
          newList.add(jo);
        }

      }

    }

    return newList;

  }

  public static JSONObject findBestFile(List<JSONObject> joAL) {

    int bestFileIndex = Integer.MAX_VALUE;
    long lowestDifference = Long.MAX_VALUE;

    for (int i = 0; i < joAL.size(); i++) {

      JSONObject jo = joAL.get(i);
      String time = jo.getString("fileDate");
      long difference = Time.timeDifferenceMinutes(time);

      if (difference < lowestDifference) {
        bestFileIndex = i;
        lowestDifference = difference;
      }

    }

    return joAL.get(bestFileIndex);
  }
  
  public static boolean validJSON(JSONObject json) {
    
    JSONArray exactMatches = (JSONArray) json.get("exactMatches");
    boolean result = exactMatches.isEmpty();
    if (result) {
      long hash = (long) ((JSONArray) json.get("installedFingerprints")).get(0);
      brokenHashes.add(hash);
      LOGGER.info("DNE hash on CurseForge: " + hash);
    }
    
    return !result;
    
  }


}
