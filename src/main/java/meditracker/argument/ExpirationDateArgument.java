package meditracker.argument;

/**
 * Expiration date of medication
 */
public class ExpirationDateArgument extends Argument {
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
