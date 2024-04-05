package meditracker.command;

import meditracker.argument.ArgumentHelper;
import meditracker.argument.ArgumentList;
import meditracker.argument.ArgumentName;
import meditracker.argument.IllnessArgument;
import meditracker.argument.NameArgument;
import meditracker.argument.AllFieldsArgument;
import meditracker.argument.SideEffectsArgument;
import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.UnknownArgumentFoundException;
import meditracker.library.LibraryManager;
import meditracker.library.SearchResult;
import meditracker.ui.Ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// @@author kyuichyi
/**
 * Represents a command to search for medications from the local medication library based on a keyword.
 */
public class SearchCommand extends Command{
    /*
     * The argumentList contains all the arguments needed for searching for medication from the medication library.
     */
    public static final ArgumentList ARGUMENT_LIST = new ArgumentList(
            new NameArgument(true),
            new IllnessArgument(true),
            new SideEffectsArgument(true),
            new AllFieldsArgument(true)
    );

    public static final String HELP_MESSAGE = ArgumentHelper.getHelpMessage(CommandName.SEARCH, ARGUMENT_LIST);
    private final Map<ArgumentName, String> parsedArguments;

    /**
     * Constructs a new search command with the specified keyword.
     *
     * @param arguments The keyword to search for in the medication library.
     * @throws ArgumentNotFoundException Argument flag specified not found
     * @throws ArgumentNoValueException When argument requires value but no value specified
     * @throws DuplicateArgumentFoundException Duplicate argument flag found
     * @throws HelpInvokedException When help argument is used or help message needed
     * @throws UnknownArgumentFoundException When unknown argument flags found in user input
     */
    public SearchCommand(String arguments)
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
    }

    /**
     * Executes the search command based on the keyword.
     *
     * @throws NullPointerException if the keyword is not found.
     * @throws IllegalArgumentException if the library is corrupted.
     */
    @Override
    public void execute() throws NullPointerException, IllegalArgumentException {
        LibraryManager libraryManager = new LibraryManager();
        List<SearchResult> searchResults = new ArrayList<>();
        try {
            String keyword;
            if (parsedArguments.containsKey(ArgumentName.ALL_FIELDS)) {
                keyword = parsedArguments.get(ArgumentName.ALL_FIELDS).toLowerCase().trim();
                libraryManager.searchLibrary(searchResults, keyword);
            } else if (parsedArguments.containsKey(ArgumentName.NAME)) {
                keyword = parsedArguments.get(ArgumentName.NAME).toLowerCase().trim();
                libraryManager.findMedication(searchResults, keyword);
            } else if (parsedArguments.containsKey(ArgumentName.ILLNESS)) {
                keyword = parsedArguments.get(ArgumentName.ILLNESS).toLowerCase().trim();
                libraryManager.findIllness(searchResults, keyword);
            } else if (parsedArguments.containsKey(ArgumentName.SIDE_EFFECTS)) {
                keyword = parsedArguments.get(ArgumentName.SIDE_EFFECTS).toLowerCase().trim();
                libraryManager.findSideEffects(searchResults, keyword);
            }
            libraryManager.printSearchResults(searchResults);
        } catch (NullPointerException e) {
            Ui.showSearchKeywordNotFoundMessage();
        } catch (IllegalArgumentException e) {
            Ui.showLibraryIsCorruptedMessage();
        }
    }
}
// @@author
