package meditracker.storage;

import static java.nio.file.StandardOpenOption.APPEND;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

import meditracker.dailymedication.DailyMedicationManager;
import meditracker.logging.MediLogger;

/**
 * A package-private class that processes DailyMedication data and writes to the target file.
 */
class DailyMedicationExporter {
    private static final Logger MEDILOGGER = MediLogger.getMediLogger();

    /**
     * Writes DailyMedication data to target file.
     *
     * @param saveFile The Path of the file to save to.
     * @return `true` if the saving succeeded, `false` otherwise.
     */
    static boolean writeDailyMedicationToFile(Path saveFile) {
        List<String> dailyMedData = DailyMedicationManager.getDailyMedicationStringData();

        //@@author annoy-o-mus-reused
        // Reused from https://stackoverflow.com/a/6548204
        // with minor modifications
        try {
            for (String stringData : dailyMedData) {
                byte[] dataInBytes = (stringData + System.lineSeparator()).getBytes();
                Files.write(saveFile, dataInBytes, APPEND);
            }
            return true;
        } catch (IOException e) {
            MEDILOGGER.severe("Unable to write DailyMedication data to file.");
            return false;
        }
    }
}
