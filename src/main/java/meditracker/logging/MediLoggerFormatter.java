package meditracker.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;


//@@author annoy-o-mus-reused
// Reused from https://stackoverflow.com/a/53211725
// with minor modifications
/**
 * A class to format the MediLogger.
 */
public class MediLoggerFormatter extends Formatter {
    // ANSI escape codes
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREY = "\u001B[90m";
    public static final String ANSI_CYAN = "\u001B[36m";

    // Called for every console log message
    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();

        String loggingLevelName = record.getLevel().getName();
        switch (loggingLevelName) {
        case "WARNING":
            builder.append(ANSI_YELLOW);
            break;
        case "SEVERE":
            builder.append(ANSI_RED);
            break;
        case "INFO":
            builder.append(ANSI_GREY);
            break;
        default:
            builder.append(ANSI_CYAN);
        }

        builder.append("[");
        builder.append(loggingLevelName);
        builder.append("] ");

        builder.append(record.getMessage());

        Object[] params = record.getParameters();
        if (params != null) {
            builder.append("\t");
            for (int i = 0; i < params.length; i++) {
                builder.append(params[i]);
                if (i < params.length - 1) {
                    builder.append(", ");
                }
            }
        }

        builder.append(ANSI_RESET);
        builder.append("\n");
        return builder.toString();
    }
}
//@@author
