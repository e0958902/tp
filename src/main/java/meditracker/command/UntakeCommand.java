package meditracker.command;

import meditracker.DailyMedicationManager;
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
 * The UntakeCommand class represents a command to untake a daily medication.
 * It extends the Command class.
 */
public class UntakeCommand extends Command {
    public static final ArgumentList ARGUMENT_LIST = new ArgumentList(
            new ListIndexArgument(false)
    );
    public static final String HELP_MESSAGE = ArgumentHelper.getHelpMessage(CommandName.UNTAKE, ARGUMENT_LIST);
    private final Map<ArgumentName, String> parsedArguments;

    /**
     * Constructs a UntakeCommand object with the specified arguments.
     *
     * @param arguments The arguments containing information to be parsed.
     * @throws ArgumentNotFoundException Argument flag specified not found
     * @throws DuplicateArgumentFoundException Duplicate argument flag found
     * @throws HelpInvokedException When help argument is used or help message needed
     */
    public UntakeCommand(String arguments)
            throws ArgumentNotFoundException, DuplicateArgumentFoundException, HelpInvokedException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
    }

    /**
     * Executes the untake command.
     * This method unmarks an existing Medication object using the provided list index.
     * It also displays a message confirming the modification of the daily medication status.
     *
     * @param medicationManager      The MedicationManager object representing the list of medications.
     */
    @Override
    public void execute(MedicationManager medicationManager) {
        String listIndexString = parsedArguments.get(ArgumentName.LIST_INDEX);
        int listIndex = Integer.parseInt(listIndexString);
        DailyMedicationManager.untakeDailyMedication(listIndex);
        Ui.showUntakeCommandMessage();
    }
}
