package meditracker;

import meditracker.command.AddCommand;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.medication.MedicationManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddCommandTest {

    // 3 part format
    // methodBeingTested_conditionToTest_expectedOutcome
    @Test
    void execute_addCommand_expectOneMedication()
            throws ArgumentNotFoundException, DuplicateArgumentFoundException, HelpInvokedException {
        // setup lines
        String inputString = "add -n Medication_A -q 60.0 -d 500.0 -e 01/07/25 -f morning -dM 500.0 -dA 250.0 "
                + "-r cause_dizziness -rep 1";
        MedicationManager medicationManager = new MedicationManager();
        AddCommand command = new AddCommand(inputString);
        command.execute(medicationManager);

        // actual test
        assertEquals(1, medicationManager.getTotalMedications());
    }
}
