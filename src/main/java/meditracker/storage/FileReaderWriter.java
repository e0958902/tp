package meditracker.storage;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardOpenOption.APPEND;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private static final Logger MEDILOGGER = MediLogger.getMediLogger();

    /**
     * Returns either the Path of folder or the Path of file.
     *
     * @param path The Path object containing both folder name and file name.
     * @param getFolder Whether to return the Path of folder or the Path of file.
     * @return The Path object of either the file name or the folder name. Null if the chosen field is empty.
     */
    static Path getFullPathComponent(Path path, boolean getFolder) {
        if (path == null) {
            return null;
        }

        if (getFolder) {
            return path.getParent();
        } else {
            return path.getFileName();
        }
    }

    /**
     * Creates new directories to allow writing of MediTracker data to the save files.
     *
     * @param folderName The directory, including its parents, to create. If null, the creation is skipped.
     * @throws FileReadWriteException When there is any issue creating the directories.
     */
    private static void initialiseDirectory(Path folderName) throws FileReadWriteException {
        if (folderName == null) {
            MEDILOGGER.info("Directory portion is null. Skipping creation.");
            return;
        }

        try {
            Files.createDirectories(folderName);
        } catch (IOException e) {
            throw new FileReadWriteException("An IO Error has occurred. Directories may not have been created.");
        } catch (SecurityException e) {
            throw new FileReadWriteException("Unable to create directories. Please make sure that the "
                    + "directory has the appropriate permissions for Meditracker to write to.");
        }
    }

    /**
     * Creates a temporary save file.
     * Also attempts to initialise the directory in case of a first-time save.
     *
     * @param dir The directory for the temporary save file. If null, it will be the directory of the project root.
     * @return The Path object of the save file. null if there is an issue with creating the save file.
     */
    static Path createTempSaveFile(Path dir) {
        Path directory;
        if (dir == null) {
            directory = Paths.get("");
        } else {
            directory = dir;
        }

        try {
            initialiseDirectory(directory);
        } catch (FileReadWriteException e) {
            MEDILOGGER.severe(e.getMessage());
            return null;
        }

        try {
            return Files.createTempFile(directory, null, null);
        } catch (IOException e) {
            MEDILOGGER.severe("IO Error: Unable to create temp save file");
            return null;
        } catch (SecurityException e) {
            MEDILOGGER.severe("Unable to create temp save file. Please make sure that "
                    + "the file has the appropriate permissions for MediTracker to write to.");
            return null;
        }
    }

    /**
     * Reads the JSON data file to load and populate the MediTracker.
     * If the file is not found, a warning will be thrown to alert the user, and the program
     * will run without the saved data (fresh state).
     */
    public static void loadMediTrackerData() {
        Path mediTrackerJsonPath = MediTrackerConfig.getDefaultJsonSaveFilePath();
        JsonImporter.processMediTrackerJsonFile(mediTrackerJsonPath);
    }

    /**
     * Saves the medication information in MediTracker.
     *
     * @param path The Path object (relative or absolute) to save the information to. If null, the path will be the
     *     path specified in `MediTrackerConfig`.
     * @return true if the saving is successful, false otherwise.
     */
    public static boolean saveMediTrackerData(Path path) {
        Path fullJsonPath;
        if (path == null) {
            fullJsonPath = MediTrackerConfig.getDefaultJsonSaveFilePath();
        } else {
            fullJsonPath = path;
        }

        Path jsonFolder = getFullPathComponent(fullJsonPath, true);
        Path tmpSaveFile = createTempSaveFile(jsonFolder);

        if (tmpSaveFile == null) {
            return false;
        }

        boolean saveStatus = JsonExporter.saveMedicationDataToJson(tmpSaveFile);
        try {
            if (saveStatus) {
                Files.move(tmpSaveFile, fullJsonPath, REPLACE_EXISTING);
            } else {
                Files.delete(tmpSaveFile);
            }
        } catch (IOException e) {
            MEDILOGGER.severe("IO Exception occurred when trying to update existing save file.");
            return false;
        }
        return true;
    }

    /**
     * Saves the daily medication information to a fixed file data/dailymed/today.txt.
     *
     * @param dailyMedData A list of type String for the daily medication data.
     */
    public static void saveDailyMedicationData(List<String> dailyMedData) {
        Path dailyMedFullSavePath = MediTrackerConfig.getDailySaveFilePath();
        Path dailyMedFolder = getFullPathComponent(dailyMedFullSavePath, true);
        Path tmpSaveFile = createTempSaveFile(dailyMedFolder);

        if (tmpSaveFile == null) {
            return;
        }

        //@@author annoy-o-mus-reused
        // Reused from https://stackoverflow.com/a/6548204
        // with minor modifications
        boolean writeStatus = false;
        try {
            for (String stringData : dailyMedData) {
                byte[] dataInBytes = (stringData + System.lineSeparator()).getBytes();
                Files.write(tmpSaveFile, dataInBytes, APPEND);
            }
            writeStatus = true;
        } catch (IOException e) {
            MEDILOGGER.severe("Unable to write DailyMedication data to file.");
        }

        try {
            if (writeStatus) {
                Files.move(tmpSaveFile, dailyMedFullSavePath, REPLACE_EXISTING);
            } else {
                Files.delete(tmpSaveFile);
            }
        } catch (IOException e) {
            MEDILOGGER.severe("IO Exception occurred when trying to update existing save file.");
        }
    }

    /**
     * Loads the daily medication information from a fixed file data/dailymed/today.txt.
     *
     * @return A list of string with the daily medication data. null if the file could not be loaded.
     */
    public static List<String> loadDailyMedicationData() {
        try {
            Path dailyMedTextFile = MediTrackerConfig.getDailySaveFilePath();
            return Files.readAllLines(dailyMedTextFile);
        } catch (IOException e) {
            MEDILOGGER.warning("Unable to Read Daily medication data. "
                    + "Daily medication data starting with clean state.");
            return null;
        }
    }
}
