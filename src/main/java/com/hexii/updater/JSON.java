package com.hexii.updater;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public final class JSON {
  
  private static String currentfileName;
  private static String currentprojectID;
  private static String currentfileID;
  private static String currentdownloadURL;

  private JSON() {}

  // https://addons-ecs.forgesvc.net/api/v2/addon/{fileID}/files

  public static Map<String, List<String>> currentFileInfo(Iterable<JSONObject> jsonObjectsTS) {

    LinkedHashMap<String, List<String>> currentFileMap = new LinkedHashMap<>();

    for (JSONObject jo : jsonObjectsTS) {

      JSONArray arr = (JSONArray) jo.get("exactMatches");

      for (int i = 0; i < arr.length(); i++) {
        JSONObject json2 = arr.getJSONObject(i);
        JSONObject file = (JSONObject) json2.get("file");
        currentprojectID = Integer.toString((int) json2.get("id"));
        currentfileName = file.getString("fileName");
        currentdownloadURL = file.get("downloadUrl").toString();
        currentfileID = file.get("id").toString();
      }

      List<String> namePlusID = new ArrayList<>();
      namePlusID.add(0, currentfileName);
      namePlusID.add(1, currentdownloadURL);
      namePlusID.add(2, currentfileID);

      currentFileMap.put(currentprojectID, namePlusID);

    }

    return currentFileMap;

  }

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
