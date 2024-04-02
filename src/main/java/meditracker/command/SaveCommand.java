package meditracker.command;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;
import java.util.logging.Logger;

import meditracker.MediTrackerConfig;
import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.SaveArgument;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.logging.MediLogger;
import meditracker.storage.FilePathChecker;
import meditracker.storage.FileReaderWriter;

/**
 * The Save Command.
 */
public class SaveCommand extends Command {
    private static final Logger MEDILOGGER = MediLogger.getMediLogger();

    private static final ArgumentList ARGUMENT_LIST = new ArgumentList(new SaveArgument());
    public static final String HELP_MESSAGE = ArgumentHelper.getHelpMessage(CommandName.SAVE, ARGUMENT_LIST);
    private final Map<ArgumentName, String> parsedArguments;

    public SaveCommand(String arguments)
            throws DuplicateArgumentFoundException, HelpInvokedException, ArgumentNotFoundException {
        if (arguments.isEmpty()) {
            parsedArguments = null;
        } else {
            parsedArguments = ARGUMENT_LIST.parse(arguments);
        }
    }

    /**
     * Saves MediTracker information as JSON at its default save location specified in MediTrackerConfig.
     */
    private void saveJsonToDefaultLocation(){
        boolean isSaveSuccessful = FileReaderWriter.saveMediTrackerData(null);
        if (!isSaveSuccessful) {
            System.out.println("An error occurred while saving. Data is not saved.");
        } else {
            Path defaultJsonPath = MediTrackerConfig.getDefaultJsonSaveFilePath();
            Path absoluteJsonPath = defaultJsonPath.toAbsolutePath();
            System.out.println("Data successfully saved to: " + absoluteJsonPath);
            System.out.println("Use the -o flag to save the data to another location.");
        }
    }

    /**
     * Saves MediTracker information as JSON at the specified path.
     *
     * @param saveFilePath The location to save to.
     */
    private void saveJsonToSpecifiedLocation(Path saveFilePath) {
        boolean isSaveSuccessful = FileReaderWriter.saveMediTrackerData(saveFilePath);
        if (!isSaveSuccessful) {
            System.out.println("An error occurred while saving. Data is not saved.");
        } else {
            Path absoluteJsonPath = saveFilePath.toAbsolutePath();
            System.out.println("Data successfully saved to: " + absoluteJsonPath);
        }
    }

    /**
     * Validates the path input and return the Path object if successfully validated.
     *
     * @return The Path object corresponding to the argument for the save file if it passes validation checks.
     * null otherwise.
     */
    private Path validateUserPathArgument() {
        String saveFileLocation = parsedArguments.get(ArgumentName.SAVE_FILE);
        boolean hasIllegalCharacters = FilePathChecker.containsIllegalCharacters(saveFileLocation);
        if (hasIllegalCharacters) {
            System.out.println("The supplied input contains potentially illegal characters. Please ensure that "
                    + "the supplied path does not have illegal character");
            return null;
        }

        Path pathOfSaveFile;
        try {
            pathOfSaveFile = Path.of(saveFileLocation);
        } catch (InvalidPathException e) {
            MEDILOGGER.severe(e.getMessage());
            System.out.println("Unable to convert input into Path object. Data is not saved.");
            return null;
        }

        boolean isValidFilePath = FilePathChecker.isValidFullPath(pathOfSaveFile);
        if (!isValidFilePath) {
            System.out.println("Path contains invalid folder names or missing valid file extension (.json).");
            System.out.println("Please ensure the path contains valid folder names and ends with .json");
            return null;
        }

        return pathOfSaveFile;
    }

    /**
     * Executes the `save` command.
     */
    @Override
    public void execute() {
        if (parsedArguments == null) {
            saveJsonToDefaultLocation();
            return;
        }
        assert (parsedArguments != null);

        Path pathOfSaveFile = validateUserPathArgument();
        if (pathOfSaveFile != null) {
            saveJsonToSpecifiedLocation(pathOfSaveFile);
        }
    }
}
