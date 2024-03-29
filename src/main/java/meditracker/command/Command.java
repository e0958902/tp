package meditracker.command;

import meditracker.exception.FileReadWriteException;
import meditracker.exception.InvalidArgumentException;
import meditracker.medication.MedicationManager;

public abstract class Command {
    private MedicationManager medicationManager;

    /**
     * Executes the command
     *
     * @param medicationManager      The MedicationList object representing the list of medications.
     */
    public abstract void execute(MedicationManager medicationManager)
            throws FileReadWriteException, InvalidArgumentException;

    /**
     * Returns the boolean to exit the program.
     *
     * @return False which continues program.
     */
    public boolean isExit() {
        return false;
    }
}
