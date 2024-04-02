package meditracker.dailymedication;

import meditracker.command.AddCommand;
import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.InsufficientQuantityException;
import meditracker.exception.MedicationNotFoundException;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;
import meditracker.medication.MedicationManagerTest;
import meditracker.time.Period;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DailyMedicationManagerTest {

    // @@author T0nyLin
    public static void resetDailyMedicationManager() throws InvocationTargetException,
            IllegalAccessException, NoSuchMethodException {
        Method resetDailyMedicationManagerMethod
                = DailyMedicationManager.class.getDeclaredMethod("clearDailyMedication");
        resetDailyMedicationManagerMethod.setAccessible(true);
        resetDailyMedicationManagerMethod.invoke(DailyMedicationManager.class);
    }
    // @@author

    @BeforeEach
    @AfterEach
    public void resetManagers() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        resetDailyMedicationManager();
        MedicationManagerTest.resetMedicationManager();
    }

    @Test
    public void addDailyMedication_genericDailyMedication_dailyMedicationAdded()
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException {
        String inputString = "add -n Medication_A -q 60.0 -e 01/07/25 -dM 500.0 -dA 250.0 "
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
    public void takeDailyMedication_genericDailyMedication_dailyMedicationTaken()
            throws InsufficientQuantityException, MedicationNotFoundException {
        String medicationName = "TestMedication";
        double oldQuantity = 60;
        double dosage = 10;
        Medication medication = new Medication(
                medicationName,
                oldQuantity,
                dosage,
                null,
                null,
                "01/07/25",
                "cause_dizziness",
                1,
                87);
        MedicationManager.addMedication(medication);

        DailyMedication dailyMedication = new DailyMedication(medicationName);
        assertFalse(dailyMedication.isTaken());
        DailyMedicationManager.addDailyMedication(dailyMedication, Period.MORNING);

        int actualIndex = 1; // 1-based indexing
        DailyMedicationManager.takeDailyMedication(actualIndex, Period.MORNING);
        DailyMedication dailyMedicationTest = DailyMedicationManager.getDailyMedication(actualIndex, Period.MORNING);
        assertTrue(dailyMedicationTest.isTaken());
        double expectedQuantity = oldQuantity - dosage;
        assertEquals(medication.getQuantity(), expectedQuantity);
    }

    @Test
    public void takeDailyMedication_lowQuantityMedication_insufficientQuantity() {
        String medicationName = "TestMedication";
        double oldQuantity = 5;
        double dosage = 10;
        Medication medication = new Medication(
                medicationName,
                oldQuantity,
                dosage,
                null,
                null,
                "01/07/25",
                "cause_dizziness",
                1,
                87);
        MedicationManager.addMedication(medication);

        DailyMedication dailyMedication = new DailyMedication(medicationName);
        assertFalse(dailyMedication.isTaken());
        DailyMedicationManager.addDailyMedication(dailyMedication, Period.MORNING);

        int actualIndex = 1; // 1-based indexing
        assertThrows(
                InsufficientQuantityException.class,
                () -> DailyMedicationManager.takeDailyMedication(actualIndex, Period.MORNING));
    }

    @Test
    public void untakeDailyMedication_genericDailyMedication_dailyMedicationNotTaken()
            throws MedicationNotFoundException {
        String medicationName = "TestMedication";
        double oldQuantity = 60;
        double dosage = 10;
        Medication medication = new Medication(
                medicationName,
                oldQuantity,
                dosage,
                null,
                null,
                "01/07/25",
                "cause_dizziness",
                1,
                87);
        MedicationManager.addMedication(medication);

        DailyMedication dailyMedication = new DailyMedication(medicationName);
        dailyMedication.take();
        assertTrue(dailyMedication.isTaken());
        DailyMedicationManager.addDailyMedication(dailyMedication, Period.MORNING);

        int actualIndex = 1; // 1-based indexing
        DailyMedicationManager.untakeDailyMedication(actualIndex, Period.MORNING);
        DailyMedication dailyMedicationTest = DailyMedicationManager.getDailyMedication(actualIndex, Period.MORNING);
        assertFalse(dailyMedicationTest.isTaken());
        double expectedQuantity = oldQuantity + dosage;
        assertEquals(medication.getQuantity(), expectedQuantity);
    }
}
