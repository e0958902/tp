package meditracker.exception;

import meditracker.command.HelpCommand;

// @@author nickczh

/**
 * Represents an exception that is thrown when an invalid command is entered in MediTracker.
 * This exception triggers the display of the help menu by executing the HelpCommand.
 * It extends the Exception class.
 */
public class CommandNotFoundException extends Exception {
    HelpCommand helpCommand;

    /**
     * Constructs a CommandNotFoundException and initializes the HelpCommand instance.
     */
    public CommandNotFoundException() {
        helpCommand = new HelpCommand();
    }

    /**
     * Overrides the getMessage method to execute the help command and return an error message.
     * When this exception is caught, it triggers the display of the help menu
     * by executing the HelpCommand before returning the error message.
     *
     * @return The error message indicating an invalid MediTracker command.
     */
    @Override
    public String getMessage() {
        helpCommand.execute();
        return "Invalid MediTracker command.";
    }

}
// @@author
