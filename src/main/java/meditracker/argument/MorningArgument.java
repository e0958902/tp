package meditracker.argument;

/**
 * Argument to represent morning
 */
public class MorningArgument extends Argument {

    /**
     * Constructs an MorningArgument with whether it is optional
     *
     * @param isOptional Whether the argument is optional
     */
    public MorningArgument(boolean isOptional) {
        super(
                ArgumentName.MORNING,
                "-m",
                "Time of day: Morning",
                isOptional,
                false
        );
    }
}
