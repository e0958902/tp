package meditracker.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

import meditracker.MediTrackerConfig;
import meditracker.exception.FileReadWriteException;
import meditracker.logging.MediLogger;

//@@author annoy-o-mus
/**
 * A static class to handle the reading and writing to the filesystem.
 */
public class FileReaderWriter {
    private static final Logger logger = MediLogger.getMediLogger();

    /**
     * Returns either the folder name or the file name of the path provided.
     *
     * @param path The path containing the folder name and file name.
     * @param getFolder Whether to return the folder name or the file name.
     * @return Either the file name or the folder name. Null if the chosen field is empty.
     */
    private static String getFullPathComponent(String path, boolean getFolder) {
        File f = new File(path);
        if (getFolder) {
            return f.getParent();
        } else {
            String fileName = f.getName();
            if (fileName.isEmpty()) {
                return null;
            }
            return fileName;
        }
    }

    /**
     * Creates new directories to allow writing of MediTracker data to the save file.
     *
     * @param folderName The directory, including its parents, to create. If null, the creation is skipped.
     * @throws FileReadWriteException When there is any issue creating the directories.
     */
    private static void initialiseDirectory(String folderName) throws FileReadWriteException {
        File directory = null;

        // Solution adapted from: https://stackoverflow.com/a/3634879
        try {
            directory = new File(folderName);
        } catch (NullPointerException e) {
            logger.info("Directory portion is null. Skipping creation.");
            return;
        }

        try {
            directory.mkdirs();
        } catch (SecurityException e) {
            throw new FileReadWriteException("Unable to create directories. Please make sure that the "
                    + "directory has the appropriate permissions for Meditracker to write to.");
        }
    }

    /**
     * Creates a JSON save file and writes.
     * Also attempts to reinitialise the directory in case of a first-time save.
     *
     * @return The File object that corresponds to the write file.
     * @throws FileReadWriteException If the file is unable to be created due to system issues.
     */
    static File createJsonSaveFile() throws FileReadWriteException {
        String fullFilePath = MediTrackerConfig.getFullJsonSaveFilePath();
        String folderPath = getFullPathComponent(fullFilePath,true);
        initialiseDirectory(folderPath);
        File fileToWrite = new File(fullFilePath);
        try {
            // TODO: Introduce a more robust way (rename, create then delete - Done by another function)
            // TODO: Also to take into account empty file for first run.
            fileToWrite.delete();
            fileToWrite.createNewFile();
            return fileToWrite;
        } catch (IOException e) {
            throw new FileReadWriteException("IO Error: Unable to write to JSON File");
        } catch (SecurityException e) {
            throw new FileReadWriteException("Unable to create save JSON file. Please make sure that "
                    + "the file has the appropriate permissions for MediTracker to write to.");
        }
    }

    /**
     * Reads the JSON file to load and populate the MediTracker.
     * If the file is not found, a warning will be thrown to alert the user, and the program
     * will execute without the saved data (fresh state).
     *
     */
    public static void loadMediTrackerData() {
        Path mediTrackerJsonPath = null;
        String jsonDataFilePath = MediTrackerConfig.getFullJsonSaveFilePath();

        try {
            // https://stackoverflow.com/a/20838298
            mediTrackerJsonPath = FileSystems.getDefault().getPath(jsonDataFilePath);
        } catch (InvalidPathException e) {
            logger.warning("Unable to find the file " + jsonDataFilePath + " to read from. "
                    + "Program will run with no data loaded.");
        }

        if (mediTrackerJsonPath != null) {
            JsonImporter.processMediTrackerJsonFile(mediTrackerJsonPath);
        }
    }

    /**
     * Saves the medication information found in `MedicationManager`.
     */
    public static void saveMediTrackerData() {
        try {
            File fileToWrite = createJsonSaveFile();
            JsonExporter.saveMedicationDataToJson(fileToWrite);
            //TODO: Delete the renamed save file by `createJsonSaveFile`
        } catch (FileReadWriteException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Saves the daily medication information to a fixed file data/dailymed/today.txt.
     *
     * @param dailyMedData A list of type String for the daily medication data.
     * @throws FileReadWriteException if there is an issue creating the file.
     */
    public static void saveDailyMedicationData(List<String> dailyMedData) throws FileReadWriteException {
        String dailyMedFullSavePath = MediTrackerConfig.getFullDailySaveFilePath();

        String dailyMedFolder = getFullPathComponent(dailyMedFullSavePath, true);
        initialiseDirectory(dailyMedFolder);

        // TODO: Refactor this part out as well.
        // This part is similar to `createJsonSaveFile()`
        File fileToWrite = new File(dailyMedFullSavePath);
        try {
            fileToWrite.delete();
            fileToWrite.createNewFile();
        } catch (IOException e) {
            throw new FileReadWriteException("IO Error: Unable to write to DailyMedication txt file");
        } catch (SecurityException e) {
            throw new FileReadWriteException("Unable to create save DailyMedication txt file. Please make sure that "
                    + "the file has the appropriate permissions for MediTracker to write to.");
        }

        //@@author annoy-o-mus-reused
        // Reused from https://stackoverflow.com/a/6548204
        // with minor modifications
        try {
            FileWriter f = new FileWriter(fileToWrite);
            for (String stringData : dailyMedData) {
                f.write(stringData + System.lineSeparator());
            }
            f.flush();
            f.close();
        } catch (IOException e) {
            logger.severe("Unable to write DailyMedication data to file.");
        }
    }

    /**
     * Loads the daily medication information from a fixed file data/dailymed/today.txt.
     *
     * @return A list of string with the daily medication data. null if the file could not be loaded.
     */
    public static List<String> loadDailyMedicationData() {
        try {
            Path dailyMedTextFile = FileSystems.getDefault()
                    .getPath(MediTrackerConfig.getFullDailySaveFilePath());
            return Files.readAllLines(dailyMedTextFile);
        } catch (IOException e) {
            logger.warning("Unable to Read Daily medication data. "
                    + "Daily medication data starting with clean state.");
            return null;
        }
    }
}
