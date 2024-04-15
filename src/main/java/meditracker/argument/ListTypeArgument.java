package meditracker.argument;

/**
 * Lists the summary of the medications in Medication
 */
public class ListTypeArgument extends Argument {

    /**
     * Constructs an ListTypeArgument with whether it is optional
     *
     * @param isOptional Whether the argument is optional
     */
    public ListTypeArgument(boolean isOptional) {
        super(
                ArgumentName.LIST_TYPE,
                "-t",
                "Lists medications accordingly",
                isOptional,
                true
        );
    }
}
