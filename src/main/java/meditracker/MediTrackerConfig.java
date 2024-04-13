package meditracker;

import java.nio.file.Path;

import meditracker.time.MediTrackerTime;

/**
 * A class that holds configuration related properties.
 * This is to allow the user to customise certain user preferences. (To be implemented beyond v2.0)
 */
public class MediTrackerConfig {
    /* Path to save Medication related information.*/
    private static final Path defaultJsonSaveFilePath = Path.of("data/MediTrackerData.json");
    /* Folder containing save medication for the different days. Not customisable.*/
    private static final Path dailySaveFolderPath = Path.of("data/dailymed");

    public static Path getDefaultJsonSaveFilePath() {
        return defaultJsonSaveFilePath;
    }

    public static Path getDailySaveFilePath() {
        String date = MediTrackerTime.getCurrentDate().toString();
        String folderPathString = dailySaveFolderPath.toString();
        return Path.of(folderPathString, date + ".txt");
    }
}
