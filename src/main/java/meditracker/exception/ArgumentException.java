package meditracker.exception;

/**
 * Exception thrown when ArgumentParser encounters an argument-related error
 */
public class ArgumentException extends Exception {
    public ArgumentException(String errorContext) {
        super(errorContext);
    }
}
