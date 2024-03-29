package meditracker.exception;

public class CommandNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid MediTracker command! Please refer to the user guide.";
    }
}
