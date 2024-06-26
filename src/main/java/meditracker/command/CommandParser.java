package meditracker.command;

import meditracker.exception.ArgumentException;
import meditracker.exception.CommandNotFoundException;
import meditracker.exception.HelpInvokedException;

// @@author nickczh
/**
 * The Parser class parses user input commands into Command objects.
 */
public class CommandParser {
    private final CommandName commandName;
    private final String arguments;

    /**
     * Parses a full command string into a CommandName enum and arguments.
     *
     * @param fullCommand The full command string entered by the user.
     * @throws CommandNotFoundException Command specified not found
     */
    public CommandParser(String fullCommand) throws CommandNotFoundException {
        if (fullCommand.isEmpty()) {
            throw new CommandNotFoundException();
        }

        // Removes all trailing and leading whitespaces
        fullCommand = fullCommand.strip();

        String[] commands = fullCommand.split(" ", 2);
        arguments = (commands.length == 2) ? commands[1] : "";
        commandName = CommandName.valueOfLabel(commands[0]);
    }

    /**
     * Gets the Command object based on the CommandName enum type
     *
     * @return A Command object corresponding to the parsed command.
     * @throws HelpInvokedException When help argument is used or help message needed
     * @throws ArgumentException Argument flag specified not found,
     *              or when argument requires value but no value specified,
     *              or when unknown argument flags found in user input,
     *              or when duplicate argument flag found
     * @throws CommandNotFoundException Command specified not found
     */
    public Command getCommand()
            throws HelpInvokedException, ArgumentException, CommandNotFoundException {
        switch (commandName) {
        case EXIT:
            return new ExitCommand();
        case HELP:
            return new HelpCommand();
        case ADD:
            return new AddCommand(arguments);
        case MODIFY:
            return new ModifyCommand(arguments);
        case LIST:
            return new ListCommand(arguments);
        case VIEW:
            return new ViewCommand(arguments);
        case DELETE:
            return new DeleteCommand(arguments);
        case SEARCH:
            return new SearchCommand(arguments);
        case TAKE:
            return new TakeCommand(arguments);
        case UNTAKE:
            return new UntakeCommand(arguments);
        case SAVE:
            return new SaveCommand(arguments);
        case LOAD:
            return new LoadCommand(arguments);
        case UNKNOWN:
            // fall through
        default:
            throw new CommandNotFoundException();
        }
    }

    public CommandName getCommandName() {
        return commandName;
    }
}
// @@author
