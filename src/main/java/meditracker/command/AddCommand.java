package meditracker.command;

import meditracker.argument.ArgumentHelper;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.MediTrackerException;
import meditracker.exception.UnknownArgumentFoundException;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;
import meditracker.time.MediTrackerTime;
import meditracker.ui.Ui;

import meditracker.argument.ArgumentName;
import meditracker.argument.ArgumentList;
import meditracker.argument.RepeatArgument;
import meditracker.argument.DosageAfternoonArgument;
import meditracker.argument.DosageMorningArgument;
import meditracker.argument.DosageEveningArgument;
import meditracker.argument.NameArgument;
import meditracker.argument.QuantityArgument;
import meditracker.argument.ExpirationDateArgument;
import meditracker.argument.RemarksArgument;

import java.time.LocalDate;
import java.util.Map;

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
            new DosageMorningArgument(true),
            new DosageAfternoonArgument(true),
            new DosageEveningArgument(true),
            new ExpirationDateArgument(false),
            new RemarksArgument(true),
            new RepeatArgument(false)
    );

    public static final String HELP_MESSAGE = ArgumentHelper.getHelpMessage(CommandName.ADD, ARGUMENT_LIST);

    private final Map<ArgumentName, String> parsedArguments;

    private double medicationQuantity;
    private double medicationDosageMorning = 0.0;
    private double medicationDosageAfternoon = 0.0;
    private double medicationDosageEvening = 0.0;

    /**
     * Constructs an AddCommand object with the specified arguments.
     *
     * @param arguments The arguments containing medication information to be parsed.
     * @throws ArgumentNotFoundException if a required argument is not found.
     * @throws ArgumentNoValueException When argument requires value but no value specified
     * @throws DuplicateArgumentFoundException Duplicate argument found
     * @throws HelpInvokedException When help argument is used
     * @throws UnknownArgumentFoundException When unknown argument flags found in user input
     */
    public AddCommand(String arguments)
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
    }

    /**
     * Executes the add command.
     * This method creates a new Medication object using the provided information and adds it to the medication list.
     * It also displays a message confirming the addition of the medication.
     */
    @Override
    public void execute() {
        Medication medication = null;
        try {
            medication = createMedication();
        } catch (MediTrackerException e) {
            Ui.showErrorMessage(e);
        }
        MedicationManager.addMedication(medication);
        DailyMedicationManager.checkForDaily(medication);
        assertionTest();
        Ui.showSuccessMessage("Medicine has been added");
    }

    /**
     * Sets the medication attributes based on parsed arguments.
     *
     * @throws MediTrackerException if there is an error encountered.
     */
    private Medication createMedication() throws MediTrackerException {
        String medicationName = parsedArguments.get(ArgumentName.NAME);
        String expiryDate = parsedArguments.get(ArgumentName.EXPIRATION_DATE);
        String remarks = parsedArguments.get(ArgumentName.REMARKS);


        String medicationQuantityArg = parsedArguments.get(ArgumentName.QUANTITY);
        String medicationDosageMorningArg = parsedArguments.get(ArgumentName.DOSAGE_MORNING);
        String medicationDosageAfternoonArg = parsedArguments.get(ArgumentName.DOSAGE_AFTERNOON);
        String medicationDosageEveningArg = parsedArguments.get(ArgumentName.DOSAGE_EVENING);

        int repeat = 0;
        try {
            repeat = Integer.parseInt(parsedArguments.get(ArgumentName.REPEAT));
            parseStringToValues(medicationQuantityArg, medicationDosageMorningArg,
                    medicationDosageAfternoonArg, medicationDosageEveningArg);
            LocalDate currentDate = MediTrackerTime.getCurrentDate();
            int dayAdded = currentDate.getDayOfYear();

            return new Medication(medicationName, medicationQuantity,
                    medicationDosageMorning, medicationDosageAfternoon, medicationDosageEvening,
                    expiryDate, remarks, repeat, dayAdded);
        } catch (NumberFormatException e) {
            throw new MediTrackerException("Incorrect Number format given");
        } catch (NullPointerException e) {
            throw new MediTrackerException("Medication not found");
        }
    }

    /**
     * Parses string values to double for medication attributes.
     *
     * @param medicationQuantity      The quantity of the medication.
     * @param medicationDosageMorning The morning dosage of the medication.
     * @param medicationDosageAfternoon The afternoon dosage of the medication.
     * @param medicationDosageEvening The evening dosage of the medication.
     * @throws NumberFormatException if there is an error in parsing numeric values.
     * @throws NullPointerException  if any of the required arguments are null.
     */
    private void parseStringToValues(String medicationQuantity,
                                     String medicationDosageMorning,
                                     String medicationDosageAfternoon, String medicationDosageEvening)
            throws NumberFormatException, NullPointerException {

        this.medicationQuantity = Double.parseDouble(medicationQuantity);

        if (medicationDosageMorning != null) {
            this.medicationDosageMorning = Double.parseDouble(medicationDosageMorning);
        }
        if (medicationDosageAfternoon != null) {
            this.medicationDosageAfternoon = Double.parseDouble(medicationDosageAfternoon);
        }
        if (medicationDosageEvening != null) {
            this.medicationDosageEvening = Double.parseDouble(medicationDosageEvening);
        }
    }

    /**
     * Performs assertion tests for medication and daily medication managers.
     */
    private void assertionTest() {
        assert MedicationManager.getTotalMedications() != 0 : "Total medications in medication " +
                "manager should not be 0!";
    }

}
