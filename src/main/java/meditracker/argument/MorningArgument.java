package meditracker.argument;

/**
 * Argument to represent morning
 */
public class MorningArgument extends Argument {
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
