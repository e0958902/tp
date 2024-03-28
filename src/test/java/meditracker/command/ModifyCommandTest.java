package meditracker.command;

import meditracker.DailyMedication;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;
import meditracker.medication.SubDailyManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModifyCommandTest {
    @Test
    void execute_inOrderArgument_expectMedicationModified()
            throws ArgumentNotFoundException, DuplicateArgumentFoundException {
        MedicationManager medicationManager = new MedicationManager();
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
                "",
                87);
        medicationManager.addMedication(medication);

        DailyMedication dailyMedication = new DailyMedication("Medication_A");
        SubDailyManager.addToMorningList(dailyMedication);

        String newName = "Medication_B";
        String inputString = "modify -l 1 -n " + newName;
        ModifyCommand command = new ModifyCommand(inputString);
        command.execute(medicationManager);

        Medication updatedMedication = medicationManager.getMedication(1);
        assertEquals(updatedMedication.getName(), newName);
    }

    @Test
    void execute_outOfOrderArgument_expectMedicationModified()
            throws ArgumentNotFoundException, DuplicateArgumentFoundException {
        MedicationManager medicationManager = new MedicationManager();
        Medication medication = new Medication(
                "Medication_A",
                60.0,
                500.0,
                300.0,
                null,
                null,
                "01/07/25",
                "morning",
                "cause_dizziness",
                "1",
                87);
        medicationManager.addMedication(medication);

        String newName = "Medication_B";
        String inputString = String.format("modify -n %s -l 1", newName);
        ModifyCommand command = new ModifyCommand(inputString);
        command.execute(medicationManager);

        Medication updatedMedication = medicationManager.getMedication(1);
        assertTrue(updatedMedication.getName().equals(newName));
    }
}
