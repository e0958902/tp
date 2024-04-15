package meditracker.command;

import java.util.Map;

import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.ExpirationDateArgument;
import meditracker.argument.ListIndexArgument;
import meditracker.argument.NameArgument;
import meditracker.argument.QuantityArgument;
import meditracker.argument.RemarksArgument;
import meditracker.exception.ArgumentException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.MedicationNotFoundException;
import meditracker.medication.MedicationManager;
import meditracker.ui.Ui;

/**
 * The ViewCommand class represents a command to print an existing medication.
 * It extends the Command class.
 */
public class ViewCommand extends Command {
    public static final ArgumentList ARGUMENT_LIST = new ArgumentList(
            new ListIndexArgument(true),
            new NameArgument(true),
            new QuantityArgument(true),
            new ExpirationDateArgument(true),
            new RemarksArgument(true)
    );

    public static final String HELP_MESSAGE = ArgumentHelper.getHelpMessage(CommandName.VIEW, ARGUMENT_LIST);
    private final Map<ArgumentName, String> parsedArguments;

    /**
     * Constructs a ViewCommand object with the specified arguments.
     *
     * @param arguments The arguments containing information to be parsed.
     * @throws HelpInvokedException When help argument is used or help message needed
     * @throws ArgumentException Argument flag specified not found,
     *              or when argument requires value but no value specified,
     *              or when unknown argument flags found in user input,
     *              or when duplicate argument flag found
     */
    public ViewCommand(String arguments) throws HelpInvokedException, ArgumentException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
    }

    /**
     * Executes the view command.
     * This method parses the input index and get the medication in the medication list
     * it then prints the medication name with its fields from the medication list
     */
    @Override
    public void execute() {
        try {
            if (parsedArguments.size() > 1) {
                Ui.showErrorMessage("You can only have one flag!");
            } else {
                executeFlag();
                Ui.showSuccessMessage("Medication details has been retrieved");
            }

        } catch (IndexOutOfBoundsException e) {
            String errorContext = String.format("Invalid medication index specified. %s. "
                     + "Medicine can not be found", e.getMessage());
            Ui.showErrorMessage(errorContext);

        } catch (NullPointerException e) {
            String errorContext = String.format("You have to input a number. %s. "
                     + "Medicine can not be found", e.getMessage());
            Ui.showErrorMessage(errorContext);

        } catch (NumberFormatException e) {
            String errorContext = String.format("Please enter a number. %s. "
                     + "Medicine can not be found", e.getMessage());
            Ui.showErrorMessage(errorContext);

        } catch (MedicationNotFoundException e) {
            String errorContext = String.format("Medicine can not be found. %s. ",
                    e.getMessage());
            Ui.showErrorMessage(errorContext);

        }
    }

    /**
     * Executes the first flag in the user input.
     *
     * @throws MedicationNotFoundException When no medication can be found
     */
    private void executeFlag() throws MedicationNotFoundException {
        if (parsedArguments.containsKey(ArgumentName.LIST_INDEX)) {
            int listIndex = Command.getListIndex(parsedArguments);
            MedicationManager.printSpecificMedication(listIndex);

        } else if (parsedArguments.containsKey(ArgumentName.NAME)) {
            String medicationNames = parsedArguments.get(ArgumentName.NAME);
            MedicationManager.showMedicationsByName(medicationNames);

        } else if (parsedArguments.containsKey(ArgumentName.QUANTITY)) {
            Double medicationQuantity = Double.parseDouble(parsedArguments.get(ArgumentName.QUANTITY));
            MedicationManager.showMedicationsByQuantity(medicationQuantity);

        } else if (parsedArguments.containsKey(ArgumentName.EXPIRATION_DATE)) {
            String medicationExpiryDate = parsedArguments.get(ArgumentName.EXPIRATION_DATE);
            MedicationManager.showMedicationsByExpiry(medicationExpiryDate);

        } else if (parsedArguments.containsKey(ArgumentName.REMARKS)) {
            String medicationRemarks = parsedArguments.get(ArgumentName.REMARKS);
            MedicationManager.showMedicationsByRemarks(medicationRemarks);
        }
    }
}
