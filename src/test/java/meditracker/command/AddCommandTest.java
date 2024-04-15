package meditracker.command;

// @@author nickczh

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import meditracker.dailymedication.DailyMedicationManagerTest;
import meditracker.exception.ArgumentException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.MediTrackerException;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;
import meditracker.medication.MedicationManagerTest;
import meditracker.time.MediTrackerTime;

class AddCommandTest {

    @BeforeEach
    @AfterEach
    public void resetManagers() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        DailyMedicationManagerTest.resetDailyMedicationManager();
        MedicationManagerTest.resetMedicationManager();
    }

    // 3 part format
    // unitBeingTested_descriptionOfTestInputs_expectedOutcome
    @Test
    void execute_correctExampleInput_expectOneMedication()
            throws HelpInvokedException, ArgumentException {

        // setup lines
        String inputString = "add -n Medication A -q 5000 -e 2025-07-01 "
                + "-dM 500 -dA 250 -dE 100 -r cause_dizziness -rep 1";
        AddCommand command = new AddCommand(inputString);
        command.execute();

        // actual test
        assertEquals(1, MedicationManager.getTotalMedications());
    }

    @Test
    void execute_invalidCharactersInMedicationName_expectZeroMedication()
            throws HelpInvokedException, ArgumentException {
        // setup lines
        String inputString = "add -n Medication_A -q 60 -e 2025-07-01 -dM 500.0 -dA 250.0 -dE 100.0"
                + "-r cause_dizziness -rep 1";
        AddCommand command = new AddCommand(inputString);
        command.execute();

        // actual test
        assertEquals(0, MedicationManager.getTotalMedications());
    }

    @Test
    void execute_emptyMedicationName_expectZeroMedication()
            throws HelpInvokedException, ArgumentException {
        // setup lines
        String inputString = "add -n \" \" -q 60 -e 2025-07-01 -dM 500.0 -dA 250.0 -dE 100.0"
                + "-r cause_dizziness -rep 1";
        AddCommand command = new AddCommand(inputString);
        command.execute();

        // actual test
        assertEquals(0, MedicationManager.getTotalMedications());
    }

    @Test
    void createMedication_correctExampleInput_success()
            throws HelpInvokedException, MediTrackerException, ArgumentException {
        // setup lines
        String inputString = "add -n Medication A -q 5000 -e 2025-07-01 -dM 500 -dA 250 -dE 100 "
                + "-r cause_dizziness -rep 1";
        AddCommand command = new AddCommand(inputString);
        // Get the current date and the day of the year
        LocalDate currentDate = MediTrackerTime.getCurrentDate();
        int dayAdded = currentDate.getDayOfYear();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedExpiryDate = LocalDate.parse("2025-07-01", dateTimeFormatter);

        Medication medication = new Medication("Medication A",
                5000.0,
                500.0,
                250.0,
                100.0,
                parsedExpiryDate,
                "cause_dizziness",
                1, dayAdded);

        // actual test
        assertEquals(medication, command.createMedication());
    }

    @Test
    void createMedication_incorrectQuantityInput_exceptionThrown()
            throws HelpInvokedException, ArgumentException {
        // setup lines
        String inputString = "add -n Medication A -q %%% -e 2025-07-01 -dM 500 -dA 250 -dE 100 "
                + "-r cause_dizziness -rep 1";
        AddCommand command = new AddCommand(inputString);

        // actual test
        try {
            assertEquals(new Medication(), command.createMedication());
            fail();
        } catch (MediTrackerException e) {
            assertEquals("Unable to parse String '%%%' into double.", e.getMessage());
        }
    }
}
// @@author
