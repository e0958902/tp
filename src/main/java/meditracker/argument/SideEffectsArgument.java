package meditracker.argument;

/**
 * Argument for the side effects of the medication.
 */
public class SideEffectsArgument extends Argument {
    public SideEffectsArgument(boolean isOptional) {
        super(
                ArgumentName.SIDE_EFFECTS,
                "-s",
                "What are the side effects of the medication?",
                "Side effects of the medication",
                isOptional,
                true
        );
    }
}
