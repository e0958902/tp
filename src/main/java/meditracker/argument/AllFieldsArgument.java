package meditracker.argument;

/**
 * Argument for the illness treated by the medicine.
 */
public class AllFieldsArgument extends Argument {
    public AllFieldsArgument(boolean isOptional) {
        super(
                ArgumentName.ALL_FIELDS,
                "-a",
                "What do you want to find? (medications, illnesses, side effects)",
                "Finds keyword from all fields of the library.",
                isOptional,
                true
        );
    }
}
