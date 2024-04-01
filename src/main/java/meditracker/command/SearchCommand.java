package meditracker.command;

import meditracker.argument.*;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.library.LibraryManager;
import meditracker.ui.Ui;

import java.util.Map;

/**
 * Represents a command to search for medications from the local medication library based on a keyword.
 */
public class SearchCommand extends Command{
    public static final ArgumentList ARGUMENT_LIST = new ArgumentList(
            new NameArgument(true),
            new IllnessArgument(true),
            new SideEffectsArgument(true),
            new FindAllFieldsArgument(true)
    );

    public static final String HELP_MESSAGE = ArgumentHelper.getHelpMessage(CommandName.SEARCH, ARGUMENT_LIST);
    private final Map<ArgumentName, String> parsedArguments;

    /**
     * Constructs a new search command with the specified keyword.
     *
     * @param arguments The keyword to search for in the medication library.
     */

    public SearchCommand(String arguments)
            throws ArgumentNotFoundException, DuplicateArgumentFoundException, HelpInvokedException {
        parsedArguments = ARGUMENT_LIST.parse(arguments);
    }

    /**
     * Executes the search command to search for medications in the library based on the keyword.
     */
    @Override
    public void execute() throws NullPointerException, IllegalArgumentException {
        LibraryManager libraryManager = new LibraryManager();
        String keyword = "";
        try {
            if (parsedArguments.containsKey(ArgumentName.FIND_ALL_FIELDS)) {
                keyword = parsedArguments.get(ArgumentName.FIND_ALL_FIELDS).toLowerCase().trim();
                LibraryManager.searchLibrary(keyword);
            } else if (parsedArguments.containsKey(ArgumentName.NAME)) {
                keyword = parsedArguments.get(ArgumentName.NAME).toLowerCase().trim();
                libraryManager.findMedication(keyword);
            } else if (parsedArguments.containsKey(ArgumentName.ILLNESS)) {
                keyword = parsedArguments.get(ArgumentName.ILLNESS).toLowerCase().trim();
                libraryManager.findIllness(keyword);
            } else if (parsedArguments.containsKey(ArgumentName.SIDE_EFFECTS)) {
                keyword = parsedArguments.get(ArgumentName.SIDE_EFFECTS).toLowerCase().trim();
                libraryManager.findSideEffects(keyword);
            }
            libraryManager.printSearchResults();
        } catch (NullPointerException e) {
            Ui.showSearchKeywordNotFoundMessage();
        } catch (IllegalArgumentException e) {
            Ui.showLibraryIsCorruptedMessage();
        }
    }
}
