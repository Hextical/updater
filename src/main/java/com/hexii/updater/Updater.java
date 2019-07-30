package com.hexii.updater;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class Updater {

    private static final Logger log = LogManager.getLogger(Updater.class);

    public static void main(String[] args) throws IOException {

	log.info("Starting execution of program...");

	// Duration of program execution
	long timeStart = System.nanoTime();

	// Path & Game Version
	final String userPath = "C:\\Users\\hexii\\Documents\\MultiMC\\instances\\1.12.2\\.minecraft\\mods";
	final String userGameVersion = "1.12.2";

	// All hashes of JARs
	List<Long> hashes = FolderOperations.folderToHash(userPath);

	// Contains the whole JSONObject of given hash (file) fingerprint
	List<JSONObject> jsonObject = Connections.connectWithHash(hashes);

	// Key: currentprojectID
	// Value: currentfileName[0] currentfileID[1]
	LinkedHashMap<String, List<String>> currentFileMap = JSON.currentFileInfo(jsonObject);

	Map<String, JSONArray> JSONS = Connections.connectWithProjectID(currentFileMap);
	Map<String, List<String>> newMap = new HashMap<String, List<String>>();

	List<String> brokenProjectIDs = Collections.synchronizedList(new ArrayList<String>());

	for (String key : JSONS.keySet()) {
	    ArrayList<JSONObject> removedstuff = JSON.removeIrrelevantGameVersions(JSONS.get(key), userGameVersion);

	    if (!removedstuff.toString().equals("[]")) {
		JSONObject bestFile = JSON.findBestFile(removedstuff);
		String filename = bestFile.get("fileName").toString();
		String downloadURL = bestFile.get("downloadUrl").toString();
		String fileID = bestFile.get("id").toString();
		List<String> stuff = new ArrayList<String>();
		stuff.add(0, filename);
		stuff.add(1, downloadURL);
		stuff.add(2, fileID);
		newMap.put(key, stuff);
	    } else {
		System.out.println(key + " is broken/unavailable.");
		brokenProjectIDs.add(key);
	    }
	}

	int updates = 0;
	for (String key : newMap.keySet()) {
	    if (!newMap.get(key).get(0).equals(currentFileMap.get(key).get(0))) {
		log.info(newMap.get(key).get(0));
		log.info("\t" + newMap.get(key).get(1));
		updates++;
	    }
	}

	JSONObject manifest = Manifest.createManifest("1.12.2", "forge-14.23.5.2836", "", "");
	manifest = Manifest.addFiles(newMap, manifest);
	
	JSONObject old = Manifest.createManifest("1.12.2", "forge-14.23.5.2836", "", "");
	old = Manifest.addFiles(currentFileMap, old);
	
	
	log.info(manifest);
	log.info(old);

	long timeEnd = System.nanoTime();

	Logging.extraLogs(timeStart, timeEnd, updates);

    }

}
