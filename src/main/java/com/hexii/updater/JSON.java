package com.hexii.updater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public final class JSON {

  private static final byte FILE_NAME_INDEX = 0;
  private static final byte DOWNLOAD_URL_INDEX = 1;
  private static final byte FILE_ID_INDEX = 2;

  // Contains a list of broken projectIDs
  private static List<String> brokenProjectIDs = Collections.synchronizedList(new ArrayList<>());

  private static final Logger LOGGER = LogManager.getLogger(JSON.class);

  private JSON() {}

  // https://addons-ecs.forgesvc.net/api/v2/addon/{fileID}/files

  public static Map<String, List<String>> cleanupOldFiles(Iterable<JSONObject> jsonObjects) {

    Map<String, List<String>> oldFileMap = new HashMap<>();

    for (JSONObject jo : jsonObjects) {

      JSONArray exactMatches = (JSONArray) jo.get("exactMatches");

      oldFileMap.putAll(currentfileInfo(exactMatches));

    }

    return oldFileMap;

  }

  public static Map<String, List<String>> cleanupNewFiles(Map<String, JSONArray> jsons) {

    Map<String, List<String>> newFileMap = new HashMap<>();

    for (Map.Entry<String, JSONArray> entry : jsons.entrySet()) {

      List<JSONObject> removedstuff =
          JSONUtils.removeIrrelevantGameVersions(entry.getValue(), Updater.USERGAMEVERSION);

      if (!removedstuff.isEmpty()) {

        JSONObject bestFile = JSONUtils.findBestFile(removedstuff);
        List<String> stuff = grabAttributesFromFile(bestFile);
        newFileMap.put(entry.getKey(), stuff);

      } else {
        LOGGER.info(entry.getKey() + " is broken/unavailable for the specified version.");
        brokenProjectIDs.add(entry.getKey());
      }

    }

    return newFileMap;


  }

  // Processes one JSONArray of "exactMatches" and returns a Map containing the three attributes
  private static Map<String, List<String>> currentfileInfo(JSONArray jsonArray) {

    Map<String, List<String>> fileMap = new HashMap<>();

    for (int i = 0; i < jsonArray.length(); i++) {

      JSONObject json2 = jsonArray.getJSONObject(i);
      JSONObject file = (JSONObject) json2.get("file");

      final String projectID = json2.get("id").toString();

      List<String> fileAttr = grabAttributesFromFile(file);

      // Having this within the for loop may cause extra processing, but it's fine since a Map key
      // is unique by definition, so there will actually be no duplicates.
      fileMap.put(projectID, fileAttr);

    }

    return fileMap;

  }

  // A file attribute list contains a fileName, downloadURL, and fileID
  private static List<String> grabAttributesFromFile(JSONObject file) {

    List<String> fileAttr = new ArrayList<>();

    final String fileName = file.get("fileName").toString();
    final String downloadURL = file.get("downloadUrl").toString();
    final String fileID = file.get("id").toString();

    fileAttr.add(FILE_NAME_INDEX, fileName);
    fileAttr.add(DOWNLOAD_URL_INDEX, downloadURL);
    fileAttr.add(FILE_ID_INDEX, fileID);

    return fileAttr;

  }

}
