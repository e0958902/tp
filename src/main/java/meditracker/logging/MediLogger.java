package meditracker.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main Logging class to get the logger for logging outputs to the console.
 */
public class MediLogger {
    private static final String MEDILOGGER_NAME = "MediLogger";
    private static Logger mediLogger = null;

    /**
     * Gets the default logger for the MediTracker project.
     * If for some reason the default logger is not initialised when this function is called,
     * initialise the default logger and logs a warning.
     *
     * @return The default logger for MediTracker project.
     */
    public static Logger getMediLogger() {
        if (mediLogger == null) {
            initialiseMediLogger();
        }
        assert mediLogger != null;
        return mediLogger;
    }

    /**
     * Initialises the default logger to be used for the MediTracker project.
     * The logger will send all the logging messages to the console.
     * Currently, no support to write to a dedicated log file.
     */
    public static void initialiseMediLogger() {
        if (mediLogger == null) {
            mediLogger = Logger.getLogger(MEDILOGGER_NAME);
            configureMediLogger();
            mediLogger.info("MediLogger initialised");
        }
        assert mediLogger != null;
    }

    /**
     * Configures the default MediLogger to print in a certain way.
     */
    private static void configureMediLogger() {
        //@@author annoy-o-mus-reused
        // Reused from https://stackoverflow.com/a/53211725
        // with minor modifications
        mediLogger.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();
        Formatter formatter = new MediLoggerFormatter();

        handler.setFormatter(formatter);
        mediLogger.addHandler(handler);

        mediLogger.setLevel(Level.INFO);
        //@@author
    }
}
