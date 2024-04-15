package meditracker.argument;

/**
 * Remarks for medication
 */
public class RemarksArgument extends Argument {

    /**
     * Constructs an RemarksArgument with whether it is optional
     *
     * @param isOptional Whether the argument is optional
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
