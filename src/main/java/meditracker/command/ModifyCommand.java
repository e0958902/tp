package meditracker.command;

import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.DosageAfternoonArgument;
import meditracker.argument.DosageEveningArgument;
import meditracker.argument.DosageMorningArgument;
import meditracker.argument.ExpirationDateArgument;
import meditracker.argument.ListIndexArgument;
import meditracker.argument.NameArgument;
import meditracker.argument.QuantityArgument;
import meditracker.argument.RemarksArgument;
import meditracker.argument.RepeatArgument;
import meditracker.dailymedication.DailyMedication;
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
 * The ModifyCommand class represents a command to modify an existing medication.
 * It extends the Command class.
 */
public class ModifyCommand extends Command {
    public static final ArgumentList ARGUMENT_LIST = new ArgumentList(
            new ListIndexArgument(false),
            new NameArgument(true),
            new QuantityArgument(true),
            new DosageMorningArgument(true),
            new DosageAfternoonArgument(true),
            new DosageEveningArgument(true),
            new ExpirationDateArgument(true),
            new RemarksArgument(true),
            new RepeatArgument(true)
    );
    public static final String HELP_MESSAGE = ArgumentHelper.getHelpMessage(CommandName.MODIFY, ARGUMENT_LIST);
    private final Map<ArgumentName, String> parsedArguments;

    /**
     * Constructs a ModifyCommand object with the specified arguments.
     *
     * @param arguments The arguments containing medication information to be parsed.
     * @throws ArgumentNotFoundException Argument flag specified not found
     * @throws ArgumentNoValueException When argument requires value but no value specified
     * @throws DuplicateArgumentFoundException Duplicate argument flag found
     * @throws HelpInvokedException When help argument is used or help message needed
     * @throws UnknownArgumentFoundException When unknown argument flags found in user input
     */
    public ModifyCommand(String arguments)
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
    }

    /**
     * Executes the modify command.
     * This method modifies an existing Medication object using the provided information in the medication list.
     * It also displays a message confirming the modification of the medication.
     */
    @Override
    public void execute() {
        Medication medication;
        int listIndex = Command.getListIndex(parsedArguments);
        try {
            medication = MedicationManager.getMedication(listIndex);
        } catch (IndexOutOfBoundsException e) {
            Ui.showErrorMessage("Invalid index specified");
            return;
        }

        Medication medicationCopy = Medication.deepCopy(medication);
        try {
            updateMedication(medication);
        } catch (NumberFormatException e) {
            medication.revertMedication(medicationCopy);
            String errorContext = String.format("Unable to format correctly. %s. Medicine has not been modified.",
                    e.getMessage());
            Ui.showErrorMessage(errorContext);
            return;
        }

        Ui.showSuccessMessage("Medicine has been modified");
    }

    /**
     * Updates Medication info
     *
     * @param medication Medication object to update
     * @throws NumberFormatException When Double.parseDouble or Integer.parseInt fails
     */
    private void updateMedication(Medication medication) throws NumberFormatException {
        for (Map.Entry<ArgumentName, String> argument: parsedArguments.entrySet()) {
            ArgumentName argumentName = argument.getKey();
            String argumentValue = argument.getValue();

            switch (argumentName) {
            case DOSAGE_MORNING:
                medication.setDosageMorning(Double.parseDouble(argumentValue));
                break;
            case DOSAGE_AFTERNOON:
                medication.setDosageAfternoon(Double.parseDouble(argumentValue));
                break;
            case DOSAGE_EVENING:
                medication.setDosageEvening(Double.parseDouble(argumentValue));
                break;
            case EXPIRATION_DATE:
                medication.setExpiryDate(argumentValue);
                break;
            case REPEAT:
                medication.setRepeat(Integer.parseInt(argumentValue));
                break;
            case LIST_INDEX:
                continue;
            case NAME:
                String oldName = medication.getName();
                medication.setName(argumentValue);

                if (!DailyMedicationManager.doesBelongToDailyList(medication)) {
                    continue;
                }

                try {
                    updateDailyMedicationName(medication, oldName, argumentValue);
                } catch (MedicationNotFoundException e) {
                    Ui.showWarningMessage("Possible corruption of data. " +
                            "Unable to update DailyMedication when using `modify`");
                    return;
                }
                break;
            case QUANTITY:
                medication.setQuantity(Double.parseDouble(argumentValue));
                break;
            case REMARKS:
                medication.setRemarks(argumentValue);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + argumentName);
            }
        }
    }

    /**
     * Updates all instances of DailyMedication name to new name
     *
     * @param medication Medication object being updated
     * @param oldName Existing old name of Medication object to search for
     * @param newName New name to replace with
     * @throws MedicationNotFoundException No DailyMedication matching specified name found
     */
    private static void updateDailyMedicationName(Medication medication, String oldName, String newName)
            throws MedicationNotFoundException {
        for (Period period : Period.values()) {
            switch (period) {
            case MORNING:
                if (medication.getDosageMorning() == 0) {
                    continue;
                }
                break;
            case AFTERNOON:
                if (medication.getDosageAfternoon() == 0) {
                    continue;
                }
                break;
            case EVENING:
                if (medication.getDosageEvening() == 0) {
                    continue;
                }
                break;
            default:
                continue;
            }
            DailyMedication dailyMedication = DailyMedicationManager.getDailyMedication(oldName, period);
            dailyMedication.setName(newName);
        }
    }
}
