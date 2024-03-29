package meditracker.dailymedication;

//@@author nickczh
//with appropriate modifications
public enum Repeat {
    DAILY(1),
    EVERY_OTHER_DAY(2),
    EVERY_3_DAYS(3),
    EVERY_4_DAYS(4),
    EVERY_WEEK(7);
    public final int value;

    Repeat(int value) {
        this.value = value;
    }

    public static Repeat repeatDays(int days) {
        for (Repeat r : values()) {
            if (r.value == days) {
                return r;
            }
        }
        return null;
    }
}
