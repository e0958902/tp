package meditracker.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import meditracker.logging.MediLogger;
import meditracker.medication.MedicationManager;

/**
 * A class to handle the importing of raw json file data and process them.
 * Passes the data in an intermediate format to the various Managers involved for them to initialise.
 */
public class JsonImporter {
    private static Logger logger = MediLogger.getMediLogger();

    /**
     * Converts information from JSONArray into a List of <String, String> mappings.
     * The JSONArray should contain purely JSONObjects, and each JSONObject should contain purely
     * keys of type String and values of type String.
     *
     * @param jsonArray A JSONArray of JSONObjects containing key-value pairs of type <String, String>.
     * @return A list of <String, String> key-value pairs. May contain only partial entries if there are entries not
     * in the specified type.
     */
    private static List<Map<String, String>> convertJsonArrayToStringMap(JSONArray jsonArray) {
        // Solution adapted from
        // https://stackoverflow.com/questions/32823746/java-code-to-convert-json-into-text-in-key-value-pair
        int numberOfElements = jsonArray.length();
        List<Map<String, String>> list = new ArrayList<>();

        for (int i = 0; i < numberOfElements; i ++ ) {
            Map<String,String> kvMap = new HashMap<>();
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = jsonObject.get(key).toString();
                    kvMap.put(key,value);
               }
                list.add(kvMap);
            } catch (JSONException e) {
                logger.warning("JSONArray to List<Map<String,String>> Error: " + e.getMessage());
                logger.warning("Entry skipped.");
                continue;
            }
        }
        return list;
    }

    /**
     * Loads the file and returns JSON data as a raw string.
     * The JSON file should only contain one line of data. Any additional lines in the file will be ignored.
     *
     * @param mediTrackerJsonPath The Path object specifying the path to the MediTracker save data.
     * @return The JSON data as a String object. Null if the data cannot be loaded or is empty.
     */
    private static String loadRawJsonFileData(Path mediTrackerJsonPath) {
        List<String> jsonFileData = null;

        // Choice of reader adapted from
        // https://www.stackchief.com/blog/FileReader%20vs%20BufferedReader%20vs%20Scanner%20%7C%20Java
        // and https://stackoverflow.com/a/20838298
        try {
            jsonFileData = Files.readAllLines(mediTrackerJsonPath);
        } catch (IOException e) {
            logger.warning("Unable to read from the JSON save file. Defaulting to empty state.");
            return null;
        }

        if (jsonFileData.isEmpty()) {
            logger.warning("Empty JSON file.");
            return null;
        }

        if (jsonFileData.size() > 1) {
            logger.warning("Multiple lines detected. JSON file should only contain one line of data. "
                    + "Only the first line will be loaded. Ignoring other lines.");
        }
        return jsonFileData.get(0);
    }


    /**
     * Reads from the JSON file and populates the various Managers with various information.
     * If the JSON file could not be found or if the structure is corrupted and could not be read,
     * a warning will be thrown to the user and the program will run as if it is the first time running.
     *
     * @param mediTrackerJsonPath The Path object specifying the path to the MediTracker save data.
     * @param medManager The instance of MedicationManager.
     */
    static void processMediTrackerJsonFile (Path mediTrackerJsonPath, MedicationManager medManager) {
        String jsonStringData = loadRawJsonFileData(mediTrackerJsonPath);
        if (jsonStringData == null) {
            return;
        }

        // Solution on reading and parsing a JSON file adapted from
        // https://stackoverflow.com/q/10926353
        JSONArray medicationList = null;
        try {
            JSONObject rawJsonData = new JSONObject(jsonStringData);
            medicationList = rawJsonData.getJSONArray("medicationList");
        } catch (JSONException e) {
            logger.warning("JSON Read Error: " + e.getMessage());
            logger.warning("JSON Save Data not read and processed. Going with Empty state.");
            return;
        }

        List<Map<String,String>> medicationStringMap = convertJsonArrayToStringMap(medicationList);
    }
}
