package meditracker.ui;

import meditracker.argument.ArgumentHelper;
import meditracker.command.CommandName;
import meditracker.dailymedication.DailyMedication;
import meditracker.library.SearchResult;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;

import java.util.List;
import java.util.Scanner;

/**
 * The Ui class handles user interface-related operations.
 * It includes methods to display welcome messages, exit messages, and read user commands.
 */
public class Ui {
    static Scanner input = new Scanner(System.in);

    /**
     * Prevents defaulting to the public constructor
     * that allows instantiation of the Ui class
     */
    private Ui() {}

    /**
     * Displays the welcome message and introduction name.
     */
    public static void showWelcomeMessage() {
        printIntroName();
        showWelcome();
    }

    /**
     * Prints the introduction name banner.
     */
    public static void printIntroName() {

        // Solution below adapted by http://patorjk.com/software/taag/#p=display&f=Graffiti&t=Type%20Something%20
        System.out.println("                    __      ______                      __                      \n" +
                " /'\\_/`\\           /\\ \\  __/\\__  _\\                    /\\ \\                     \n" +
                "/\\      \\     __   \\_\\ \\/\\_\\/_/\\ \\/ _ __    __      ___\\ \\ \\/'\\      __   _ __  \n" +
                "\\ \\ \\__\\ \\  /'__`\\ /'_` \\/\\ \\ \\ \\ \\/\\`'__\\/'__`\\   /'___\\ \\ , <    /'__`\\/\\`'__\\\n"
                +
                " \\ \\ \\_/\\ \\/\\  __//\\ \\L\\ \\ \\ \\ \\ \\ \\ \\ \\//\\ \\L\\.\\_/\\ \\__/\\ \\ \\\\`\\ /\\" +
                "  __/\\" +
                " \\ \\/ \n" +
                "  \\ \\_\\\\ \\_\\ \\____\\ \\___,_\\ \\_\\ \\ \\_\\ \\_\\\\ \\__/.\\_\\ \\____\\\\ \\_\\ \\_\\ " +
                "\\____\\" +
                "\\ \\_\\ \n" +
                "   \\/_/ \\/_/\\/____/\\/__,_ /\\/_/  \\/_/\\/_/ \\/__/\\/_/\\/____/ \\/_/\\/_/\\/____/ \\/_/ \n" +
                "                                                                                \n" +
                "                                                                                ");
    }

    /**
     * Displays a line divider.
     */
    public static void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays the welcome message.
     */
    public static void showWelcome() {
        System.out.println("Welcome to MediTracker, your best companion to track your medicine intake.");
        System.out.println("Let's begin tracking!");
        System.out.println();
    }

    /**
     * Displays the exit message.
     */
    public static void showExitMessage() {
        System.out.println("Thank you for using MediTracker. Hope to see you again!");
    }

    public static void showSuccessMessage(String message) {
        System.out.print("SUCCESS: ");
        System.out.println(message);
    }

    public static void showErrorMessage(String message) {
        System.out.print("ERROR: ");
        System.out.println(message);
    }

    public static void showErrorMessage(Throwable throwable) {
        showErrorMessage(throwable.getMessage());
    }

    public static void showHelpMessage(CommandName commandName) {
        String helpMessage = ArgumentHelper.getHelpMessage(commandName);
        System.out.println(helpMessage);
    }

    public static void showWarningMessage(String message) {
        System.out.print("WARNING: ");
        System.out.println(message);
    }

    public static void showInfoMessage(String message) {
        System.out.print("INFO: ");
        System.out.println(message);
    }

    /**
     * Reads user input command.
     * @return The user input command as a String.
     */
    public static String readCommand() {
        System.out.print("meditracker> ");
        return input.nextLine();
    }

    /**
     * Prints the medication list
     *
     * @param medications The list of medications
     * @param <T> Generic class for code reusability
     */
    public static <T> void printMedsList(List<T> medications) {
        for (T medication : medications) {
            int numbering = medications.indexOf(medication) + 1;
            System.out.println("\t" + numbering + ". " + medication);
        }
    }

    /**
     * Prints all the medications in the medication list.
     *
     * @param medications Contains the list of medications.
     */
    public static void printMedicationList(List<Medication> medications) {
        int totalMedications = MedicationManager.getTotalMedications();
        if (totalMedications > 0) {
            String headerFormat = "   %-30s %-10s %-10s %-30s";
            String name = "Name";
            String quantity = "Quantity";
            String expiryDate = "Expiry";
            String remarks = "Remarks";
            System.out.println("You have " + totalMedications + " medications listed below.");
            System.out.printf(headerFormat + System.lineSeparator(), name, quantity, expiryDate, remarks);

            for (Medication medication : medications) {
                int numbering = medications.indexOf(medication) + 1;
                String bodyFormat = "%-30.30s %-10.1f %-10s %-30s ";
                System.out.printf(numbering + ". " + bodyFormat + System.lineSeparator(),
                        medication.getName(),
                        medication.getQuantity(),
                        medication.getExpiryDate(),
                        medication.getRemarks());
            }
            System.out.println("Your list of medications has been successfully shown!");
        }
    }

    /**
     * Prints a specific medication in the medication list
     *
     * @param medication Contains a medication in the medication list
     */
    public static void printSpecificMed(Medication medication) {
        System.out.printf("Name: %s" + System.lineSeparator() +
                        "Quantity: %.1f" + System.lineSeparator() +
                        "Expiry Date: %s" + System.lineSeparator() +
                        "Remarks: %s" + System.lineSeparator() +
                        "Morning Dosage: %.1f" + System.lineSeparator() +
                        "Afternoon Dosage: %.1f" + System.lineSeparator() +
                        "Evening Dosage: %.1f" + System.lineSeparator() +
                        "Repeat: %d" + System.lineSeparator(),
                medication.getName(),
                medication.getQuantity(),
                medication.getExpiryDate(),
                medication.getRemarks(),
                medication.getDosageMorning(),
                medication.getDosageAfternoon(),
                medication.getDosageEvening(),
                medication.getRepeat(),
                medication.getDayAdded());
        System.out.println();
    }

    /**
     * Prints the sub lists of dailyMedications based on the period
     *
     * @param medsList list of medications from MedicationManager
     * @param dailyMedications subList of dailyMedication
     * @param period Specified period of the day
     */
    public static void printMedsLists(List<Medication> medsList,
                                      List<DailyMedication> dailyMedications, String period) {
        int numbering = 0;
        assert medsList != null;
        assert dailyMedications != null;
        for (DailyMedication med: dailyMedications) {
            String name = med.getName();
            Double intakeDose = 0.0;
            int index = getIndex(medsList, name);
            Medication medication;
            try {
                medication = medsList.get(index);
            } catch (IndexOutOfBoundsException e) {
                return;
            }
            switch (period) {
            case "Morning:":
                intakeDose = medication.getDosageMorning();
                break;
            case "Afternoon:":
                intakeDose = medication.getDosageAfternoon();
                break;
            case "Evening:":
                intakeDose = medication.getDosageEvening();
                break;
            default:
                showErrorMessage("Medication not found.");
            }
            numbering++;
            System.out.println("\t" + numbering + ". " + med + " | " + intakeDose);
        }
        
    }

    /**
     * Gets index of the dailyMedication in the Main Medication list
     *
     * @param medsList list of medications from MedicationManager
     * @param name name of DailyMedication
     * @return index of the medication in main medication
     */
    private static int getIndex(List<Medication> medsList, String name) {
        int index = 0;
        for (Medication medication: medsList) {
            if (medication.getName().equals(name)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Prints when there are no search results found
     */
    public static void showNoSearchResultsMessage() {
        System.out.println("No search results found!");
    }


    /**
     * Prints when the library is corrupted
     */
    public static void showLibraryIsCorruptedMessage() {
        System.out.println("The library is corrupted! Please download the library from the website.");
    }

    /**
     * Prints the search results
     *
     * @param searchResults list of search results
     */
    public static void showSearchResults(List<SearchResult> searchResults) {
        System.out.println("Here are the search results:");

        for (int i = 0; i < searchResults.size(); i++) {
            System.out.println((i + 1) + ". " + searchResults.get(i));
        }
    }

    /**
     * Prints when there is no keyword provided for search command
     */
    public static void showSearchKeywordNotFoundMessage() {
        System.out.println("You have not provided a keyword to search for! Please try again.");
    }
}
