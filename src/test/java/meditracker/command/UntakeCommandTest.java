package meditracker.command;

import meditracker.dailymedication.DailyMedication;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.dailymedication.DailyMedicationManagerTest;
import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.UnknownArgumentFoundException;
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
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UntakeCommandTest {

    @BeforeEach
    @AfterEach
    void resetManagers() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        DailyMedicationManagerTest.resetDailyMedicationManager();
        MedicationManagerTest.resetMedicationManager();
    }

    @Test
    void execute_inOrderArgument_expectDailyMedicationUntaken()
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException {
        DailyMedication dailyMedication = new DailyMedication("Medication_A");
        DailyMedicationManager.addDailyMedication(dailyMedication, Period.MORNING);    //only doing for MORNING sub list

        String inputString = "untake -l 1 -m";
        UntakeCommand command = new UntakeCommand(inputString);
        command.execute();

        assertFalse(dailyMedication.isTaken());
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
