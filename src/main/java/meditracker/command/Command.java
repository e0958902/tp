package meditracker.command;

public abstract class Command {

    /**
     * Executes the command
     *
     */
    public abstract void execute();

    /**
     * Returns the boolean to exit the program.
     *
     * @return False which continues program.
     */
    public boolean isExit() {
        return false;
    }
}
