package meditracker.library;

import meditracker.ui.Ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Manages the searching of medications in the medication library.
 */
public class LibraryManager {

    private static final String FILE_PATH = "/medicationLibrary.txt";
    private static final List<SearchResult> medicationLibrary = new ArrayList<>();
    private static List<SearchResult> searchResults = new ArrayList<>();

    /**
     * Static initializer block.
     *
     * This block of code is executed when the LibraryManager class is loaded into memory.
     * It calls the loadMedicationLibrary method to load the medication library from a text file.
     * This ensures that the medication library is loaded only once, regardless of how many instances of LibraryManager
     * are created.
     */
    static {
        loadMedicationLibrary();
    }

    public LibraryManager() {
    }

    /**
     * Loads the medication library from a text file.
     */
    private static void loadMedicationLibrary() {
        InputStream file = LibraryManager.class.getResourceAsStream(FILE_PATH);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] medicationDetails = line.split("\\|");
            medicationLibrary.add(new SearchResult(medicationDetails[0], medicationDetails[1], medicationDetails[2]));
        }
    }

    /**
     * Searches for medications in the library based on a keyword.
     *
     * @param keyword The keyword to search for in the medication library.
     */
    public static void searchLibrary(String keyword) {
        if (keyword.isEmpty()) {
            throw new NullPointerException();
        }
        searchResults.clear();
        for (int i = 0; i < medicationLibrary.size(); i++) {
            SearchResult medication = medicationLibrary.get(i);
            if (medication.getAllMedicationDetails().toLowerCase().contains(keyword)) {
                searchResults.add(medication);
            }
        }
    }


    /**
     * Searches medication names in the library based on a keyword.
     *
     * @param keyword The keyword to search for in the medication library.
     */
    public void findMedication(String keyword) {
        if (keyword.isEmpty()) {
            throw new NullPointerException();
        }
        searchResults.clear();
        for (int i = 0; i < medicationLibrary.size(); i++) {
            SearchResult medication = medicationLibrary.get(i);
            if (medication.getName().toLowerCase().contains(keyword)) {
                searchResults.add(medication);
            }
        }
    }

    /**
     * Searches for medications based on the illness they treat.
     *
     * @param keyword The keyword to search for in the medication library.
     */
    public void findIllness(String keyword) {
        if (keyword.isEmpty()) {
            throw new NullPointerException();
        }
        searchResults.clear();
        for (int i = 0; i < medicationLibrary.size(); i++) {
            SearchResult medication = medicationLibrary.get(i);
            if (medication.getIllness().toLowerCase().contains(keyword)) {
                searchResults.add(medication);
            }
        }
    }

    /**
     * Searches for medications based on their side effects.
     *
     * @param keyword The keyword to search for in the medication library.
     */
    public void findSideEffects(String keyword) {
        if (keyword.isEmpty()) {
            throw new NullPointerException();
        }
        searchResults.clear();
        for (int i = 0; i < medicationLibrary.size(); i++) {
            SearchResult medication = medicationLibrary.get(i);
            if (medication.getSideEffects().toLowerCase().contains(keyword)) {
                searchResults.add(medication);
            }
        }
    }

    /**
     * Prints the search results to the user interface.
     */
    public void printSearchResults() {
        if (searchResults.isEmpty()) {
            Ui.showNoSearchResultsMessage();
        } else {
            Ui.showSearchResults(searchResults);
        }
    }
}
