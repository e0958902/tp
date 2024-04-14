package meditracker.command;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.LoadArgument;
import meditracker.exception.ArgumentException;
import meditracker.exception.HelpInvokedException;
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

    /**
     * Parses the arguments associated with the `load` command.
     *
     * @param arguments Associated arguments, if any.
     * @throws HelpInvokedException When help argument is used
     * @throws ArgumentException If compulsory arguments are not found,
     *              or if any argument with a compulsory value is not found,
     *              or if there are duplicate arguments,
     *              or if an argument not supported by the command is found.
     */
    public LoadCommand(String arguments) throws HelpInvokedException, ArgumentException {
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
