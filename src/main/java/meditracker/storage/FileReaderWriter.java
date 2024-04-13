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
import meditracker.dailymedication.DailyMedicationManager;
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
    public static Path getFullPathComponent(Path path, boolean getFolder) {
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
     * Saves the daily medication information to a text file under a predefined sub-folder.
     * This sub-folder (relative to JSON file) can be found under `MediTrackerConfig`.
     *
     * @param suppliedDailyPath The DailyMedication file to save to. If null, the path will be built based on the
     *     default directory the JSON file resides in `MediTrackerConfig`.
     * @param dailyMedData A list of type String for the daily medication data.
     * @return `true` if successfully saved, `false` otherwise.
     */
    public static boolean saveDailyMedicationData(Path suppliedDailyPath, List<String> dailyMedData) {
        Path dailyMedSavePath;
        if (suppliedDailyPath == null) {
            dailyMedSavePath = MediTrackerConfig.getDailymedFilePath(null);
        } else {
            dailyMedSavePath = suppliedDailyPath;
        }

        if (dailyMedSavePath == null) {
            return false;
        }

        Path dailyMedFolder = getFullPathComponent(dailyMedSavePath, true);
        Path tmpSaveFile = createTempSaveFile(dailyMedFolder);
        if (tmpSaveFile == null) {
            return false;
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
            return false;
        }

        try {
            if (writeStatus) {
                Files.move(tmpSaveFile, dailyMedSavePath, REPLACE_EXISTING);
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
     * Loads all MediTracker related data.
     * This includes the JSON data for the Medications and the txt data for DailyMedications.
     *
     * @param jsonPath The Path of the json file. If null, will attempt to load from default location.
     */
    public static void loadMediTrackerData(Path jsonPath) {
        Path jsonFilePath;
        if (jsonPath == null) {
            jsonFilePath = MediTrackerConfig.getDefaultJsonSaveFilePath();
        } else {
            jsonFilePath = jsonPath;
        }

        JsonImporter.processMediTrackerJsonFile(jsonFilePath);

        Path dailyMedFilePath = MediTrackerConfig.getDailymedFilePath(jsonFilePath);
        loadDailyMedicationData(dailyMedFilePath);
    }

    /**
     * Loads the daily medication information from a text file under a predefined sub-folder.
     * This sub-folder name (relative to JSON file) can be found under `MediTrackerConfig`.
     *
     * @param dailyMedPath Path of the txt file containing the DailyMedication information.
     */
    private static void loadDailyMedicationData(Path dailyMedPath) {
        List<String> dailyMedData;
        try {
            dailyMedData = Files.readAllLines(dailyMedPath);
        } catch (IOException e) {
            MEDILOGGER.warning("IOException. Unable to Read Daily medication data.");
            dailyMedData = null;
        } catch (NullPointerException e) {
            MEDILOGGER.warning("Null path supplied. Unable to read daily medication data.");
            dailyMedData = null;
        }

        if (dailyMedData == null) {
            DailyMedicationManager.createDailyMedicationManager();
        } else {
            DailyMedicationManager.importDailyMedicationManager(dailyMedData);
        }
    }
}
