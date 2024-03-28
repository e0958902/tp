package meditracker;

import meditracker.command.AddCommand;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.FileReadWriteException;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;
import meditracker.medication.SubDailyManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DailyMedicationManagerTest {
    @Test
    public void addDailyMedication_genericDailyMedication_dailyMedicationAdded() throws ArgumentNotFoundException,
            DuplicateArgumentFoundException {
        SubDailyManager.clearAllSubLists();
        String inputString = "add -n Medication_A -q 60.0 -d 500.0 -e 01/07/25 -f morning -dM 500.0 -dA 250.0 "
                + "-dE 300.0 -r cause_dizziness -rep 1";
        MedicationManager medicationManager = new MedicationManager();
        AddCommand command = new AddCommand(inputString);
        command.execute(medicationManager);

        List<DailyMedication> morningMedications = new ArrayList<>();
        List<DailyMedication> afternoonMedications = new ArrayList<>();
        List<DailyMedication> eveningMedications = new ArrayList<>();

        DailyMedication morningMeds = new DailyMedication("Medication_A");
        morningMedications.add(morningMeds);

        DailyMedication afternoonMeds = new DailyMedication("Medication_A");
        afternoonMedications.add(afternoonMeds);

        DailyMedication eveningMeds = new DailyMedication("Medication_A");
        eveningMedications.add(eveningMeds);


        List<Medication> medicationList = medicationManager.getMedications();

        SubDailyManager.printTodayMedications(medicationList,
                morningMedications, "Morning:");
        SubDailyManager.printTodayMedications(medicationList,
                afternoonMedications, "Afternoon:");
        SubDailyManager.printTodayMedications(medicationList,
                eveningMedications, "Evening:");

        int actualIndex = 1; // 1-based indexing
        DailyMedication morningMedicationTest = SubDailyManager.getMorningMedication(actualIndex);
        DailyMedication afternoonMedicationTest = SubDailyManager.getAfternoonMedication(actualIndex);
        DailyMedication eveningMedicationTest = SubDailyManager.getEveningMedication(actualIndex);

        assertEquals(morningMeds.toString(), morningMedicationTest.toString());
        assertEquals(afternoonMeds.toString(), afternoonMedicationTest.toString());
        assertEquals(eveningMeds.toString(), eveningMedicationTest.toString());
    }

    @Test
    public void takeDailyMedication_genericDailyMedication_dailyMedicationTaken() throws FileReadWriteException {
        SubDailyManager.clearAllSubLists();
        DailyMedication dailyMedication = new DailyMedication("TestMedication");
        assertFalse(dailyMedication.isTaken());
        SubDailyManager.addToMorningList(dailyMedication);

        int actualIndex = 1; // 1-based indexing
        DailyMedicationManager.takeDailyMedication(actualIndex);
        DailyMedication dailyMedicationTest = SubDailyManager.getMorningMedication(actualIndex);
        assertTrue(dailyMedicationTest.isTaken());
    }

    @Test
    public void untakeDailyMedication_genericDailyMedication_dailyMedicationNotTaken() throws FileReadWriteException {
        SubDailyManager.clearAllSubLists();
        DailyMedication dailyMedication = new DailyMedication("TestMedication");
        dailyMedication.take();
        assertTrue(dailyMedication.isTaken());
        SubDailyManager.addToMorningList(dailyMedication);

        int actualIndex = 1; // 1-based indexing
        DailyMedicationManager.untakeDailyMedication(actualIndex);
        DailyMedication dailyMedicationTest = SubDailyManager.getMorningMedication(actualIndex);
        assertFalse(dailyMedicationTest.isTaken());
    }
}
