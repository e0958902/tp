package meditracker.exception;

public class InvalidArgumentException extends Exception {
    public InvalidArgumentException(String errorContext) {
        super(errorContext);
    }
}
