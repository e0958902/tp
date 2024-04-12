package meditracker.command;

import meditracker.dailymedication.DailyMedication;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.dailymedication.DailyMedicationManagerTest;
import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.MediTrackerException;
import meditracker.exception.UnknownArgumentFoundException;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;
import meditracker.medication.MedicationManagerTest;
import meditracker.time.Period;
import meditracker.ui.Ui;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TakeCommandTest {

    @BeforeEach
    @AfterEach
    void resetManagers() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        DailyMedicationManagerTest.resetDailyMedicationManager();
        MedicationManagerTest.resetMedicationManager();
    }

    @Test
    void execute_inOrderArgument_expectDailyMedicationTaken()
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException, MediTrackerException {
        Medication medication = new Medication(
                "Medication_A",
                60.0,
                10.0,
                0.0,
                0.0,
                "2025-07-01",
                "cause_dizziness",
                1,
                87);
        MedicationManager.addMedication(medication);
        DailyMedicationManager.checkForDaily(medication);

        int listIndex = 1;
        DailyMedication dailyMedication = DailyMedicationManager.getDailyMedication(listIndex, Period.MORNING);

        String inputString = String.format("take -l %d -m", listIndex);
        TakeCommand command = new TakeCommand(inputString);
        command.execute();

        assertTrue(dailyMedication.isTaken());
    }

    @Test
    void execute_erroneousListIndex_errorMessagePrinted()
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException {
        //Solution below adapted by https://stackoverflow.com/questions/58665761
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output)); // set up capture stream

        Ui.showErrorMessage("Invalid index specified");
        String expectedOutput = output.toString();
        output.reset();

        new TakeCommand("-l '").execute();
        String actualOutput = output.toString();
        output.reset();
        assertEquals(expectedOutput, actualOutput);

        new TakeCommand("-l string").execute();
        actualOutput = output.toString();
        output.reset();
        assertEquals(expectedOutput, actualOutput);

        new TakeCommand("-l 4 [-h]").execute();
        actualOutput = output.toString();
        output.reset();
        assertEquals(expectedOutput, actualOutput);

        System.setOut(originalOut); // restore stream
    }
}
