package meditracker.exception;

public class UnknownArgumentFoundException extends Exception {
    public UnknownArgumentFoundException(String errorContext) {
        super(errorContext);
    }
}
