package meditracker.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import meditracker.argument.ArgumentName;
import meditracker.logging.MediLogger;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;

//@@author annoy-o-mus
/**
 * A class that converts Medication data to JSON and writes to the target file.
 */
class JsonExporter {
    private static final Logger MEDILOGGER = MediLogger.getMediLogger();

    /**
     * Converts the information inside a Medication object into a JSON Object.
     *
     * @param medInfo The Medication object.
     * @return The JSON object representing the Medication object.
     */
    private static JSONObject convertMedicationInfoToJsonObject(Medication medInfo) {
        JSONObject medObject = new JSONObject();
        try {
            medObject.put(ArgumentName.NAME.value, medInfo.getName());
            medObject.put(ArgumentName.EXPIRATION_DATE.value, medInfo.getExpiryDate());
            medObject.put(ArgumentName.REMARKS.value, medInfo.getRemarks());

            medObject.put(ArgumentName.QUANTITY.value, medInfo.getQuantity());
            medObject.put(ArgumentName.DOSAGE_MORNING.value, medInfo.getDosageMorning());
            medObject.put(ArgumentName.DOSAGE_AFTERNOON.value, medInfo.getDosageAfternoon());
            medObject.put(ArgumentName.DOSAGE_EVENING.value, medInfo.getDosageEvening());
            medObject.put(ArgumentName.REPEAT.value, medInfo.getRepeat());
            medObject.put(ArgumentName.DAY_ADDED.value, medInfo.getDayAdded());
        } catch (JSONException e) {
            MEDILOGGER.severe(e.getMessage());
            MEDILOGGER.severe("Entry not saved to JSON file.");
            return null;
        }
        return medObject;
    }

    /**
     * Populates the JSON array with Medication information in JSON form.
     *
     * @return JSON array containing a list of medication in JSON form.
     */
    private static JSONArray populateJsonMedicationList() {
        JSONArray medicationList = new JSONArray();
        int numberOfMedication = MedicationManager.getTotalMedications();
        // Start with 1 since the `getMedication` method will be converting from 1-based to 0-based
        for (int i = 1; i <= numberOfMedication; i++) {
            Medication medicationInfo = MedicationManager.getMedication(i);
            JSONObject medObject = convertMedicationInfoToJsonObject(medicationInfo);
            medicationList.put(medObject);
        }
        return medicationList;
    }

    /**
     * Writes the JSON object (starting with the root) to specified file.
     *
     * @param root The JSON object that contains all the JSON data.
     * @param fileToWrite The Path object containing the abstract pathname of the JSON file.
     * @return `true` if the data has been written to successfully, `false` otherwise.
     */
    private static boolean writeJsonData(JSONObject root, Path fileToWrite) {
        try {
            Files.write(fileToWrite, root.toString().getBytes());
            return true;
        } catch (IOException e) {
            MEDILOGGER.severe("Unable to write data to JSON file.");
            return false;
        }
    }

    /**
     * Save all the medication information into a JSON file.
     *
     * @param fileToWrite The Path object containing the abstract pathname of the JSON file to write to.
     * @return `true` if the information is saved successfully, `false` otherwise.
     */
    static boolean saveMedicationDataToJson(Path fileToWrite) {
        // Solution on how to write to a JSON file is adapted from:
        // https://stackoverflow.com/a/62947435
        // and https://javadoc.io/doc/org.json/json/latest/org/json/JSONObject.html
        // and https://javadoc.io/doc/org.json/json/latest/org/json/JSONArray.html

        JSONObject rootData = new JSONObject();
        rootData.put("version", 1.0);

        JSONArray medicationList = populateJsonMedicationList();
        rootData.put("medicationList", medicationList);

        return writeJsonData(rootData, fileToWrite);
    }
}
