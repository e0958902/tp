package meditracker.argument;

/**
 * Lists the summary of the medications in Medication
 */
public class ListTypeArgument extends Argument {
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
