package meditracker;

import meditracker.command.AddCommand;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.MediTrackerException;
import meditracker.medication.MedicationManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddCommandTest {

    @BeforeEach
    public void resetMedicationManager() throws InvocationTargetException,
            IllegalAccessException, NoSuchMethodException {
        Method resetMedicationManagerMethod
                = MedicationManager.class.getDeclaredMethod("clearMedication");
        resetMedicationManagerMethod.setAccessible(true);
        resetMedicationManagerMethod.invoke(MedicationManager.class);
    }

    // 3 part format
    // methodBeingTested_conditionToTest_expectedOutcome
    @Test
    void execute_addCommand_expectOneMedication()
            throws ArgumentNotFoundException, DuplicateArgumentFoundException, HelpInvokedException,
            MediTrackerException {
        // setup lines
        String inputString = "add -n Medication_A -q 60.0 -e 01/07/25 -dM 500.0 -dA 250.0 "
                + "-r cause_dizziness -rep 1";
        AddCommand command = new AddCommand(inputString);
        command.execute();

        // actual test
        assertEquals(1, MedicationManager.getTotalMedications());
    }
}
