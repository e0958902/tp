package meditracker.argument;

/**
 * Argument to represent evening
 */
public class EveningArgument extends Argument {
    public EveningArgument(boolean isOptional) {
        super(
                ArgumentName.EVENING,
                "-e",
                "Time of day: Evening",
                isOptional,
                false
        );
    }
}
