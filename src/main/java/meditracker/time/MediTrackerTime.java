package meditracker.time;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TimeZone;

import meditracker.exception.InvalidSimulatedTimeException;

// The time travel implementation is inspired by
// https://www.baeldung.com/java-override-system-time
/**
 * The single source of truth for time in MediTracker.
 */
public class MediTrackerTime {
    private static boolean isSimulatedTime = false;
    private static Clock simulatedClock = null;

    /**
     * Gets the current system time, or if the simulation flag is enabled, the simulated time.
     *
     * @return System time. Simulated time if -sim flag is enabled in CLI.
     */
    public static LocalTime getCurrentTime() {
        if (isSimulatedTime) {
            return LocalTime.now(simulatedClock);
        } else {
            return LocalTime.now();
        }
    }

    /**
     * Gets the current system date, or if the simulation flag is enabled, the simulated date.
     *
     * @return System date. Simulated date if -sim flag is enabled in CLI
     */
    public static LocalDate getCurrentDate() {
        if (isSimulatedTime) {
            return LocalDate.now(simulatedClock);
        } else {
            return LocalDate.now();
        }
    }

    /**
     * Sets up the program up to get the time from either the simulated time or the system time.
     * The simulated time will only override the system time if there is a -sim flag and a valid
     *     date and time format is provided (as per `time.Instant.parse`).
     *
     * @param cliInput The input associated with the `-sim` flag
     * @throws InvalidSimulatedTimeException When the format of the input does not match that required by the
     *     `time.Instant.parse` method
     */
    private static void setMediTrackerTime(String cliInput) throws InvalidSimulatedTimeException {
        Instant setTime;
        try {
            setTime = Instant.parse(cliInput);
        } catch (DateTimeParseException e) {
            throw new InvalidSimulatedTimeException("Invalid Date and Time Format!");
        }
        assert (setTime != null);

        // https://stackoverflow.com/a/11399538
        TimeZone tz = TimeZone.getTimeZone(ZoneId.systemDefault());
        int offset = tz.getOffset(new Date().getTime());
        setTime = setTime.minusMillis(offset);

        simulatedClock = Clock.fixed(setTime, ZoneId.systemDefault().normalized());
        isSimulatedTime = true;

        LocalDateTime localDateTime = LocalDateTime.now(simulatedClock);
        System.out.println("Developer Feature: Simulated Time Enabled. The time is now fixed at " + localDateTime);
    }

    /**
     * Process the CLI input to check for the need to set up simulated time.
     *
     * @param cliArguments The commandline arguments.
     * @throws InvalidSimulatedTimeException If there is no argument provided with the `-sim` flag.
     */
    public static void setUpSimulatedTime(String[] cliArguments) throws InvalidSimulatedTimeException {
        Iterator<String> stringIterator = Arrays.stream(cliArguments).iterator();
        while (stringIterator.hasNext()) {
            String s = stringIterator.next();
            if (s.equals("-sim")) {
                try {
                    String dateTimeString = stringIterator.next();
                    MediTrackerTime.setMediTrackerTime(dateTimeString);
                    return;
                } catch (NoSuchElementException e) {
                    throw new InvalidSimulatedTimeException("Empty field after -sim flag!");
                }
            }
        }
    }
}
