package meditracker;

import meditracker.command.AddCommand;
import meditracker.dailymedication.DailyMedication;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.dailymedication.Period;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.FileReadWriteException;
import meditracker.exception.HelpInvokedException;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DailyMedicationManagerTest {

    @BeforeEach
    public void resetDailyMedicationManager() throws InvocationTargetException,
            IllegalAccessException, NoSuchMethodException {
        Method resetDailyMedicationManagerMethod
                = DailyMedicationManager.class.getDeclaredMethod("clearDailyMedication");
        resetDailyMedicationManagerMethod.setAccessible(true);
        resetDailyMedicationManagerMethod.invoke(DailyMedicationManager.class);
    }
    @Test
    public void addDailyMedication_genericDailyMedication_dailyMedicationAdded()
            throws ArgumentNotFoundException, DuplicateArgumentFoundException, HelpInvokedException {
        String inputString = "add -n Medication_A -q 60.0 -d 500.0 -e 01/07/25 -f morning -dM 500.0 -dA 250.0 "
                + "-dE 300.0 -r cause_dizziness -rep 1";
        AddCommand command = new AddCommand(inputString);
        command.execute();

        List<DailyMedication> morningMedications = new ArrayList<>();
        List<DailyMedication> afternoonMedications = new ArrayList<>();
        List<DailyMedication> eveningMedications = new ArrayList<>();

        DailyMedication morningMeds = new DailyMedication("Medication_A");
        morningMedications.add(morningMeds);

        DailyMedication afternoonMeds = new DailyMedication("Medication_A");
        afternoonMedications.add(afternoonMeds);

        DailyMedication eveningMeds = new DailyMedication("Medication_A");
        eveningMedications.add(eveningMeds);


        List<Medication> medicationList = MedicationManager.getMedications();

        DailyMedicationManager.printTodayMedications(medicationList,
                morningMedications, "Morning:");
        DailyMedicationManager.printTodayMedications(medicationList,
                afternoonMedications, "Afternoon:");
        DailyMedicationManager.printTodayMedications(medicationList,
                eveningMedications, "Evening:");

        int actualIndex = 1; // 1-based indexing
        DailyMedication morningMedicationTest = DailyMedicationManager.getDailyMedication(actualIndex, Period.MORNING);
        DailyMedication afternoonMedicationTest =
                DailyMedicationManager.getDailyMedication(actualIndex, Period.AFTERNOON);
        DailyMedication eveningMedicationTest = DailyMedicationManager.getDailyMedication(actualIndex, Period.EVENING);

        assertEquals(morningMeds.toString(), morningMedicationTest.toString());
        assertEquals(afternoonMeds.toString(), afternoonMedicationTest.toString());
        assertEquals(eveningMeds.toString(), eveningMedicationTest.toString());
    }

    @Test
    public void takeDailyMedication_genericDailyMedication_dailyMedicationTaken() throws FileReadWriteException {
        DailyMedication dailyMedication = new DailyMedication("TestMedication");
        assertFalse(dailyMedication.isTaken());
        DailyMedicationManager.addDailyMedication(dailyMedication, Period.MORNING);

        int actualIndex = 1; // 1-based indexing
        DailyMedicationManager.takeDailyMedication(actualIndex);
        DailyMedication dailyMedicationTest = DailyMedicationManager.getDailyMedication(actualIndex, Period.MORNING);
        assertTrue(dailyMedicationTest.isTaken());
    }

    @Test
    public void untakeDailyMedication_genericDailyMedication_dailyMedicationNotTaken() throws FileReadWriteException {
        DailyMedication dailyMedication = new DailyMedication("TestMedication");
        dailyMedication.take();
        assertTrue(dailyMedication.isTaken());
        DailyMedicationManager.addDailyMedication(dailyMedication, Period.MORNING);

        int actualIndex = 1; // 1-based indexing
        DailyMedicationManager.untakeDailyMedication(actualIndex);
        DailyMedication dailyMedicationTest = DailyMedicationManager.getDailyMedication(actualIndex, Period.MORNING);
        assertFalse(dailyMedicationTest.isTaken());
    }
}
