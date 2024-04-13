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
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.MediTrackerException;
import meditracker.exception.UnknownArgumentFoundException;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;
import meditracker.storage.FileReaderWriter;
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

        parsedArguments.remove(ArgumentName.LIST_INDEX);
        if (parsedArguments.isEmpty()) {
            Ui.showSuccessMessage("No changes specified. Medicine not modified.");
            return;
        }

        Medication medicationCopy = Medication.deepCopy(medication);
        try {
            updateMedication(medication);
        } catch (MediTrackerException e) {
            try {
                rollbackChanges(medication, medicationCopy);
            } catch (MediTrackerException ex) {
                Ui.showErrorMessage(ex);
                return;
            }
            Ui.showErrorMessage(e);
            Ui.showWarningMessage("Changes have been rolled back. Medicine not modified.");
            return;
        }

        FileReaderWriter.saveMediTrackerData(null);
        Ui.showSuccessMessage("Medicine has been modified");
    }

    /**
     * Rollbacks the changes made to Medication and DailyMedication
     *
     * @param medication Medication object in MedicationManager. To be written to.
     * @param medicationCopy Backup copy of original Medication object. To be read from.
     * @throws MediTrackerException Unable to revert Medication
     */
    private void rollbackChanges(Medication medication, Medication medicationCopy) throws MediTrackerException {
        if (parsedArguments.containsKey(ArgumentName.NAME)) {
            String oldName = medicationCopy.getName();
            DailyMedicationManager.updateDailyMedicationName(medication, oldName);
        }
        medication.revertMedication(medicationCopy);
    }

    /**
     * Updates Medication info
     *
     * @param medication Medication object to update
     */
    private void updateMedication(Medication medication) throws MediTrackerException {
        for (Map.Entry<ArgumentName, String> argument: parsedArguments.entrySet()) {
            ArgumentName argumentName = argument.getKey();
            String argumentValue = argument.getValue();
            medication.setMedicationValue(argumentName, argumentValue);

            if (argumentName == ArgumentName.NAME) {
                DailyMedicationManager.updateDailyMedicationName(medication, argumentValue);
            }
        }
        medication.checkValidity();
        checkDosageOrRepeatModified(medication);
    }

    /**
     * Checks whether dosage and/or repeat was modified and also if all dosages are 0
     *
     * @param medication Medication object being checked
     */
    private void checkDosageOrRepeatModified(Medication medication) {
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
