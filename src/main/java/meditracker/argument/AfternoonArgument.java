package meditracker.argument;

/**
 * Argument to represent afternoon
 */
public class AfternoonArgument extends Argument {

    /**
     * Constructs an AfternoonArgument with whether it is optional
     *
     * @param isOptional Whether the argument is optional
     */
    public AfternoonArgument(boolean isOptional) {
        super(
                ArgumentName.AFTERNOON,
                "-a",
                "Time of day: Afternoon",
                isOptional,
                false
        );
    }
}
