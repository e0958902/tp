package meditracker.exception;

public class ArgumentNoValueException extends Exception {
    public ArgumentNoValueException(String errorContext) {
        super(errorContext);
    }
}
