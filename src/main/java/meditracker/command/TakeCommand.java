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
import meditracker.exception.InsufficientQuantityException;
import meditracker.exception.MedicationNotFoundException;
import meditracker.exception.UnknownArgumentFoundException;
import meditracker.time.Period;
import meditracker.ui.Ui;

import java.time.LocalTime;
import java.util.Map;

/**
 * The TakeCommand class represents a command to take a daily medication.
 * It extends the Command class.
 */
public class TakeCommand extends Command {
    public static final ArgumentList ARGUMENT_LIST = new ArgumentList(
            new ListIndexArgument(false),
            new MorningArgument(true),
            new AfternoonArgument(true),
            new EveningArgument(true)
    );
    public static final String HELP_MESSAGE = ArgumentHelper.getHelpMessage(CommandName.TAKE, ARGUMENT_LIST);
    private final Map<ArgumentName, String> parsedArguments;

    /**
     * Constructs a TakeCommand object with the specified arguments.
     *
     * @param arguments The arguments containing information to be parsed.
     * @throws ArgumentNotFoundException Argument flag specified not found
     * @throws ArgumentNoValueException When argument requires value but no value specified
     * @throws DuplicateArgumentFoundException Duplicate argument flag found
     * @throws HelpInvokedException When help argument is used or help message needed
     * @throws UnknownArgumentFoundException When unknown argument flags found in user input
     */
    public TakeCommand(String arguments)
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
    }

    /**
     * Executes the take command.
     * This method marks an existing Medication object using the provided list index.
     * It also displays a message confirming the modification of the daily medication status.
     *
     */
    @Override
    public void execute() {
        String listIndexString = parsedArguments.get(ArgumentName.LIST_INDEX);
        int listIndex = Integer.parseInt(listIndexString);

        boolean isMorning = parsedArguments.get(ArgumentName.MORNING) != null;
        boolean isAfternoon = parsedArguments.get(ArgumentName.AFTERNOON) != null;
        boolean isEvening = parsedArguments.get(ArgumentName.EVENING) != null;
        Period period = Period.getPeriod(isMorning, isAfternoon, isEvening);
        if (period == Period.NONE) {
            period = Period.getPeriod(LocalTime.now());
        }

        if (period == Period.UNKNOWN) {
            Ui.showErrorMessage("Unable to determine time period. " +
                    "Please select 1 flag only or try again later.");
            return;
        }

        try {
            DailyMedicationManager.takeDailyMedication(listIndex, period);
        } catch (IndexOutOfBoundsException e) {
            Ui.showErrorMessage("Invalid index specified");
            return;
        } catch (InsufficientQuantityException e) {
            Ui.showErrorMessage(e);
            return;
        } catch (MedicationNotFoundException e) {
            Ui.showWarningMessage("Possible corruption of data. " +
                    "Unable to increase Medication quantity as object not found");
            return;
        }
        Ui.showSuccessMessage("Medicine has been taken");
    }
}
