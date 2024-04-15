package meditracker.command;

import java.time.LocalDate;
import java.util.Map;

import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.DosageAfternoonArgument;
import meditracker.argument.DosageEveningArgument;
import meditracker.argument.DosageMorningArgument;
import meditracker.argument.ExpirationDateArgument;
import meditracker.argument.NameArgument;
import meditracker.argument.QuantityArgument;
import meditracker.argument.RemarksArgument;
import meditracker.argument.RepeatArgument;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.exception.ArgumentException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.MediTrackerException;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;
import meditracker.time.MediTrackerTime;
import meditracker.ui.Ui;

// @@author nickczh
/**
 * The AddCommand class represents a command to add a new medication.
 * It extends the Command class.
 */
public class AddCommand extends Command {

    /**
     * The argumentList contains all the arguments needed for adding a medication.
     */
    public static final ArgumentList ARGUMENT_LIST = new ArgumentList(
            new NameArgument(false),
            new QuantityArgument(false),
            new ExpirationDateArgument(false),
            new DosageMorningArgument(false),
            new DosageAfternoonArgument(false),
            new DosageEveningArgument(false),
            new RepeatArgument(false),
            new RemarksArgument(true)
    );

    public static final String HELP_MESSAGE = ArgumentHelper.getHelpMessage(CommandName.ADD, ARGUMENT_LIST);

    private final Map<ArgumentName, String> parsedArguments;

    /**
     * Constructs an AddCommand object with the specified arguments.
     *
     * @param arguments The arguments containing medication information to be parsed.
     * @throws HelpInvokedException When help argument is used or help message needed
     * @throws ArgumentException Argument flag specified not found,
     *              or when argument requires value but no value specified,
     *              or when unknown argument flags found in user input,
     *              or when duplicate argument flag found
     */
    public AddCommand(String arguments) throws HelpInvokedException, ArgumentException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
        if (!parsedArguments.containsKey(ArgumentName.REMARKS)) {
            parsedArguments.put(ArgumentName.REMARKS, null);
        }
    }

    /**
     * Executes the add command.
     * This method creates a new Medication object and adds it to the MedicationManager.
     * It performs an assertion test which tests that the medication has been added and
     * displays a message confirming the addition of the medication.
     */
    @Override
    public void execute() {
        Medication medication;
        try {
            medication = createMedication();
            MedicationManager.addMedication(medication);
        } catch (MediTrackerException e) {
            Ui.showErrorMessage(e);
            return;
        }

        DailyMedicationManager.checkForDaily(medication);
        assertionTest();
        Ui.showSuccessMessage("Medicine has been added");
    }

    /**
     * Creates a new Medication object based on parsed arguments.
     * This method reads and validates medication-related information from the parsed arguments,
     * such as name, expiry date, quantity, dosage for different times of the day, and remarks.
     * It sanitizes the input, converts necessary string arguments to their respective types,
     * and then instantiates a Medication object with these details.
     *
     * @return A new Medication object populated with the provided arguments.
     * @throws MediTrackerException if any of the input values are invalid, such as non-alphabetic medication names,
     *                              incorrect number formats, or if required arguments are missing.
     */
    Medication createMedication() throws MediTrackerException {
        Medication medication = new Medication();

        for (Map.Entry<ArgumentName, String> keyValuePair : parsedArguments.entrySet()) {
            ArgumentName argumentName = keyValuePair.getKey();
            String argumentValue = keyValuePair.getValue();
            medication.setMedicationValue(argumentName, argumentValue);
        }

        LocalDate currentDate = MediTrackerTime.getCurrentDate();
        int dayAdded = currentDate.getDayOfYear();
        medication.setMedicationValue(ArgumentName.DAY_ADDED, String.valueOf(dayAdded));

        return medication;
    }

    /**
     * Performs assertion tests for medication and daily medication managers.
     */
    private void assertionTest() {
        assert MedicationManager.getTotalMedications() != 0 : "Total medications in medication "
                + "manager should not be 0!";
    }

}
// @@author
