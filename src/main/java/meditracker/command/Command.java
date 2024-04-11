package meditracker.command;

import meditracker.argument.ArgumentName;
import meditracker.exception.MediTrackerException;
import meditracker.time.MediTrackerTime;
import meditracker.time.Period;

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

    /**
     * Gets the period of day based on what period flag was set
     *
     * @param parsedArguments A map of argument name as key and the corresponding value
     * @return The period determined from the flag set
     */
    public static Period getPeriod(Map<ArgumentName, String> parsedArguments) {
        boolean isMorning = parsedArguments.get(ArgumentName.MORNING) != null;
        boolean isAfternoon = parsedArguments.get(ArgumentName.AFTERNOON) != null;
        boolean isEvening = parsedArguments.get(ArgumentName.EVENING) != null;
        Period period = Period.getPeriod(isMorning, isAfternoon, isEvening);
        if (period == Period.NONE) {
            period = Period.getPeriod(MediTrackerTime.getCurrentTime());
        }
        return period;
    }

    // @@author T0nyLin
    /**
     * Checks if repeat is within range of 1 to 7
     *
     * @param parsedArguments A map of argument name as key and the corresponding value
     * @return Returns the repeat in Integer
     * @throws MediTrackerException when the value is not within the specified range
     */
    public static int getRepeat(Map<ArgumentName, String> parsedArguments) throws MediTrackerException {
        int repeat;
        repeat = Integer.parseInt(parsedArguments.get(ArgumentName.REPEAT));
        if (repeat < 1 || repeat > 7) {
            throw new MediTrackerException("Provide a \"-rep\" number from 1 to 7");
        }
        return repeat;
    }
    // @@author
}
