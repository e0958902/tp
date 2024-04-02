package meditracker.command;

import meditracker.dailymedication.DailyMedication;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;
import meditracker.time.Period;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModifyCommandTest {

    @BeforeEach
    public void resetMedicationManager() throws InvocationTargetException,
            IllegalAccessException, NoSuchMethodException {
        Method resetMedicationManagerMethod
                = MedicationManager.class.getDeclaredMethod("clearMedication");
        resetMedicationManagerMethod.setAccessible(true);
        resetMedicationManagerMethod.invoke(MedicationManager.class);
    }

    @Test
    void execute_inOrderArgument_expectMedicationModified()
            throws ArgumentNotFoundException, DuplicateArgumentFoundException, HelpInvokedException {
        Medication medication = new Medication(
                "Medication_A",
                60.0,
                10.0,
                10.0,
                10.0,
                "01/07/25",
                "cause_dizziness",
                1,
                87);
        MedicationManager.addMedication(medication);

        DailyMedication dailyMedication = new DailyMedication("Medication_A");
        DailyMedicationManager.addDailyMedication(dailyMedication, Period.MORNING);

        String newName = "Medication_B";
        String inputString = "modify -l 1 -n " + newName;
        ModifyCommand command = new ModifyCommand(inputString);
        command.execute();

        Medication updatedMedication = MedicationManager.getMedication(1);
        assertEquals(updatedMedication.getName(), newName);
    }

    @Test
    void execute_outOfOrderArgument_expectMedicationModified()
            throws ArgumentNotFoundException, DuplicateArgumentFoundException, HelpInvokedException {
        Medication medication = new Medication(
                "Medication_A",
                60.0,
                300.0,
                null,
                null,
                "01/07/25",
                "cause_dizziness",
                1,
                87);
        MedicationManager.addMedication(medication);

        String newName = "Medication_B";
        String inputString = String.format("modify -n %s -l 1", newName);
        ModifyCommand command = new ModifyCommand(inputString);
        command.execute();

        Medication updatedMedication = MedicationManager.getMedication(1);
        assertEquals(updatedMedication.getName(), newName);
    }
}
