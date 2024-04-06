package meditracker.command;

import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.ExpirationDateArgument;
import meditracker.argument.ListIndexArgument;
import meditracker.argument.NameArgument;
import meditracker.argument.QuantityArgument;
import meditracker.argument.RemarksArgument;
import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.MedicationNotFoundException;
import meditracker.exception.UnknownArgumentFoundException;
import meditracker.medication.MedicationManager;
import meditracker.ui.Ui;

import java.util.Map;

/**
 * The ViewCommand class represents a command to print an existing medication.
 * It extends the Command class.
 */
public class ViewCommand extends Command {
    public static final ArgumentList ARGUMENT_LIST = new ArgumentList(
            new ListIndexArgument(false)
    );
    public static final String HELP_MESSAGE = ArgumentHelper.getHelpMessage(CommandName.VIEW, ARGUMENT_LIST);
    private final Map<ArgumentName, String> parsedArguments;

    /**
     * Constructs a FindCommand object with the specified arguments.
     *
     * @param arguments The arguments containing information to be parsed.
     * @throws ArgumentNotFoundException Argument flag specified not found
     * @throws DuplicateArgumentFoundException Duplicate argument flag found
     * @throws HelpInvokedException When help argument is used or help message needed
     * @throws ArgumentNoValueException When argument requires value but no value specified
     * @throws UnknownArgumentFoundException When unknown argument flags found in user input
     */
    public ViewCommand(String arguments)
            throws ArgumentNotFoundException, DuplicateArgumentFoundException, HelpInvokedException,
            ArgumentNoValueException, UnknownArgumentFoundException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
    }

    /**
     * Executes the view command.
     * This method parses the input index and get the medication in the medication list
     * it then prints the medication name with its fields from the medication list
     */
    @Override
    public void execute() {
        //System.out.println("parsedArguments: " + parsedArguments);
        //view -l 1 -n 123 -q 20 -e 01/07/25 -r cause_dizziness
        try {
            executeFlag();

        } catch (IndexOutOfBoundsException e) {
            String errorContext = String.format("Invalid medication index specified. %s. " +
                            "Medicine can not be found", e.getMessage());
            Ui.showErrorMessage(errorContext);
            return;
        } catch (NullPointerException e) {
            String errorContext = String.format("You have to input an index number. %s. " +
                             "Medicine can not be found", e.getMessage());
            Ui.showErrorMessage(errorContext);
            return;
        } catch (NumberFormatException e) {
            String errorContext = String.format("Unable to interpret the index correctly. %s. " +
                            "Medicine can not be found", e.getMessage());
            Ui.showErrorMessage(errorContext);
            return;
        } catch (MedicationNotFoundException e) {
            String errorContext = String.format("Medicine can not be found. %s. ", e.getMessage());
            Ui.showErrorMessage(errorContext);
            return;
        }
        Ui.showSuccessMessage("Medication details has been retrieved");
    }

    private void executeFlag() throws MedicationNotFoundException {
        if (parsedArguments.containsKey(ArgumentName.LIST_INDEX)) {
            String listIndexString = parsedArguments.get(ArgumentName.LIST_INDEX);
            int listIndex = Integer.parseInt(listIndexString);
            MedicationManager.printSpecificMedication(listIndex);

        } else if (parsedArguments.containsKey(ArgumentName.NAME)) {
            String medicationNames = parsedArguments.get(ArgumentName.NAME);
            MedicationManager.showMedicationsByName(medicationNames);

        } else if (parsedArguments.containsKey(ArgumentName.QUANTITY)) {
            Double medicationQuantity = Double.parseDouble(parsedArguments.get(ArgumentName.QUANTITY));
            int medicationsFound = MedicationManager.showMedicationsByQuantity(medicationQuantity);

            if (medicationsFound == 0) {
                throw new MedicationNotFoundException();
            }

        } else if (parsedArguments.containsKey(ArgumentName.EXPIRATION_DATE)) {
            String medicationExpiryDate = parsedArguments.get(ArgumentName.EXPIRATION_DATE);
            int medicationsFound = MedicationManager.showMedicationsByExpiry(medicationExpiryDate);

            if (medicationsFound == 0) {
                throw new MedicationNotFoundException();
            }

        } else if (parsedArguments.containsKey(ArgumentName.REMARKS)) {
            String medicationRemarks = parsedArguments.get(ArgumentName.REMARKS);
            MedicationManager.showMedicationsByRemarks(medicationRemarks);
        }

    }

}
