package meditracker.argument;

/**
 * List index of Medication or DailyMedication
 */
public class ListIndexArgument extends Argument {
    public ListIndexArgument(boolean isOptional) {
        super(
                ArgumentName.LIST_INDEX,
                "-l",
                "Index of item in list",
                isOptional,
                true
        );
    }
}
