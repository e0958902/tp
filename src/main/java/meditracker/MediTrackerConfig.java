package meditracker;

import java.nio.file.Path;
import java.util.logging.Logger;

import meditracker.logging.MediLogger;

/**
 * A class that holds configuration related properties.
 * This is to allow the user to customise certain user preferences. (To be implemented beyond v2.0)
 */
public class MediTrackerConfig {
    private static final Logger logger = MediLogger.getMediLogger();

    /* Path to save Medication related information.*/
    private static final Path defaultJsonSaveFilePath = Path.of("data/MediTrackerData.json");
    /* Path to save medication for the day. Not customisable.*/
    private static final Path defaultDailySaveFilePath = Path.of("data/dailymed/today.txt");

    public static Path getDefaultJsonSaveFilePath() {
        return defaultJsonSaveFilePath;
    }

    public static Path getDefaultDailySaveFilePath() {
        return defaultDailySaveFilePath;
    }
}
