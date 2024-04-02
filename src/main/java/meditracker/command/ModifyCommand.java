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
     */
    public ModifyCommand(String arguments)
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
    }

    /**
     * Executes the modify command.
     * This method modifies an existing Medication object using the provided information in the medication list.
     * It also displays a message confirming the modification of the medication.
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

        for (Map.Entry<ArgumentName, String> argument: parsedArguments.entrySet()) {
            ArgumentName argumentName = argument.getKey();
            String argumentValue = argument.getValue();

            switch (argumentName) {
            case DOSAGE_MORNING:
                medication.setDosageMorning(Double.valueOf(argumentValue));
                break;
            case DOSAGE_AFTERNOON:
                medication.setDosageAfternoon(Double.valueOf(argumentValue));
                break;
            case DOSAGE_EVENING:
                medication.setDosageEvening(Double.valueOf(argumentValue));
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

        Ui.showSuccessMessage("Medicine has been modified");
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
            DailyMedication dailyMedication;
            switch (period) {
            case MORNING:
                if (medication.getDosageMorning() == 0) {
                    continue;
                }
                dailyMedication = DailyMedicationManager.getDailyMedication(oldName, Period.MORNING);
                break;
            case AFTERNOON:
                if (medication.getDosageAfternoon() == 0) {
                    continue;
                }
                dailyMedication = DailyMedicationManager.getDailyMedication(oldName, Period.AFTERNOON);
                break;
            case EVENING:
                if (medication.getDosageEvening() == 0) {
                    continue;
                }
                dailyMedication = DailyMedicationManager.getDailyMedication(oldName, Period.EVENING);
                break;
            default:
                continue;
            }
            dailyMedication.setName(newName);
        }
    }
}
