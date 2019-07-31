package com.hexii.updater;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public final class JSONUtils {

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


}
