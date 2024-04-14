package meditracker.command;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.LoadArgument;
import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.UnknownArgumentFoundException;
import meditracker.storage.FilePathChecker;
import meditracker.storage.FileReaderWriter;
import meditracker.ui.Ui;

/**
 * A class that handles the `load` command and its relevant arguments.
 */
public class LoadCommand extends Command {
    private static final ArgumentList ARGUMENT_LIST = new ArgumentList(new LoadArgument());
    public static final String HELP_MESSAGE = ArgumentHelper.getHelpMessage(CommandName.LOAD, ARGUMENT_LIST);
    private Map<ArgumentName, String> parsedArguments;

    public LoadCommand(String arguments)
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
    }

    /**
     * Confirms that the user wants to overwrite existing Meditracker data with data from a JSON file.
     *
     * @return true if the user confirms to overwrite, false otherwise.
     */
    private boolean confirmUserOverwrite() {
        System.out.println("Confirm overwrite existing MediTracker data with the file specified. (y/N)");
        String input = Ui.readCommand();
        return input.equalsIgnoreCase("y");
    }

    @Override
    public void execute() {
        assert (parsedArguments != null);

        String loadFileLocation = parsedArguments.get(ArgumentName.LOAD_FILE);
        Path pathOfJsonLoadFile = FilePathChecker.validateUserPathArgument(loadFileLocation);
        if (pathOfJsonLoadFile == null) {
            return;
        }

        boolean fileExists = Files.exists(pathOfJsonLoadFile);
        if (!fileExists) {
            System.out.println("The provided file does not exist");
            System.out.println("Full path of provided input: " + pathOfJsonLoadFile.toAbsolutePath());
            return;
        }

        if (!confirmUserOverwrite()) {
            return;
        }

        FileReaderWriter.loadMediTrackerData(pathOfJsonLoadFile);
    }
}
