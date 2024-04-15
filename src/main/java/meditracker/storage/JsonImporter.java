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

//@@author annoy-o-mus
/**
 * A class to handle the importing of raw json file data.
 * Passes the intermediate data to MedicationManager to process.
 */
class JsonImporter {
    private static final Logger MEDILOGGER = MediLogger.getMediLogger();

    /**
     * Converts information from JSONArray into a List of (String, String) mappings.
     * The JSONArray should contain purely JSONObjects (with no additional nesting),
     *     and each JSONObject should contain purely keys of type String and values of type String.
     *
     * @param jsonArray A JSONArray of JSONObjects containing key-value pairs of type (String, String).
     * @return A list of (String, String) key-value pairs. May contain only partial entries if there are entries not
     *     in the specified type.
     */
    private static List<Map<String, String>> convertJsonArrayToStringMap(JSONArray jsonArray) {
        // Solution adapted from
        // https://stackoverflow.com/questions/32823746/java-code-to-convert-json-into-text-in-key-value-pair
        int numberOfElements = jsonArray.length();
        List<Map<String, String>> list = new ArrayList<>();

        for (int i = 0; i < numberOfElements; i++) {
            Map<String, String> kvMap = new HashMap<>();
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = jsonObject.get(key).toString();
                    kvMap.put(key, value);
                }
                list.add(kvMap);
            } catch (JSONException e) {
                MEDILOGGER.warning("JSONArray to List<Map<String,String>> Error: " + e.getMessage());
                MEDILOGGER.warning("Entry skipped.");
                continue;
            }
        }
        return list;
    }

    /**
     * Loads from the file and returns JSON data as a raw string.
     * The JSON file should only contain one line of data. Any additional lines in the file will be ignored.
     *
     * @param jsonPath The Path object specifying the path to the JSON save data.
     * @return The JSON data as a String object. Null if the data cannot be loaded or is empty.
     */
    private static String loadRawJsonFileData(Path jsonPath) {
        List<String> jsonFileData = null;

        // Choice of reader adapted from
        // https://www.stackchief.com/blog/FileReader%20vs%20BufferedReader%20vs%20Scanner%20%7C%20Java
        // and https://stackoverflow.com/a/20838298
        try {
            jsonFileData = Files.readAllLines(jsonPath);
        } catch (IOException e) {
            MEDILOGGER.warning("Unable to read from the JSON save file.");
            return null;
        }

        if (jsonFileData.isEmpty()) {
            MEDILOGGER.warning("Empty JSON file.");
            return null;
        }

        if (jsonFileData.size() > 1) {
            MEDILOGGER.warning("Multiple lines detected. JSON file should only contain one line of data. "
                    + "Only the first line will be loaded. Ignoring other lines.");
        }
        return jsonFileData.get(0);
    }


    /**
     * Reads from the JSON file and sends MedicationManager the required information.
     * If the JSON file could not be found or if the structure is corrupted and could not be read,
     *     a warning will be thrown to the user and the program will run with a clean state.
     *
     * @param medicationJsonPath The Path object specifying the path to the JSON save data.
     */
    static void processMedicationJsonFile(Path medicationJsonPath) {
        if (medicationJsonPath == null) {
            MEDILOGGER.warning("No path specified to read the JSON file.");
            return;
        }

        String jsonStringData = loadRawJsonFileData(medicationJsonPath);
        if (jsonStringData == null) {
            MEDILOGGER.warning("Empty JSON file.");
            return;
        }

        // Solution on reading and parsing a JSON file adapted from
        // https://stackoverflow.com/q/10926353
        MEDILOGGER.info("Reading JSON file...");
        JSONArray medicationList = null;
        try {
            JSONObject rawJsonData = new JSONObject(jsonStringData);
            medicationList = rawJsonData.getJSONArray("medicationList");
        } catch (JSONException e) {
            MEDILOGGER.warning("JSON Read Error: " + e.getMessage());
            MEDILOGGER.warning("JSON Save Data not read and processed.");
            return;
        }

        MEDILOGGER.info("JSON file Read. Loading Medication data...");
        List<Map<String, String>> medicationStringMap = convertJsonArrayToStringMap(medicationList);
        MedicationManager.addMedicationFromSaveFile(medicationStringMap);
    }
}
