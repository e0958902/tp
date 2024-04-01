package meditracker.argument;

/**
 * Argument for the illness treated by the medicine.
 */
public class SearchAcrossAllFieldsArgument extends Argument {
    public SearchAcrossAllFieldsArgument(boolean isOptional) {
        super(
                ArgumentName.SEARCH_ALL_FIELDS,
                "-a",
                "What do you want to find? (medications, illnesses, side effects)",
                "Finds keyword from all fields of the library.",
                isOptional,
                true
        );
    }
}
