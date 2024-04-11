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
import meditracker.exception.MediTrackerException;
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
            String errorContext = String.format("Unable to format correctly. %s.", e.getMessage());
            Ui.showErrorMessage(errorContext);
            Ui.showWarningMessage("Changes have been rolled back. Medicine not modified.");
            return;
        } catch (MediTrackerException e) {
            medication.revertMedication(medicationCopy);
            Ui.showErrorMessage(e);
            Ui.showWarningMessage("Changes have been rolled back. Medicine not modified.");
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
    private void updateMedication(Medication medication) throws NumberFormatException, MediTrackerException {
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
                int repeat = Command.getRepeat(parsedArguments);
                medication.setRepeat(repeat);
                break;
            case LIST_INDEX:
                continue;
            case NAME:
                updateMedicationName(medication, argumentValue);
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
        checkDosageOrRepeatModified(medication);
    }

    /**
     * Updates Medication and all instances of DailyMedication name to new name
     *
     * @param medication Medication object being updated
     * @param newName New name to replace with
     */
    private static void updateMedicationName(Medication medication, String newName) {
        String oldName = medication.getName();
        medication.setName(newName);

        if (!DailyMedicationManager.doesBelongToDailyList(medication)) {
            return;
        }

        for (Period period : Period.values()) {
            if (!medication.hasDosage(period)) {
                continue;
            }

            DailyMedication dailyMedication;
            try {
                dailyMedication = DailyMedicationManager.getDailyMedication(oldName, period);
            } catch (MedicationNotFoundException e) {
                String message = String.format("Possible data corruption: Medication missing from %s list", period);
                Ui.showWarningMessage(message);
                continue;
            }

            dailyMedication.setName(newName);
        }
    }

    /**
     * Checks whether dosage and/or repeat was modified and also if all dosages are 0
     *
     * @param medication Medication object being checked
     * @throws MediTrackerException Thrown if all dosages are 0
     */
    private void checkDosageOrRepeatModified(Medication medication) throws MediTrackerException {
        boolean hasNoDosages = medication.hasNoDosages();
        if (hasNoDosages) {
            throw new MediTrackerException("Medication modification results in all empty dosages. " +
                    "Please ensure at least 1 period of day has dosage (-dM, -dA and/or -dE).");
        }

        boolean doesBelongToDailyList = DailyMedicationManager.doesBelongToDailyList(medication);
        if (!doesBelongToDailyList) {
            // Warning message below does not apply for today as medication not part of today's list
            return;
        }

        boolean hasDosageMorning = parsedArguments.containsKey(ArgumentName.DOSAGE_MORNING);
        boolean hasDosageAfternoon = parsedArguments.containsKey(ArgumentName.DOSAGE_AFTERNOON);
        boolean hasDosageEvening = parsedArguments.containsKey(ArgumentName.DOSAGE_EVENING);
        boolean hasRepeat = parsedArguments.containsKey(ArgumentName.REPEAT);
        if (hasDosageMorning || hasDosageAfternoon || hasDosageEvening || hasRepeat) {
            String message =
                    "New dosage and/or repeat frequency will be applied tomorrow/next time you require " +
                    System.lineSeparator() +
                    "to take the medication (whichever occurs later). No changes will be made to today's list of " +
                    System.lineSeparator() +
                    "medication to take.";
            Ui.showWarningMessage(message);
        }
    }
}
