package com.hexii.updater;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import com.hexii.updater.filehandler.FolderOperations;

public class Updater {

  private static final Logger LOGGER = LogManager.getLogger(Updater.class);

  public static String USERPATH;
  public static String USERGAMEVERSION;

  public static void main(String[] args) throws IOException {

    LOGGER.info("Starting execution of program...");

    // Duration of program execution
    long timeStart = System.nanoTime();

    // Path & Game Version
    USERPATH = args[0];
    USERGAMEVERSION = args[1];

    // All hashes of JARs
    List<Long> hashes = FolderOperations.folderToHash(USERPATH);

    // Contains the whole JSONObject of given hash (file) fingerprint
    List<JSONObject> jsonObject = Connections.connectWithHash(hashes);

    // Key: currentprojectID
    // Value: currentfileName[0] currentfileID[1]
    Map<String, List<String>> currentFileMap = JSON.cleanupOldFiles(jsonObject);

    // Contains a bunch of currentprojectIDs with the JSONArrays found when connecting with the
    // addonID from the Twitch API
    Map<String, JSONArray> jsons = Connections.connectWithProjectID(currentFileMap);

    // Key: newprojectID
    // Value: newfileName[0] newfileID[1]
    Map<String, List<String>> newFileMap = JSON.cleanupNewFiles(jsons);

    int updates = 0;
    for (String key : newFileMap.keySet()) {
      if (!newFileMap.get(key).get(0).equals(currentFileMap.get(key).get(0))) {
        LOGGER.info(newFileMap.get(key).get(0));
        LOGGER.info("\t" + newFileMap.get(key).get(1));
        updates++;
      }
    }

    JSONObject manifest = Manifest.createManifest(USERGAMEVERSION, "forge-14.23.5.2836", "", "");
    manifest = Manifest.addFiles(newFileMap, manifest);

    JSONObject old = Manifest.createManifest(USERGAMEVERSION, "forge-14.23.5.2836", "", "");
    old = Manifest.addFiles(currentFileMap, old);

    LOGGER.info(manifest);
    LOGGER.info(old);

    long timeEnd = System.nanoTime();

    Logging.extraLogs(timeStart, timeEnd, updates);

  }

}
