package meditracker.command;

import java.util.Map;

import meditracker.argument.AfternoonArgument;
import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.EveningArgument;
import meditracker.argument.ListTypeArgument;
import meditracker.argument.MorningArgument;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.exception.ArgumentException;
import meditracker.exception.HelpInvokedException;
import meditracker.medication.MedicationManager;
import meditracker.time.Period;
import meditracker.ui.Ui;

/**
 * The ListCommand class represents a command to list the medications.
 * It extends the Command class.
 */
public class ListCommand extends Command {

    public static final ArgumentList ARGUMENT_LIST = new ArgumentList(
            new ListTypeArgument(false),
            new MorningArgument(true),
            new AfternoonArgument(true),
            new EveningArgument(true)
    );

    public static final String HELP_MESSAGE = ArgumentHelper.getHelpMessage(CommandName.LIST, ARGUMENT_LIST);

    private final Map<ArgumentName, String> parsedArguments;

    /**
     * Constructs a ListCommand object with the specified arguments.
     *
     * @param arguments The arguments containing information to be parsed.
     * @throws HelpInvokedException When help argument is used or help message needed
     * @throws ArgumentException Argument flag specified not found,
     *              or when argument requires value but no value specified,
     *              or when unknown argument flags found in user input,
     *              or when duplicate argument flag found
     */
    public ListCommand(String arguments) throws HelpInvokedException, ArgumentException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
    }

    /**
     * Executes the list command and performs its specific task, -t.
     * Uses a switch to do list all and list today
     */
    @Override
    public void execute() {
        String listTypeString = parsedArguments.get(ArgumentName.LIST_TYPE);
        boolean isMorning = parsedArguments.get(ArgumentName.MORNING) != null;
        boolean isAfternoon = parsedArguments.get(ArgumentName.AFTERNOON) != null;
        boolean isEvening = parsedArguments.get(ArgumentName.EVENING) != null;
        Period period = Period.getPeriod(isMorning, isAfternoon, isEvening);

        // checks if user added extra flags or words after "list -t all"
        switch (listTypeString) {
        case "all":
            if (parsedArguments.size() > 1) {
                Ui.showErrorMessage(String.format("List type -> \"%s\" not compatible with "
                         + "\"list -t all\" command.", period));
                return;
            }
            MedicationManager.printAllMedications();
            break;
        case "today":
            switch (period) {
            case MORNING: // fall through
            case AFTERNOON: // fall through
            case EVENING:
                DailyMedicationManager.printTodayMedications(period);
                break;
            case NONE:
                DailyMedicationManager.printTodayMedications();
                break;
            case UNKNOWN:
                Ui.showErrorMessage(String.format("Unknown list type -> \"%s\"", period));
                break;
            default:
                Ui.showErrorMessage(String.format("Unknown list type -> \"%s\"", listTypeString));
            }
            break;
        default:
            Ui.showErrorMessage(String.format("Unknown list type -> \"%s\"", listTypeString));
        }
    }
}
