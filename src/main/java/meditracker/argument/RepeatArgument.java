package meditracker.argument;

/**
 * Represents the repeat frequency of medication.
 * Extends the Argument class.
 */
public class RepeatArgument extends Argument {

    /**
     * Constructs a RepeatArgument object with the specified optional status.
     * @param isOptional true if the argument is optional, false otherwise.
     */
    public RepeatArgument(boolean isOptional) {
        super(
             ArgumentName.REPEAT,
                "-rep",
                "How often to take medication (eg: Supply a number from 1 to 7)",
                isOptional,
                false
        );
    }
}
