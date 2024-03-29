package meditracker.argument;

/**
 * Argument to represent morning
 */
public class MorningArgument extends Argument {
    public MorningArgument(boolean isOptional) {
        super(
                ArgumentName.MORNING,
                "-m",
                null,
                "Time of day: Morning",
                isOptional,
                false
        );
    }
}
