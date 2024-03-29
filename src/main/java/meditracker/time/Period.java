package meditracker.time;

public enum Period {
    MORNING,
    AFTERNOON,
    EVENING,
    UNKNOWN,
    NONE;

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
}
