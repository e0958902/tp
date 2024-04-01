package meditracker.argument;

public class FindAllFieldsArgument extends Argument {
    public FindAllFieldsArgument(boolean isOptional) {
        super(
                ArgumentName.FIND_ALL_FIELDS,
                "-a",
                "What do you want to find? (medications, illnesses, side effects)",
                "Finds keyword from all fields of the library.",
                isOptional,
                true
                );
    }
}
