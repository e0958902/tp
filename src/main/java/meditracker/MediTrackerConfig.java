package meditracker;

import java.nio.file.Path;

import meditracker.storage.FileReaderWriter;
import meditracker.time.MediTrackerTime;

/**
 * A class that holds configuration related properties.
 * This is to allow the user to customise certain user preferences. (To be implemented beyond v2.1)
 */
public class MediTrackerConfig {
    /* Path to save Medication related information.*/
    private static final Path DEFAULT_JSON_SAVE_FILE_PATH = Path.of("data/MediTrackerData.json");

    /* Folder containing daily medication for the different days.*/
    /* Folder name is FIXED and should always be RELATIVE to the JSON save file.*/
    private static final String DAILY_SAVE_FOLDER_NAME = "dailymed";

    public static Path getDefaultJsonSaveFilePath() {
        return DEFAULT_JSON_SAVE_FILE_PATH;
    }

    /**
     * Gets the file name to save DailyMedication data.
     *
     * @return file name in the YYYY-MM-DD.txt format.
     */
    private static String getDailySaveFileName() {
        String date = MediTrackerTime.getCurrentDate().toString();
        return (date + ".txt");
    }

    /**
     * Returns the Path of the file to save the DailyMedication information to.
     * This path will be relative to the JSON file.
     *
     * @param jsonFilePath The path of the JSON file. If null, it will take the default JSON path.
     * @return Path of the save file (.txt) for the daily medication.
     */
    public static Path getDailymedFilePath(Path jsonFilePath) {
        Path jsonFolder;
        if (jsonFilePath == null) {
            jsonFolder = FileReaderWriter.getFullPathComponent(DEFAULT_JSON_SAVE_FILE_PATH, true);
        } else {
            jsonFolder = FileReaderWriter.getFullPathComponent(jsonFilePath, true);
        }

        if (jsonFolder == null) {
            return Path.of(DAILY_SAVE_FOLDER_NAME, getDailySaveFileName());
        } else {
            return Path.of(jsonFolder.toString(), DAILY_SAVE_FOLDER_NAME, getDailySaveFileName());
        }
    }
}
