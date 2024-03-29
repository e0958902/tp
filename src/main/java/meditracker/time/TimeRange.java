package meditracker.time;

import java.time.LocalTime;

public class TimeRange {
    private final LocalTime start;
    private final LocalTime end;

    public TimeRange(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public boolean isWithinTimeRange(LocalTime time) {
        boolean isWithinStartAndEnd = time.isAfter(start) && time.isBefore(end);
        return start.equals(time) || end.equals(time) || isWithinStartAndEnd;
    }
}
