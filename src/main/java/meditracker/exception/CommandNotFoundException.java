package meditracker.exception;

import meditracker.command.CommandParser;
import meditracker.command.HelpCommand;

// @@author nickczh
public class CommandNotFoundException extends Exception {
    HelpCommand helpCommand;
    @Override
    public String getMessage() {
        helpCommand.execute();
        return "Invalid MediTracker command.";
    }

    public CommandNotFoundException() {
        helpCommand = new HelpCommand();
    }
}
// @@author
