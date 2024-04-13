package meditracker.argument;

/**
 * Contains the attributes of the save argument of the associated `save` command.
 */
public class SaveArgument extends Argument {
    /**
     * Sets up the attributes of the save argument of the associated `save` command.
     */
    public SaveArgument() {
        super(
                ArgumentName.SAVE_FILE,
                "-o",
                "File path (including .json extension) to save to",
                true,
                true
        );
    }
}
