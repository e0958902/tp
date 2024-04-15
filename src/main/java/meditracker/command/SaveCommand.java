package meditracker.command;

import java.nio.file.Path;
import java.util.Map;

import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.SaveArgument;
import meditracker.exception.ArgumentException;
import meditracker.exception.HelpInvokedException;
import meditracker.storage.FilePathChecker;
import meditracker.storage.FileReaderWriter;
import meditracker.storage.MediTrackerFileConfig;

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
     * @throws HelpInvokedException If the help message is invoked.
     * @throws ArgumentException If compulsory arguments are not found,
     *     or if any argument with a compulsory value is not found,
     *     or if there are duplicate arguments,
     *     or if an argument not supported by the command is found.
     */
    public SaveCommand(String arguments) throws HelpInvokedException, ArgumentException {
        if (arguments.isEmpty()) {
            parsedArguments = null;
        } else {
            parsedArguments = ARGUMENT_LIST.parse(arguments);
        }
    }

    /**
     * Saves MediTracker information to the default save location specified in `MediTrackerFileConfig`.
     * Includes the Medication data (in JSON) and DailyMedication data (in txt).
     */
    private void saveToDefaultLocation() {
        boolean isSaveSuccessful = FileReaderWriter.saveMedicationData(null);
        if (!isSaveSuccessful) {
            System.out.println("An error occurred while saving Medication data. Data is not saved.");
        } else {
            Path defaultJsonPath = MediTrackerFileConfig.getDefaultJsonSaveFilePath();
            Path absoluteJsonPath = defaultJsonPath.toAbsolutePath();
            System.out.println("Medication Data successfully saved to: " + absoluteJsonPath);
            System.out.println("Use the -o flag to save the data to another location.");
        }

        isSaveSuccessful = FileReaderWriter.saveDailyMedicationData(null);
        if (!isSaveSuccessful) {
            System.out.println("An error occurred while saving DailyMedication Data. Data is not saved.");
        } else {
            Path dailyMedPath = MediTrackerFileConfig.getDailymedFilePath(null);
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
        } else {
            Path absoluteJsonPath = jsonSaveFilePath.toAbsolutePath();
            System.out.println("Medication Data successfully saved to: " + absoluteJsonPath);
        }

        Path dailyMedPath = MediTrackerFileConfig.getDailymedFilePath(jsonSaveFilePath);
        isSaveSuccessful = FileReaderWriter.saveDailyMedicationData(dailyMedPath);
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
        Path pathOfJsonSaveFile = FilePathChecker.getValidatedUserPathArgument(saveFileLocation);
        if (pathOfJsonSaveFile != null) {
            saveToSpecifiedLocation(pathOfJsonSaveFile);
        }
    }
}
