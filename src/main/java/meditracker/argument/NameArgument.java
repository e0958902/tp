package meditracker.argument;

/**
 * Name of medication
 */
public class NameArgument extends Argument {
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
