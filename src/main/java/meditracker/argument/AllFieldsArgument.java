package meditracker.argument;

/**
 * Argument for the illness treated by the medicine.
 */
public class AllFieldsArgument extends Argument {
    public AllFieldsArgument(boolean isOptional) {
        super(
                ArgumentName.ALL_FIELDS,
                "-a",
                "Finds keyword from all fields of the library.",
                isOptional,
                true
        );
    }
}
