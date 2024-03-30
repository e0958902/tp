package meditracker.command;

import meditracker.argument.ArgumentHelper;
import meditracker.dailymedication.DailyMedication;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.ListTypeArgument;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.dailymedication.Period;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.medication.MedicationManager;

import java.util.List;
import java.util.Map;

/**
 * The ListCommand class represents a command to list the medications.
 * It extends the Command class.
 */
public class ListCommand extends Command {

    public static final ArgumentList ARGUMENT_LIST = new ArgumentList(
            new ListTypeArgument(false));

    public static final String HELP_MESSAGE = ArgumentHelper.getHelpMessage(CommandName.LIST, ARGUMENT_LIST);

    private final Map<ArgumentName, String> parsedArguments;

    /**
     * Constructs a ListCommand object with the specified arguments.
     *
     * @param arguments The arguments containing information to be parsed.
     * @throws ArgumentNotFoundException Argument flag specified not found
     * @throws DuplicateArgumentFoundException Duplicate argument flag found
     * @throws HelpInvokedException When help argument is used or help message needed
     */
    public ListCommand(String arguments)
            throws ArgumentNotFoundException, DuplicateArgumentFoundException, HelpInvokedException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
    }

    /**
     * Executes the list command and performs its specific task, -t.
     * Uses a switch to do a, list all and, list today
     *
     */
    @Override
    public void execute() {
        String listTypeString = parsedArguments.get(ArgumentName.LIST_TYPE);
        
        switch (listTypeString) {
        case "all":
            MedicationManager.printAllMedications();
            break;
        case "today":
            DailyMedicationManager.printTodayMedications(MedicationManager.getMedications());
            break;
        case "today-m":
            List<DailyMedication> morningMedications = DailyMedicationManager.getDailyMedications(Period.MORNING);
            DailyMedicationManager.printTodayMedications(MedicationManager.getMedications(),
                    morningMedications, "Morning:");
            break;
        case "today-a":
            List<DailyMedication> afternoonMedications = DailyMedicationManager.getDailyMedications(Period.AFTERNOON);
            DailyMedicationManager.printTodayMedications(MedicationManager.getMedications(),
                    afternoonMedications, "Afternoon:");
            break;
        case "today-e":
            List<DailyMedication> eveningMedications = DailyMedicationManager.getDailyMedications(Period.EVENING);
            DailyMedicationManager.printTodayMedications(MedicationManager.getMedications(),
                    eveningMedications, "Evening:");
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + listTypeString);
        }
    }
}
