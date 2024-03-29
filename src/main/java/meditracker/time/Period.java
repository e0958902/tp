package meditracker.time;

import java.time.LocalTime;

public enum Period {
    MORNING(LocalTime.MIDNIGHT, LocalTime.NOON.minusNanos(1)),
    AFTERNOON(LocalTime.NOON, LocalTime.of(18, 0).minusNanos(1)),
    EVENING(LocalTime.of(18, 0), LocalTime.MIDNIGHT.minusNanos(1)),
    UNKNOWN,
    NONE;

    public final TimeRange timeRange;

    Period() {
        timeRange = null;
    }

    Period(LocalTime start, LocalTime end) {
        timeRange = new TimeRange(start, end);
    }

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
