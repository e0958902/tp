package meditracker.command;

// @@author nickczh

import meditracker.dailymedication.DailyMedicationManagerTest;
import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.MediTrackerException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.UnknownArgumentFoundException;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;
import meditracker.medication.MedicationManagerTest;
import meditracker.time.MediTrackerTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException {

        // setup lines
        String inputString = "add -n Medication A -q 5000 -e 2025-07-01 " +
                "-dM 500 -dA 250 -dE 100 -r cause_dizziness -rep 1";
        AddCommand command = new AddCommand(inputString);
        command.execute();

        // actual test
        assertEquals(1, MedicationManager.getTotalMedications());
    }

    @Test
    void execute_invalidCharactersInMedicationName_expectZeroMedication()
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException {
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
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException {
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
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException, MediTrackerException {
        // setup lines
        String inputString = "add -n Medication A -q 5000 -e 2025-07-01 -dM 500 -dA 250 -dE 100 " +
                "-r cause_dizziness -rep 1";
        AddCommand command = new AddCommand(inputString);
        // Get the current date and the day of the year
        LocalDate currentDate = MediTrackerTime.getCurrentDate();
        int dayAdded = currentDate.getDayOfYear();

        Medication medication = new Medication("Medication A",
                5000.0,
                500.0,
                250.0,
                100.0,
                "2025-07-01",
                "cause_dizziness",
                1, dayAdded);

        // actual test
        assertEquals(medication, command.createMedication());
    }

    @Test
    void createMedication_incorrectQuantityInput_exceptionThrown()
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException {
        // setup lines
        String inputString = "add -n Medication A -q %%% -e 2025-07-01 -dM 500 -dA 250 -dE 100 " +
                "-r cause_dizziness -rep 1";
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
