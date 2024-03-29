package meditracker.command;

import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.ListIndexArgument;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.medication.MedicationManager;
import meditracker.ui.Ui;

import java.util.Map;

/**
 * The DeleteCommand class represents a command to delete an existing medication.
 * It extends the Command class.
 */
public class DeleteCommand extends Command {
    public static final ArgumentList ARGUMENT_LIST = new ArgumentList(
            new ListIndexArgument(false)
    );
    public static final String HELP_MESSAGE = ArgumentHelper.getHelpMessage(CommandName.DELETE, ARGUMENT_LIST);
    private final Map<ArgumentName, String> parsedArguments;

    /**
     * Constructs a DeleteCommand object with the specified arguments.
     *
     * @param arguments The arguments containing medication information to be parsed.
     * @throws ArgumentNotFoundException Argument flag specified not found
     * @throws DuplicateArgumentFoundException Duplicate argument flag found
     * @throws HelpInvokedException When help argument is used or help message needed
     */
    public DeleteCommand(String arguments)
            throws ArgumentNotFoundException, DuplicateArgumentFoundException, HelpInvokedException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
    }

    /**
     * Executes the delete command.
     * This method deletes an existing Medication object using the provided information in the medication list.
     * It also displays a message confirming the deletion of the medication.
     *
     */
    @Override
    public void execute() {
        String listIndexString = parsedArguments.get(ArgumentName.LIST_INDEX);
        int listIndex = Integer.parseInt(listIndexString);
        MedicationManager.removeMedication(listIndex);

        // TODO: remove medication from DailyMedicationManager as well.

        Ui.showDeleteCommandMessage();
    }

}
