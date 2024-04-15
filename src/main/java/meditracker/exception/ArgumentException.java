package meditracker.exception;

/**
 * Exception thrown when ArgumentParser encounters an argument-related error.
 */
public class ArgumentException extends Exception {

    /**
     * Constructs a InsufficientQuantityException with the error context.
     *
     * @param errorContext Error context.
     */
    public ArgumentException(String errorContext) {
        super(errorContext);
    }
}
