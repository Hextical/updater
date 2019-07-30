package com.hexii.updater;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class Updater {

  private static final Logger LOGGER = LogManager.getLogger(Updater.class);

  public static void main(String[] args) throws IOException {

    LOGGER.info("Starting execution of program...");

    // Duration of program execution
    long timeStart = System.nanoTime();

    // Path & Game Version
    final String USERPATH = args[0];
    final String USERGAMEVERSION = args[1];

    // All hashes of JARs
    List<Long> hashes = FolderOperations.folderToHash(USERPATH);

    // Contains the whole JSONObject of given hash (file) fingerprint
    List<JSONObject> jsonObject = Connections.connectWithHash(hashes);

    // Key: currentprojectID
    // Value: currentfileName[0] currentfileID[1]
    Map<String, List<String>> currentFileMap = JSON.currentFileInfo(jsonObject);

    Map<String, JSONArray> jsons = Connections.connectWithProjectID(currentFileMap);
    Map<String, List<String>> newMap = new HashMap<>();

    List<String> brokenProjectIDs = Collections.synchronizedList(new ArrayList<String>());
    
    // This will be gone soon (in a different file)...

    for (String key : jsons.keySet()) {
      List<JSONObject> removedstuff =
          JSON.removeIrrelevantGameVersions(jsons.get(key), USERGAMEVERSION);

      if (!removedstuff.toString().equals("[]")) {
        JSONObject bestFile = JSON.findBestFile(removedstuff);
        String filename = bestFile.get("fileName").toString();
        String downloadURL = bestFile.get("downloadUrl").toString();
        String fileID = bestFile.get("id").toString();
        List<String> stuff = new ArrayList<>();
        stuff.add(0, filename);
        stuff.add(1, downloadURL);
        stuff.add(2, fileID);
        newMap.put(key, stuff);
      } else {
        LOGGER.info(key + " is broken/unavailable.");
        brokenProjectIDs.add(key);
      }
    }

    int updates = 0;
    for (String key : newMap.keySet()) {
      if (!newMap.get(key).get(0).equals(currentFileMap.get(key).get(0))) {
        LOGGER.info(newMap.get(key).get(0));
        LOGGER.info("\t" + newMap.get(key).get(1));
        updates++;
      }
    }

    JSONObject manifest = Manifest.createManifest(USERGAMEVERSION, "forge-14.23.5.2836", "", "");
    manifest = Manifest.addFiles(newMap, manifest);

    JSONObject old = Manifest.createManifest(USERGAMEVERSION, "forge-14.23.5.2836", "", "");
    old = Manifest.addFiles(currentFileMap, old);


    LOGGER.info(manifest);
    LOGGER.info(old);

    long timeEnd = System.nanoTime();

    Logging.extraLogs(timeStart, timeEnd, updates);

  }

}
