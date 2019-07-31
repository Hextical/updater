package com.hexii.updater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public final class JSON {

  private static String currentprojectID;
  private static String currentfileName;
  private static String currentfileID;
  private static String currentdownloadURL;

  private static final byte FILE_NAME_INDEX = 0;
  private static final byte DOWNLOAD_URL_INDEX = 1;
  private static final byte FILE_ID_INDEX = 2;

  private static final Logger LOGGER = LogManager.getLogger(JSON.class);

  private JSON() {}

  // https://addons-ecs.forgesvc.net/api/v2/addon/{fileID}/files

  public static Map<String, List<String>> currentFileInfo(Iterable<JSONObject> jsonObjectsTS) {

    Map<String, List<String>> currentFileMap = new ConcurrentHashMap<>();

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

      try {
        namePlusID.add(FILE_NAME_INDEX, currentfileName);
        namePlusID.add(DOWNLOAD_URL_INDEX, currentdownloadURL);
        namePlusID.add(FILE_ID_INDEX, currentfileID);
        currentFileMap.put(currentprojectID, namePlusID);
      } catch (NullPointerException e) {
        LOGGER.error("Error: " + e + " Object passed in: " + jo);
      }

    }

    return currentFileMap;

  }

}
