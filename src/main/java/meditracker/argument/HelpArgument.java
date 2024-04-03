package meditracker.argument;

/**
 * Help argument to print help message
 */
public class HelpArgument extends Argument {
    protected HelpArgument() {
        super(
                ArgumentName.HELP,
                "-h",
                "Prints this help message",
                true,
                false
        );
    }
}
