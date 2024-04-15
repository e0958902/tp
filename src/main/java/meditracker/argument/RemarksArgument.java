package meditracker.argument;

/**
 * Argument for remarks of medication.
 */
public class RemarksArgument extends Argument {

    /**
     * Constructs an RemarksArgument with whether the argument is optional.
     *
     * @param isOptional Whether the argument is optional.
     */
    public RemarksArgument(boolean isOptional) {
        super(
                ArgumentName.REMARKS,
                "-r",
                "Additional remarks on medication",
                isOptional,
                true
        );
    }
}
