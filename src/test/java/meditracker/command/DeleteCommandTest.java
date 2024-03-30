package meditracker.command;

import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteCommandTest {

    @BeforeEach
    public void resetMedicationManager() throws InvocationTargetException,
            IllegalAccessException, NoSuchMethodException {
        Method resetMedicationManagerMethod
                = MedicationManager.class.getDeclaredMethod("clearMedication");
        resetMedicationManagerMethod.setAccessible(true);
        resetMedicationManagerMethod.invoke(MedicationManager.class);
    }

    @Test
    void execute_inOrderArgument_expectMedicationDeleted()
            throws ArgumentNotFoundException, DuplicateArgumentFoundException, HelpInvokedException {
        Medication medication = new Medication(
                "Medication_A",
                60.0,
                500.0,
                null,
                null,
                null,
                "01/07/25",
                "morning",
                "cause_dizziness",
                1,
                87);
        MedicationManager.addMedication(medication);

        MedicationManager.printAllMedications();

        String inputString = "delete -l 1";
        DeleteCommand command = new DeleteCommand(inputString);
        command.execute();

        assertThrows(IndexOutOfBoundsException.class, () -> MedicationManager.getMedication(1));
    }
}
