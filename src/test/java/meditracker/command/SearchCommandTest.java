package meditracker.command;

import meditracker.exception.ArgumentException;
import meditracker.exception.HelpInvokedException;
import meditracker.library.LibraryManager;
import meditracker.library.SearchResult;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchCommandTest {

    @Test
    void execute_searchCommandSearchAllFields_expectSearchResult()
            throws HelpInvokedException, ArgumentException {
        String keyword = "paracetamol";
        String inputString = "search -a " + keyword;

        SearchResult searchResult = new SearchResult(
                "Paracetamol",
                "Fever, Pain",
                "Nausea, Liver damage (in high doses)");

        LibraryManager libraryManager = new LibraryManager();
        SearchCommand command = new SearchCommand(inputString);
        command.execute();

        List<SearchResult> expectedSearchResults = new ArrayList<>();
        expectedSearchResults.add(searchResult);

        List<SearchResult> searchResults = new ArrayList<>();
        libraryManager.searchLibrary(searchResults, keyword);

        assertEquals(expectedSearchResults.toString(), searchResults.toString());
    }

    @Test
    void execute_searchCommandSearchMedicationName_expectSearchResult()
            throws HelpInvokedException, ArgumentException {
        String keyword = "paracetamol";
        String inputString = "search -n " + keyword;

        SearchResult searchResult = new SearchResult(
                "Paracetamol",
                "Fever, Pain",
                "Nausea, Liver damage (in high doses)");

        LibraryManager libraryManager = new LibraryManager();
        SearchCommand command = new SearchCommand(inputString);
        command.execute();

        List<SearchResult> expectedSearchResults = new ArrayList<>();
        expectedSearchResults.add(searchResult);

        List<SearchResult> searchResults = new ArrayList<>();
        libraryManager.findMedication(searchResults, keyword);

        assertEquals(expectedSearchResults.toString(), searchResults.toString());
    }

    @Test
    void execute_searchCommandSearchIllness_expectSearchResults()
            throws HelpInvokedException, ArgumentException {
        String keyword = "fever";
        String inputString = "search -i " + keyword;

        SearchResult firstSearchResult = new SearchResult(
                "Aspirin",
                "Headache, Fever",
                "Stomach irritation, Bleeding");
        SearchResult secondSearchResult = new SearchResult(
                "Paracetamol",
                "Fever, Pain",
                "Nausea, Liver damage (in high doses)");

        LibraryManager libraryManager = new LibraryManager();
        SearchCommand command = new SearchCommand(inputString);
        command.execute();

        List<SearchResult> expectedSearchResults = new ArrayList<>();
        expectedSearchResults.add(firstSearchResult);
        expectedSearchResults.add(secondSearchResult);

        List<SearchResult> searchResults = new ArrayList<>();
        libraryManager.findIllness(searchResults, keyword);

        assertEquals(expectedSearchResults.toString(), searchResults.toString());
    }

    @Test
    void execute_searchCommandSearchSideEffect_expectSearchResult()
            throws HelpInvokedException, ArgumentException {
        String keyword = "fatigue";
        String inputString = "search -s " + keyword;

        SearchResult firstSearchResult = new SearchResult(
                "Loratadine",
                "Allergy symptoms",
                "Headache, Fatigue");

        SearchResult secondSearchResult = new SearchResult(
                "Losartan",
                "High blood pressure",
                "Dizziness, Fatigue");

        SearchResult thirdSearchResult = new SearchResult(
                "Metoprolol",
                "High blood pressure",
                "Dizziness, Fatigue");

        LibraryManager libraryManager = new LibraryManager();
        SearchCommand command = new SearchCommand(inputString);
        command.execute();

        List<SearchResult> expectedSearchResults = new ArrayList<>();
        expectedSearchResults.add(firstSearchResult);
        expectedSearchResults.add(secondSearchResult);
        expectedSearchResults.add(thirdSearchResult);

        List<SearchResult> searchResults = new ArrayList<>();
        libraryManager.searchLibrary(searchResults, keyword);

        assertEquals(expectedSearchResults.toString(), searchResults.toString());
    }
}
