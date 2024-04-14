package meditracker.exception;

public class ArgumentException extends Exception {
    public ArgumentException(String errorContext) {
        super(errorContext);
    }
}
