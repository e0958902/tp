package meditracker.argument;

public class SaveArgument extends Argument {

    public SaveArgument() {
        super(
                ArgumentName.SAVE_FILE,
                "-o",
                "",
                "File path (including .json extension) to save to",
                true,
                true
        );
    }
}
