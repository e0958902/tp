package meditracker;

import java.nio.file.Path;
import java.util.logging.Logger;

import meditracker.logging.MediLogger;

/**
 * A class that holds configuration related properties.
 * This is to allow the user to customise certain user preferences.
 */
public class MediTrackerConfig {
    private static final Logger logger = MediLogger.getMediLogger();

    /* Path to save Medication related information.*/
    private static Path fullJsonSaveFilePath = Path.of("data/MediTrackerData.json");
    /* Path to save medication for the day. Not customisable.*/
    private static Path fullDailySaveFilePath = Path.of("data/dailymed/today.txt");


    public static Path getFullJsonSaveFilePath() {
        return fullJsonSaveFilePath;
    }

    public static void setFullJsonSaveFilePath(String fullJsonSaveFilePath) {
        if (fullJsonSaveFilePath == null) {
            logger.warning("No JSON save path provided. Will not overwrite default save path.");
            return;
        }
        //Todo: CHECK VALIDITY. PROCESS THEM.
        //MediTrackerConfig.fullJsonSaveFilePath = fullJsonSaveFilePath;
    }

    public static Path getFullDailySaveFilePath() {
        return fullDailySaveFilePath;
    }
}
