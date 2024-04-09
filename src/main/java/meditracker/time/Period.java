package meditracker.time;

import java.time.LocalTime;

// @@author T0nyLin
/**
 * Period enum to represent different time Periods and their corresponding TimeRange
 */
public enum Period {
    MORNING(LocalTime.MIDNIGHT, LocalTime.NOON.minusNanos(1)),
    AFTERNOON(LocalTime.NOON, LocalTime.of(18, 0).minusNanos(1)),
    EVENING(LocalTime.of(18, 0), LocalTime.MIDNIGHT.minusNanos(1)),
    UNKNOWN,
    NONE;

    public final TimeRange timeRange;

    /**
     * Constructs default Period with null timeRange attribute
     */
    Period() {
        timeRange = null;
    }

    /**
     * Constructs Period with a start and end LocalTime
     *
     * @param start time of the period
     * @param end time of the period
     */
    Period(LocalTime start, LocalTime end) {
        timeRange = new TimeRange(start, end);
    }

    /**
     * Gets the time period based on which boolean value is set
     *
     * @param isMorning Boolean value for if it is morning
     * @param isAfternoon Boolean value for if it is afternoon
     * @param isEvening Boolean value for if it is evening
     * @return Enum value for which time Period it is
     */
    public static Period getPeriod(boolean isMorning, boolean isAfternoon, boolean isEvening) {
        if (!isMorning && !isAfternoon && !isEvening) {
            return NONE;
        } else if (isMorning && !isAfternoon && !isEvening) {
            return MORNING;
        } else if (!isMorning && isAfternoon && !isEvening) {
            return AFTERNOON;
        } else if (!isMorning && !isAfternoon && isEvening) {
            return EVENING;
        }
        return UNKNOWN;
    }

    /**
     * Gets the time period based on the given time
     *
     * @param time LocalTime object representing the time to check
     * @return Enum value for which time Period it is
     */
    public static Period getPeriod(LocalTime time) {
        if (MORNING.timeRange.isWithinTimeRange(time)) {
            return MORNING;
        } else if (AFTERNOON.timeRange.isWithinTimeRange(time)) {
            return AFTERNOON;
        } else if (EVENING.timeRange.isWithinTimeRange(time)) {
            return EVENING;
        }
        return UNKNOWN;
    }
}
// @@author
