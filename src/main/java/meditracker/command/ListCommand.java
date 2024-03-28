package meditracker.command;

import meditracker.dailyMeds.DailyMedication;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.ListTypeArgument;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.medication.MedicationManager;
import meditracker.dailyMeds.SubDailyManager;

import java.util.List;
import java.util.Map;

/**
 * The ListCommand class represents a command to list the medications.
 * It extends the Command class.
 */
public class ListCommand extends Command {

    public ArgumentList argumentList = new ArgumentList(
            new ListTypeArgument(false));

    private final Map<ArgumentName, String> parsedArguments;

    public ListCommand(String arguments) throws ArgumentNotFoundException, DuplicateArgumentFoundException {
        parsedArguments = argumentList.parse(arguments);
    }

    /**
     * Executes the list command.
     *
     * @param medicationManager      The MedicationManager object representing the list of medications.
     */
    @Override
    public void execute(MedicationManager medicationManager) {
        String listTypeString = parsedArguments.get(ArgumentName.LIST_TYPE);
        
        switch (listTypeString) {
        case "all":
            medicationManager.printAllMedications();
            break;
        case "today":
            SubDailyManager.printTodayMedications(medicationManager.getMedications());
            break;
        case "today-m":
            List<DailyMedication> morningMedications = SubDailyManager.getMorningMedications();
            SubDailyManager.printTodayMedications(medicationManager.getMedications(),
                    morningMedications, "Morning:");
            break;
        case "today-a":
            List<DailyMedication> afternoonMedications = SubDailyManager.getAfternoonMedications();
            SubDailyManager.printTodayMedications(medicationManager.getMedications(),
                    afternoonMedications, "Afternoon:");
            break;
        case "today-e":
            List<DailyMedication> eveningMedications = SubDailyManager.getEveningMedications();
            SubDailyManager.printTodayMedications(medicationManager.getMedications(),
                    eveningMedications, "Evening:");
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + listTypeString);
        }
    }
}
