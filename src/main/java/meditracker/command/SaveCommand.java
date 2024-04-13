package meditracker.command;

import java.nio.file.Path;
import java.util.Map;

import meditracker.MediTrackerConfig;
import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.SaveArgument;
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
     * Saves MediTracker information as JSON at its default save location specified in MediTrackerConfig.
     */
    private void saveJsonToDefaultLocation() {
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
     * Executes the `save` command.
     */
    @Override
    public void execute() {
        if (parsedArguments == null) {
            saveJsonToDefaultLocation();
            return;
        }
        assert (parsedArguments != null);

        String saveFileLocation = parsedArguments.get(ArgumentName.SAVE_FILE);
        Path pathOfSaveFile = FilePathChecker.validateUserPathArgument(saveFileLocation);
        if (pathOfSaveFile != null) {
            saveJsonToSpecifiedLocation(pathOfSaveFile);
        }
    }
}
