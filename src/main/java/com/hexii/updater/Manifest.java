package com.hexii.updater;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Manifest {
    
    private Manifest() {}

    public static JSONObject createManifest(String gameVersion, String forgeVersion, String name, String version) {
	JSONObject manifest = new JSONObject();

	JSONObject minecraft = new JSONObject();

	JSONArray modLoaders = new JSONArray();
	JSONObject modLoadersInfo = new JSONObject();
	modLoadersInfo.put("id", forgeVersion);
	modLoadersInfo.put("primary", true);

	modLoaders.put(modLoadersInfo);

	minecraft.put("version", gameVersion);
	minecraft.put("modLoaders", modLoaders);

	manifest.put("minecraft", minecraft);
	manifest.put("version", version);
	manifest.put("manifestType", "minecraftModpack");
	manifest.put("manifestVersion", 1);
	manifest.put("name", name);
	

	return manifest;
    }
    
    public static JSONObject addFiles(Map<String, List<String>> map, JSONObject manifest) {
	
	JSONArray files = new JSONArray();
	
	for (Map.Entry<String, List<String>> entry: map.entrySet()) {
	    JSONObject newfile = new JSONObject();
	    newfile.put("required", true);
	    newfile.put("projectID", entry.getKey());
	    newfile.put("fileID", entry.getValue().get(2));
	    files.put(newfile);
	}
	
	manifest.put("files", files);
	
	return manifest;
	
    }

}
