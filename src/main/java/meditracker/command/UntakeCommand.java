package meditracker.command;

import meditracker.argument.AfternoonArgument;
import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.EveningArgument;
import meditracker.argument.ListIndexArgument;
import meditracker.argument.MorningArgument;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.MedicationNotFoundException;
import meditracker.exception.MedicationUnchangedException;
import meditracker.exception.UnknownArgumentFoundException;
import meditracker.time.Period;
import meditracker.ui.Ui;

import java.util.Map;

/**
 * The UntakeCommand class represents a command to untake a daily medication.
 * It extends the Command class.
 */
public class UntakeCommand extends Command {
    public static final ArgumentList ARGUMENT_LIST = new ArgumentList(
            new ListIndexArgument(false),
            new MorningArgument(true),
            new AfternoonArgument(true),
            new EveningArgument(true)
    );
    public static final String HELP_MESSAGE = ArgumentHelper.getHelpMessage(CommandName.UNTAKE, ARGUMENT_LIST);
    private final Map<ArgumentName, String> parsedArguments;

    /**
     * Constructs a UntakeCommand object with the specified arguments.
     *
     * @param arguments The arguments containing information to be parsed.
     * @throws ArgumentNotFoundException Argument flag specified not found
     * @throws ArgumentNoValueException When argument requires value but no value specified
     * @throws DuplicateArgumentFoundException Duplicate argument flag found
     * @throws HelpInvokedException When help argument is used or help message needed
     * @throws UnknownArgumentFoundException When unknown argument flags found in user input
     */
    public UntakeCommand(String arguments)
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
    }

    /**
     * Executes the untake command.
     * This method unmarks an existing Medication object using the provided list index.
     * It also displays a message confirming the modification of the daily medication status.
     */
    @Override
    public void execute() {
        Period period = Command.getPeriod(parsedArguments);
        if (period == Period.UNKNOWN) {
            Ui.showErrorMessage("Unable to determine time period. " +
                    "Please select only 1 of following flag: -m/-a/-e");
            return;
        }

        int listIndex = Command.getListIndex(parsedArguments);
        try {
            DailyMedicationManager.untakeDailyMedication(listIndex, period);
        } catch (IndexOutOfBoundsException e) {
            Ui.showErrorMessage("Invalid index specified");
            return;
        } catch (MedicationNotFoundException e) {
            Ui.showWarningMessage("Possible data corruption: Medication not found");
            return;
        } catch (MedicationUnchangedException e) {
            Ui.showSuccessMessage("Medication already untaken, no changes were made");
            return;
        }

        Ui.showSuccessMessage("Medicine has been untaken");
    }
}
