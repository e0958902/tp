package meditracker.command;

import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.ListIndexArgument;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.MedicationNotFoundException;
import meditracker.exception.UnknownArgumentFoundException;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;
import meditracker.time.Period;
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
     * @throws ArgumentNoValueException When argument requires value but no value specified
     * @throws DuplicateArgumentFoundException Duplicate argument flag found
     * @throws HelpInvokedException When help argument is used or help message needed
     * @throws UnknownArgumentFoundException When unknown argument flags found in user input
     */
    public DeleteCommand(String arguments)
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException {
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

        Medication medication;
        try {
            medication = MedicationManager.getMedication(listIndex);
        } catch (IndexOutOfBoundsException e) {
            Ui.showErrorMessage("Invalid index specified");
            return;
        }

        MedicationManager.removeMedication(listIndex);
        deleteDailyMedication(medication);
        Ui.showSuccessMessage("Medicine has been deleted");
    }

    private static void deleteDailyMedication(Medication medication) {
        String name = medication.getName();

        for (Period period : Period.values()) {
            switch (period) {
            case MORNING:
                if (medication.getDosageMorning() == 0) {
                    continue;
                }

                try {
                    DailyMedicationManager.removeDailyMedication(name, Period.MORNING);
                } catch (MedicationNotFoundException e) {
                    Ui.showWarningMessage("Possible corruption of data. " +
                            "Unable to remove DailyMedication when using `delete`");
                }
                break;
            case AFTERNOON:
                if (medication.getDosageAfternoon() == 0) {
                    continue;
                }

                try {
                    DailyMedicationManager.removeDailyMedication(name, Period.AFTERNOON);
                } catch (MedicationNotFoundException e) {
                    Ui.showWarningMessage("Possible corruption of data. " +
                            "Unable to remove DailyMedication when using `delete`");
                }
                break;
            case EVENING:
                if (medication.getDosageEvening() == 0) {
                    continue;
                }

                try {
                    DailyMedicationManager.removeDailyMedication(name, Period.EVENING);
                } catch (MedicationNotFoundException e) {
                    Ui.showWarningMessage("Possible corruption of data. " +
                            "Unable to remove DailyMedication when using `delete`");
                }
                break;
            default:
                break;
            }
        }
    }
}
