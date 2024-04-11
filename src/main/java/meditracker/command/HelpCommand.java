package meditracker.command;

import meditracker.ui.Ui;

/**
 * Represents a command to display help information for all available commands in MediTracker.
 *
 * This command lists all available commands along with a brief description of each.
 * It extends the Command class and overrides the execute method to perform the action.
 */
public class HelpCommand extends Command {

    /**
     * Executes the help command to display a list of available commands and descriptions.
     * It prints a header message followed by each command's name and description.
     */
    @Override
    public void execute() {
        System.out.println();
        Ui.showLine();
        System.out.println("Here are the commands you can use with MediTracker:" + System.lineSeparator());
        getCommandNamesAndDescription();
        System.out.println();
        System.out.println("For more details about each command, simply type in the command name.");
    }

    /**
     * Retrieves and prints the names and descriptions of all available commands, except for the UNKNOWN command.
     * Iterates through the CommandName enum values, skipping the UNKNOWN command,
     * and prints each command's name and its description to the console.
     */
    public void getCommandNamesAndDescription() {
        for (CommandName commandName : CommandName.values()) {
            if (commandName.equals(CommandName.UNKNOWN)) {
                continue;
            }
            String helpMessage = String.format("\t%-10s%s", commandName.value, commandName.description);
            System.out.println(helpMessage);
        }
    }
}
