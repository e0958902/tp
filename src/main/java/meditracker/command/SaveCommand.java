package meditracker.command;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import meditracker.MediTrackerConfig;
import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.SaveArgument;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.UnknownArgumentFoundException;
import meditracker.storage.FilePathChecker;
import meditracker.storage.FileReaderWriter;

/**
 * A class that handles the `save` command and its relevant arguments.
 */
public class SaveCommand extends Command {
    private static final ArgumentList ARGUMENT_LIST = new ArgumentList(new SaveArgument());
    public static final String HELP_MESSAGE = ArgumentHelper.getHelpMessage(CommandName.SAVE, ARGUMENT_LIST);
    private Map<ArgumentName, String> parsedArguments;

    /**
     * Parses the arguments associated with the `save` command.
     * If empty, the parsed arguments will receive `null`.
     *
     * @param arguments Associated arguments, if any.
     * @throws ArgumentNotFoundException If compulsory arguments are not found.
     * @throws ArgumentNoValueException If any argument with a compulsory value is not found.
     * @throws DuplicateArgumentFoundException If there are duplicate arguments.
     * @throws HelpInvokedException If the help message is invoked.
     * @throws UnknownArgumentFoundException If an argument not supported by the command is found.
     */
    public SaveCommand(String arguments)
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException {
        if (arguments.isEmpty()) {
            parsedArguments = null;
        } else {
            parsedArguments = ARGUMENT_LIST.parse(arguments);
        }
    }

    /**
     * Saves MediTracker information to the default save location specified in `MediTrackerConfig`.
     * Includes the Medication data (in JSON) and DailyMedication data (in txt).
     */
    private void saveToDefaultLocation() {
        boolean isSaveSuccessful = FileReaderWriter.saveMedicationData(null);
        if (!isSaveSuccessful) {
            System.out.println("An error occurred while saving Medication data. Data is not saved.");
            return;
        } else {
            Path defaultJsonPath = MediTrackerConfig.getDefaultJsonSaveFilePath();
            Path absoluteJsonPath = defaultJsonPath.toAbsolutePath();
            System.out.println("Medication Data successfully saved to: " + absoluteJsonPath);
            System.out.println("Use the -o flag to save the data to another location.");
        }

        List<String> dailyMedData = DailyMedicationManager.getDailyMedicationStringData();
        isSaveSuccessful = FileReaderWriter.saveDailyMedicationData(null, dailyMedData);
        if (!isSaveSuccessful) {
            System.out.println("An error occurred while saving DailyMedication Data. Data is not saved.");
        } else {
            Path dailyMedPath = MediTrackerConfig.getDailymedFilePath(null);
            Path absoluteDailyMedPath = dailyMedPath.toAbsolutePath();
            System.out.println("DailyMedication Data successfully saved to: " + absoluteDailyMedPath);
        }
    }

    /**
     * Saves MediTracker information to the specified location.
     * Includes the Medication data (in JSON) and DailyMedication data (in txt).
     * The DailyMedication data will be saved relative to the JSON file.
     *
     * @param jsonSaveFilePath The location of the JSON file to save to.
     */
    private void saveToSpecifiedLocation(Path jsonSaveFilePath) {
        boolean isSaveSuccessful = FileReaderWriter.saveMedicationData(jsonSaveFilePath);
        if (!isSaveSuccessful) {
            System.out.println("An error occurred while saving Medication Data. Data is not saved.");
            return;
        } else {
            Path absoluteJsonPath = jsonSaveFilePath.toAbsolutePath();
            System.out.println("Data successfully saved to: " + absoluteJsonPath);
        }

        List<String> dailyMedData = DailyMedicationManager.getDailyMedicationStringData();
        Path dailyMedPath = MediTrackerConfig.getDailymedFilePath(jsonSaveFilePath);
        isSaveSuccessful = FileReaderWriter.saveDailyMedicationData(dailyMedPath, dailyMedData);
        if (!isSaveSuccessful) {
            System.out.println("An error occurred while saving DailyMedication Data. Data is not saved.");
        } else {
            Path absoluteDailyMedPath = dailyMedPath.toAbsolutePath();
            System.out.println("DailyMedication Data successfully saved to: " + absoluteDailyMedPath);
        }
    }

    /**
     * Executes the `save` command.
     */
    @Override
    public void execute() {
        if (parsedArguments == null) {
            saveToDefaultLocation();
            return;
        }
        assert (parsedArguments != null);

        String saveFileLocation = parsedArguments.get(ArgumentName.SAVE_FILE);
        Path pathOfJsonSaveFile = FilePathChecker.validateUserPathArgument(saveFileLocation);
        if (pathOfJsonSaveFile != null) {
            saveToSpecifiedLocation(pathOfJsonSaveFile);
        }
    }
}
