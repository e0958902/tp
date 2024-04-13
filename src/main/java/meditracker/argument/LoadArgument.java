package meditracker.argument;

/**
 * Contains the attributes of the load argument of the associated `load` command.
 */
public class LoadArgument extends Argument {
    /**
     * Sets up the attributes of the load argument of the associated `load` command.
     */
    public LoadArgument() {
        super(
                ArgumentName.LOAD_FILE,
                "-in",
                "File path (including .json extension) to load from",
                false,
                true
        );
    }
}
