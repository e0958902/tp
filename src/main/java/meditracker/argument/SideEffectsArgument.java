package meditracker.argument;

/**
 * Argument for the side effects of the medication.
 */
public class SideEffectsArgument extends Argument {
    public SideEffectsArgument(boolean isOptional) {
        super(
                ArgumentName.SIDE_EFFECTS,
                "-s",
                "Side effects of the medication",
                isOptional,
                true
        );
    }
}
