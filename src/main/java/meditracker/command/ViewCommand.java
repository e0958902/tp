package meditracker.command;

import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.ListIndexArgument;
import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.UnknownArgumentFoundException;
import meditracker.medication.MedicationManager;

import java.util.Map;

/**
 * The ViewCommand class represents a command to print an existing medication.
 * It extends the Command class.
 */
public class ViewCommand extends Command {
    public static final ArgumentList ARGUMENT_LIST = new ArgumentList(
            new ListIndexArgument(false)
    );
    public static final String HELP_MESSAGE = ArgumentHelper.getHelpMessage(CommandName.VIEW, ARGUMENT_LIST);
    private final Map<ArgumentName, String> parsedArguments;

    public ViewCommand(String arguments)
            throws ArgumentNotFoundException, DuplicateArgumentFoundException, HelpInvokedException,
            ArgumentNoValueException, UnknownArgumentFoundException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
    }

    /**
     * Executes the view command.
     * This method parses the input index and get the medication in the medication list
     * it then prints the medication name with its fields from the medication list
     */
    @Override
    public void execute() {
        String listIndexString = parsedArguments.get(ArgumentName.LIST_INDEX);
        int listIndex = Integer.parseInt(listIndexString);

        MedicationManager.printSpecificMedication(listIndex);
    }
}
