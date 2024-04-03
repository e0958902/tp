package meditracker.argument;

/**
 * Argument for the illness treated by the medicine.
 */
public class IllnessArgument extends Argument {
    public IllnessArgument(boolean isOptional) {
        super(
                ArgumentName.ILLNESS,
                "-i",
                "What illness is the medicine used for?",
                "Illness treated by the medicine",
                isOptional,
                true
        );
    }
}
