package meditracker.argument;

/**
 * Argument for quantity of medication.
 */
public class QuantityArgument extends Argument {

    /**
     * Constructs an QuantityArgument with whether the argument is optional.
     *
     * @param isOptional Whether the argument is optional.
     */
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
