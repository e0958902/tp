package meditracker.argument;

/**
 * List index of Medication or DailyMedication.
 */
public class ListIndexArgument extends Argument {

    /**
     * Constructs an ListIndexArgument with whether the argument is optional.
     *
     * @param isOptional Whether the argument is optional.
     */
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
