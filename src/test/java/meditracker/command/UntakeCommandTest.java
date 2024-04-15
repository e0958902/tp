package meditracker.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import meditracker.dailymedication.DailyMedication;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.dailymedication.DailyMedicationManagerTest;
import meditracker.exception.ArgumentException;
import meditracker.exception.HelpInvokedException;
import meditracker.medication.MedicationManagerTest;
import meditracker.time.Period;
import meditracker.ui.Ui;

public class UntakeCommandTest {

    @BeforeEach
    @AfterEach
    void resetManagers() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        DailyMedicationManagerTest.resetDailyMedicationManager();
        MedicationManagerTest.resetMedicationManager();
    }

    @Test
    void execute_inOrderArgument_expectDailyMedicationUntaken() throws HelpInvokedException, ArgumentException {
        DailyMedication dailyMedication = new DailyMedication("Medication_A", 10, Period.MORNING);
        DailyMedicationManager.addDailyMedication(dailyMedication); // only doing for MORNING sub list

        String inputString = "untake -l 1 -m";
        UntakeCommand command = new UntakeCommand(inputString);
        command.execute();

        assertFalse(dailyMedication.isTaken());
    }

    @Test
    void execute_erroneousListIndex_errorMessagePrinted() throws HelpInvokedException, ArgumentException {
        //Solution below adapted by https://stackoverflow.com/questions/58665761
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output)); // set up capture stream

        Ui.showErrorMessage("Invalid index specified");
        String expectedOutput = output.toString();
        output.reset();

        new UntakeCommand("-l '").execute();
        String actualOutput = output.toString();
        output.reset();
        assertEquals(expectedOutput, actualOutput);

        new UntakeCommand("-l string").execute();
        actualOutput = output.toString();
        output.reset();
        assertEquals(expectedOutput, actualOutput);

        new UntakeCommand("-l 4 [-h]").execute();
        actualOutput = output.toString();
        output.reset();
        assertEquals(expectedOutput, actualOutput);

        System.setOut(originalOut); // restore stream
    }
}
