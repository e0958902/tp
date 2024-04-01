package meditracker.command;

import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListCommandTest {

    @Test
    void execute_listCommand_expect() {
        String medicationName = "Medication_B";

        Medication medication = new Medication(
                medicationName,
                30.0,
                null,
                null,
                null,
                "01/08/25",
                "for_flu_or_allergy",
                1,
                87
        );

        MedicationManager.addMedication(medication);

        assert MedicationManager.getTotalMedications() > 0 : "Total medications in medication must be greater " +
                "than 0 after adding in" + medicationName;

        Medication addedMedication = MedicationManager.getMedication(1);
        assertEquals(addedMedication.getName(), medicationName);
    }
}
