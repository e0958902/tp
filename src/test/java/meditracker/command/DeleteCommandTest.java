package meditracker.command;

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
import meditracker.ui.Ui;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteCommandTest {

    @BeforeEach
    @AfterEach
    void resetManagers() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        DailyMedicationManagerTest.resetDailyMedicationManager();
        MedicationManagerTest.resetMedicationManager();
    }

    @Test
    void execute_inOrderArgument_expectMedicationDeleted()
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException, MediTrackerException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedExpiryDate = LocalDate.parse("2025-07-01", dateTimeFormatter);
        Medication medication = new Medication(
                "Medication_A",
                60.0,
                10.0,
                10.0,
                10.0,
                parsedExpiryDate,
                "cause_dizziness",
                1,
                87);
        MedicationManager.addMedication(medication);
        DailyMedicationManager.checkForDaily(medication);

        String inputString = "-l 1";
        DeleteCommand command = new DeleteCommand(inputString);
        command.execute();

        assertThrows(IndexOutOfBoundsException.class, () -> MedicationManager.getMedication(1));
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

        new DeleteCommand("-l '").execute();
        String actualOutput = output.toString();
        output.reset();
        assertEquals(expectedOutput, actualOutput);

        new DeleteCommand("-l string").execute();
        actualOutput = output.toString();
        output.reset();
        assertEquals(expectedOutput, actualOutput);

        new DeleteCommand("-l 4 [-h]").execute();
        actualOutput = output.toString();
        output.reset();
        assertEquals(expectedOutput, actualOutput);

        System.setOut(originalOut); // restore stream
    }
}
