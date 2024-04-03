package meditracker.argument;

/**
 * Quantity of medication
 */
public class QuantityArgument extends Argument {
    public QuantityArgument(boolean isOptional) {
        super(
                ArgumentName.QUANTITY,
                "-q",
                "Quantity of medication",
                isOptional,
                true
        );
    }
}
