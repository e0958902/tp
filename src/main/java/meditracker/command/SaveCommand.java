package meditracker.command;

import java.nio.file.Path;
import java.util.Map;

import meditracker.MediTrackerConfig;
import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.SaveArgument;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.storage.FilePathChecker;
import meditracker.storage.FileReaderWriter;

/**
 * The Save Command.
 */
public class SaveCommand extends Command {
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
