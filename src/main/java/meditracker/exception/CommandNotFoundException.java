package meditracker.exception;

// @@author nickczh
public class CommandNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid MediTracker command! Please refer to the user guide.";
    }
}
// @@author
