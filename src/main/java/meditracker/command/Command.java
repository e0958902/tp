package meditracker.command;

import meditracker.medication.MedicationManager;

public abstract class Command {
    private MedicationManager medicationManager;

    /**
     * Executes the command
     *
     */
    public abstract void execute() ;

    /**
     * Returns the boolean to exit the program.
     *
     * @return False which continues program.
     */
    public boolean isExit() {
        return false;
    }
}
