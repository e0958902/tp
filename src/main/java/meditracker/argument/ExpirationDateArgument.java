package meditracker.argument;

/**
 * Expiration date of medication.
 */
public class ExpirationDateArgument extends Argument {

    /**
     * Constructs an ExpirationDateArgument with whether the argument is optional.
     *
     * @param isOptional Whether the argument is optional.
     */
    public ExpirationDateArgument(boolean isOptional) {
        super(
                ArgumentName.EXPIRATION_DATE,
                "-e",
                "Expiration date of medication",
                isOptional,
                true
        );
    }
}
