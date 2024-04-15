package meditracker.argument;

/**
 * Argument to represent evening.
 */
public class EveningArgument extends Argument {

    /**
     * Constructs an EveningArgument with whether the argument is optional.
     *
     * @param isOptional Whether the argument is optional.
     */
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
