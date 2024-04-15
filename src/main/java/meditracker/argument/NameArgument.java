package meditracker.argument;

/**
 * Argument for name of medication.
 */
public class NameArgument extends Argument {

    /**
     * Constructs an NameArgument with whether the argument is optional.
     *
     * @param isOptional Whether the argument is optional.
     */
    public NameArgument(boolean isOptional) {
        super(
                ArgumentName.NAME,
                "-n",
                "Name of medication",
                isOptional,
                true
        );
    }
}
