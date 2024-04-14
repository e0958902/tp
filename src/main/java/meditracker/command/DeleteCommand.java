package meditracker.command;

import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.ListIndexArgument;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.exception.ArgumentException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.MedicationNotFoundException;
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
     * @throws HelpInvokedException When help argument is used or help message needed
     * @throws ArgumentException Argument flag specified not found,
     *              or when argument requires value but no value specified,
     *              or when unknown argument flags found in user input,
     *              or when duplicate argument flag found
     */
    public DeleteCommand(String arguments) throws HelpInvokedException, ArgumentException {
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
        int listIndex = Command.getListIndex(parsedArguments);

        Medication medication;
        try {
            medication = MedicationManager.getMedication(listIndex);
        } catch (IndexOutOfBoundsException e) {
            Ui.showErrorMessage("Invalid index specified");
            return;
        }

        MedicationManager.removeMedication(listIndex);
        if (DailyMedicationManager.doesBelongToDailyList(medication)) {
            deleteDailyMedication(medication);
        }
        Ui.showSuccessMessage("Medicine has been deleted");
    }

    /**
     * Delete all instance of DailyMedication related to the Medication object
     *
     * @param medication The Medication object that will result in the deletion of
     *                   DailyMedication objects
     */
    private static void deleteDailyMedication(Medication medication) {
        String name = medication.getName();

        for (Period period : Period.values()) {
            if (!medication.hasDosage(period)) {
                continue;
            }

            try {
                DailyMedicationManager.removeDailyMedication(name, period);
            } catch (MedicationNotFoundException e) {
                Ui.showWarningMessage("Possible corruption of data. " +
                        "Unable to remove DailyMedication when using `delete`");
            }
        }
    }
}
