package com.hexii.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

// For documentation on the Twitch API check out: https://twitchappapi.docs.apiary.io/

public final class Connections {
  
  private static final Logger LOGGER = LogManager.getLogger(Connections.class);
  
  private Connections() {}

  // StackOverflow
  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  /*
   * Connects to the Twitch API with the MurmurHash2 32-bit and retrieves the fingerprint via POST.
   */
  public static List<JSONObject> connectWithHash(Collection<Long> hashes) {

    LOGGER.info("Attempting to connect to the Twitch API with all of the hashes...");

    List<JSONObject> jsonObject = Collections.synchronizedList(new ArrayList<JSONObject>());

    hashes.parallelStream().forEach(hash -> {
      try {

        URL url = new URL("https://addons-ecs.forgesvc.net/api/v2/fingerprint");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        // Body
        final String jsonHashString = "[" + hash + "]";

        // Request sent
        OutputStreamWriter out =
            new OutputStreamWriter(con.getOutputStream(), StandardCharsets.UTF_8);
        out.write(jsonHashString);
        out.close();

        // Request received
        BufferedReader in =
            new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

        final String jsonText = readAll(in);
        final JSONObject json = new JSONObject(jsonText);

        jsonObject.add(json);

        in.close();
        con.disconnect();

      } catch (IOException e) {
        LOGGER.error("Something went wrong connecting with the hash: " + hash);
        LOGGER.error("Error: " + e);
      }

    });

    LOGGER.info("Hash connections complete.");

    return jsonObject;

  }

  /**
   * Connects to the API with the projectID {addonID} via GET and retrieves the JSONArray which is
   * then stored inside a List<JSONArray> which contains all the files of that projectID. Exact link
   * is: https://addons-ecs.forgesvc.net/api/v2/addon/{addonID}/files Fun fact: One can utilize the
   * projectID in a browser by navigating to: https://www.curseforge.com/projects/{addonID}
   */
  public static Map<String, JSONArray> connectWithProjectID(Map<String, List<String>> projectIDs) {

    LOGGER.info("Attempting to connect to the Twitch API with the projectIDs...");

    Map<String, JSONArray> mapTS = new ConcurrentHashMap<>();

    projectIDs.entrySet().parallelStream().forEach(entry -> {

      if (entry != null) {

        try {

          URL url =
              new URL("https://addons-ecs.forgesvc.net/api/v2/addon/" + entry.getKey() + "/files");

          HttpURLConnection con = (HttpURLConnection) url.openConnection();

          BufferedReader in = new BufferedReader(
              new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

          final String jsonText = readAll(in);
          final JSONArray json = new JSONArray(jsonText);

          mapTS.put(entry.getKey(), json);

          in.close();
          con.disconnect();

        } catch (IOException e) {
          LOGGER.error("Something went wrong connecting with the projectID: " + entry.getKey());
          LOGGER.error("Error: " + e);
        }

      }

    });

    LOGGER.info("projectID connections complete.");

    return mapTS;

  }

}
