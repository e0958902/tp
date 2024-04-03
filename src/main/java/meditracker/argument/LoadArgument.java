package meditracker.argument;

public class LoadArgument extends Argument {
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
