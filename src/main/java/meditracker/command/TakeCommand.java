package meditracker.command;

import meditracker.argument.ArgumentHelper;
import meditracker.argument.AfternoonArgument;
import meditracker.argument.EveningArgument;
import meditracker.argument.MorningArgument;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.ListIndexArgument;
import meditracker.time.Period;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.FileReadWriteException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.InvalidArgumentException;
import meditracker.medication.MedicationManager;
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
     * @throws DuplicateArgumentFoundException Duplicate argument flag found
     * @throws HelpInvokedException When help argument is used or help message needed
     */
    public TakeCommand(String arguments)
            throws ArgumentNotFoundException, DuplicateArgumentFoundException, HelpInvokedException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
    }

    /**
     * Executes the take command.
     * This method marks an existing Medication object using the provided list index.
     * It also displays a message confirming the modification of the daily medication status.
     *
     * @param medicationManager      The MedicationManager object representing the list of medications.
     */
    @Override
    public void execute(MedicationManager medicationManager) throws FileReadWriteException, InvalidArgumentException {
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
            throw new InvalidArgumentException("Unable to determine time period. " +
                    "Please select 1 flag only or try again later.");
        }

        DailyMedicationManager.takeDailyMedication(listIndex, period);
        Ui.showTakeCommandMessage();
    }
}
