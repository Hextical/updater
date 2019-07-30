package com.hexii.updater;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSON {

    private static String currentfileName = null;
    private static String currentprojectID = null;
    private static String currentfileID = null;
    private static String currentdownloadURL = null;

    // public static final String userPreferredReleaseType = "Release";

    // https://addons-ecs.forgesvc.net/api/v2/addon/{fileID}/files

    public static LinkedHashMap<String, List<String>> currentFileInfo(List<JSONObject> jsonObjectsTS) {

	LinkedHashMap<String, List<String>> currentFileMap = new LinkedHashMap<String, List<String>>();

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
	    
	    List<String> namePlusID = new ArrayList<String>();
	    namePlusID.add(0, currentfileName);
	    namePlusID.add(1, currentdownloadURL);
	    namePlusID.add(2, currentfileID);

	    currentFileMap.put(currentprojectID, namePlusID);

	}

	return currentFileMap;

    }

    public static ArrayList<JSONObject> removeIrrelevantGameVersions(JSONArray ja, String userGameVersion) {

	ArrayList<JSONObject> newList = new ArrayList<JSONObject>();

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

    public static JSONObject findBestFile(ArrayList<JSONObject> joAL) {

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
