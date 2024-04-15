package meditracker.time;

import java.time.LocalTime;

/**
 * Manages a start and end time, to maintain information on a time range
 */
public class TimeRange {
    private final LocalTime start;
    private final LocalTime end;

    /**
     * Constructs a TimeRange using the start and end time
     *
     * @param start Start time of TimeRange
     * @param end End time of TimeRange
     */
    public TimeRange(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Checks whether a given time is within the time range
     *
     * @param time Given time to check the range
     * @return True if it is within the time range, else False
     */
    public boolean isWithinTimeRange(LocalTime time) {
        boolean isWithinStartAndEnd = time.isAfter(start) && time.isBefore(end);
        return start.equals(time) || end.equals(time) || isWithinStartAndEnd;
    }
}
