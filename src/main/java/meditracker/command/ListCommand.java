package meditracker.command;

import meditracker.argument.AfternoonArgument;
import meditracker.argument.ArgumentHelper;
import meditracker.argument.EveningArgument;
import meditracker.argument.MorningArgument;
import meditracker.dailymedication.DailyMedication;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.ListTypeArgument;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.UnknownArgumentFoundException;
import meditracker.time.Period;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.medication.MedicationManager;
import meditracker.ui.Ui;

import java.util.List;
import java.util.Map;

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
     * @throws ArgumentNotFoundException Argument flag specified not found
     * @throws ArgumentNoValueException When argument requires value but no value specified
     * @throws DuplicateArgumentFoundException Duplicate argument flag found
     * @throws HelpInvokedException When help argument is used or help message needed
     * @throws UnknownArgumentFoundException When unknown argument flags found in user input
     */
    public ListCommand(String arguments)
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
    }

    /**
     * Executes the list command and performs its specific task, -t.
     * Uses a switch to do list all and list today
     *
     */
    @Override
    public void execute() {
        String listTypeString = parsedArguments.get(ArgumentName.LIST_TYPE);
        boolean isMorning = parsedArguments.get(ArgumentName.MORNING) != null;
        boolean isAfternoon = parsedArguments.get(ArgumentName.AFTERNOON) != null;
        boolean isEvening = parsedArguments.get(ArgumentName.EVENING) != null;
        Period period = Period.getPeriod(isMorning, isAfternoon, isEvening);
        
        switch (listTypeString) {
        case "all":
            MedicationManager.printAllMedications();
            break;
        case "today":
            switch (period) {
            case MORNING:
                List<DailyMedication> morningMedications = DailyMedicationManager.getDailyMedications(Period.MORNING);
                DailyMedicationManager.printTodayMedications(MedicationManager.getMedications(),
                        morningMedications, "Morning:");
                break;
            case AFTERNOON:
                List<DailyMedication> afternoonMedications
                        = DailyMedicationManager.getDailyMedications(Period.AFTERNOON);
                DailyMedicationManager.printTodayMedications(MedicationManager.getMedications(),
                        afternoonMedications, "Afternoon:");
                break;
            case EVENING:
                List<DailyMedication> eveningMedications = DailyMedicationManager.getDailyMedications(Period.EVENING);
                DailyMedicationManager.printTodayMedications(MedicationManager.getMedications(),
                        eveningMedications, "Evening:");
                break;
            case NONE:
                DailyMedicationManager.printTodayMedications(MedicationManager.getMedications());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + listTypeString);
            }
            break;
        default:
            Ui.showErrorMessage(String.format("Unknown list type -> \"%s\"", listTypeString));
        }
    }
}
