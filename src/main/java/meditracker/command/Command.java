package meditracker.command;

import meditracker.argument.ArgumentName;

import java.util.Map;

public abstract class Command {

    /**
     * Executes the command
     *
     */
    public abstract void execute();

    /**
     * Returns the boolean to exit the program.
     *
     * @return False which continues program.
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Gets the list index from a map of argument name and value and parses as integer
     *
     * @param parsedArguments A map of argument name as key and the corresponding value
     * @return The integer parsed or 0 if unable to parse
     */
    public static int getListIndex(Map<ArgumentName, String> parsedArguments) {
        String listIndexString = parsedArguments.get(ArgumentName.LIST_INDEX);
        try {
            return Integer.parseInt(listIndexString);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
