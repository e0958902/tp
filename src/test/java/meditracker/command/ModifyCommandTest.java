package meditracker.command;

import meditracker.dailymedication.DailyMedicationManager;
import meditracker.dailymedication.DailyMedicationManagerTest;
import meditracker.exception.ArgumentException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.MediTrackerException;
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

public class ModifyCommandTest {

    void setupMedication() throws MediTrackerException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedExpiryDate = LocalDate.parse("2025-07-01", dateTimeFormatter);
        Medication medication = new Medication(
                "Medication A",
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
    }

    private static String getExpectedOutput(ByteArrayOutputStream output, String errorMessage) {
        Ui.showErrorMessage(errorMessage);
        Ui.showWarningMessage("Rolling back changes...");
        Ui.showInfoMessage("Changes have been rolled back. Medicine not modified.");
        String expectedOutput = output.toString();
        output.reset();
        return expectedOutput;
    }

    @BeforeEach
    @AfterEach
    void resetManagers() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        DailyMedicationManagerTest.resetDailyMedicationManager();
        MedicationManagerTest.resetMedicationManager();
    }

    @Test
    void execute_inOrderArgument_expectMedicationModified()
            throws HelpInvokedException, MediTrackerException, ArgumentException {
        setupMedication();

        String newName = "Medication B";
        String inputString = "-l 1 -n " + newName;
        ModifyCommand command = new ModifyCommand(inputString);
        command.execute();

        Medication updatedMedication = MedicationManager.getMedication(1);
        assertEquals(updatedMedication.getName(), newName);
    }

    @Test
    void execute_outOfOrderArgument_expectMedicationModified()
            throws HelpInvokedException, MediTrackerException, ArgumentException {
        setupMedication();

        String newName = "Medication B";
        String inputString = String.format("-n %s -l 1", newName);
        ModifyCommand command = new ModifyCommand(inputString);
        command.execute();

        Medication updatedMedication = MedicationManager.getMedication(1);
        assertEquals(updatedMedication.getName(), newName);
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

        new ModifyCommand("-l '").execute();
        String actualOutput = output.toString();
        output.reset();
        assertEquals(expectedOutput, actualOutput);

        new ModifyCommand("-l string").execute();
        actualOutput = output.toString();
        output.reset();
        assertEquals(expectedOutput, actualOutput);

        new ModifyCommand("-l 4 [-h]").execute();
        actualOutput = output.toString();
        output.reset();
        assertEquals(expectedOutput, actualOutput);

        System.setOut(originalOut); // restore stream
    }

    @Test
    void execute_onlyListIndex_noChangesSpecified()
            throws HelpInvokedException, ArgumentException, MediTrackerException {
        //Solution below adapted by https://stackoverflow.com/questions/58665761
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output)); // set up capture stream

        Ui.showSuccessMessage("No changes specified. Medicine not modified.");
        String expectedOutput = output.toString();
        output.reset();
        setupMedication();

        new ModifyCommand("-l 1").execute();
        String actualOutput = output.toString();
        output.reset();
        assertEquals(expectedOutput, actualOutput);

        System.setOut(originalOut); // restore stream
    }

    @Test
    void execute_invalidName_rollbackChanges()
            throws HelpInvokedException, ArgumentException, MediTrackerException {
        //Solution below adapted by https://stackoverflow.com/questions/58665761
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output)); // set up capture stream

        setupMedication();

        new ModifyCommand("-l 1 -q 70 -n Medication_B").execute();
        String actualOutput = output.toString();
        output.reset();
        String expectedOutput = getExpectedOutput(output, "Please enter a proper medication name.");
        assertEquals(expectedOutput, actualOutput);

        System.setOut(originalOut); // restore stream
    }

    @Test
    void execute_invalidQuantity_rollbackChanges()
            throws HelpInvokedException, ArgumentException, MediTrackerException {
        //Solution below adapted by https://stackoverflow.com/questions/58665761
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output)); // set up capture stream

        setupMedication();

        new ModifyCommand("-l 1 -q seventy -n Medication B").execute();
        String actualOutput = output.toString();
        output.reset();
        String expectedOutput = getExpectedOutput(output, "Unable to parse String 'seventy' into double.");
        assertEquals(expectedOutput, actualOutput);

        System.setOut(originalOut); // restore stream
    }

    @Test
    void execute_invalidDosage_rollbackChanges()
            throws HelpInvokedException, ArgumentException, MediTrackerException {
        //Solution below adapted by https://stackoverflow.com/questions/58665761
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output)); // set up capture stream

        setupMedication();

        new ModifyCommand("-l 1 -q 70 -dM ten").execute();
        String actualOutput = output.toString();
        output.reset();
        String expectedOutput = getExpectedOutput(output, "Unable to parse String 'ten' into double.");
        assertEquals(expectedOutput, actualOutput);

        new ModifyCommand("-l 1 -q 70 -dM ten").execute();
        actualOutput = output.toString();
        output.reset();
        expectedOutput = getExpectedOutput(output, "Unable to parse String 'ten' into double.");
        assertEquals(expectedOutput, actualOutput);

        new ModifyCommand("-l 1 -q 70 -dA ten").execute();
        actualOutput = output.toString();
        output.reset();
        expectedOutput = getExpectedOutput(output, "Unable to parse String 'ten' into double.");
        assertEquals(expectedOutput, actualOutput);

        new ModifyCommand("-l 1 -q 70 -dE ten").execute();
        actualOutput = output.toString();
        output.reset();
        expectedOutput = getExpectedOutput(output, "Unable to parse String 'ten' into double.");
        assertEquals(expectedOutput, actualOutput);

        new ModifyCommand("-l 1 -q 70 -dM 0 -dA 0 -dE 0").execute();
        actualOutput = output.toString();
        output.reset();
        expectedOutput = getExpectedOutput(
                output,
                "Medication has no dosages. " +
                        "Please ensure at least 1 period of day has dosage (-dM, -dA and/or -dE).");
        assertEquals(expectedOutput, actualOutput);

        System.setOut(originalOut); // restore stream
    }

    @Test
    void execute_invalidExpirationDate_rollbackChanges()
            throws HelpInvokedException, ArgumentException, MediTrackerException {
        //Solution below adapted by https://stackoverflow.com/questions/58665761
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output)); // set up capture stream

        setupMedication();

        new ModifyCommand("-l 1 -e 3000-13-32").execute();
        String actualOutput = output.toString();
        output.reset();
        String expectedOutput = getExpectedOutput(output, "Please enter a valid expiry date in yyyy-MM-dd!");
        assertEquals(expectedOutput, actualOutput);

        new ModifyCommand("-l 1 -e 1900-12-31").execute();
        actualOutput = output.toString();
        output.reset();
        expectedOutput = getExpectedOutput(output, "You are not allowed to enter expired medications!");
        assertEquals(expectedOutput, actualOutput);

        System.setOut(originalOut); // restore stream
    }

    @Test
    void execute_invalidRepeat_rollbackChanges()
            throws HelpInvokedException, ArgumentException, MediTrackerException {
        //Solution below adapted by https://stackoverflow.com/questions/58665761
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output)); // set up capture stream

        setupMedication();

        new ModifyCommand("-l 1 -rep 1.0E1000").execute();
        String actualOutput = output.toString();
        output.reset();
        String expectedOutput = getExpectedOutput(output, "Unable to parse String '1.0E1000' into integer.");
        assertEquals(expectedOutput, actualOutput);

        new ModifyCommand("-l 1 -rep NaN").execute();
        actualOutput = output.toString();
        output.reset();
        expectedOutput = getExpectedOutput(output, "Unable to parse String 'NaN' into integer.");
        assertEquals(expectedOutput, actualOutput);

        new ModifyCommand("-l 1 -rep 1.0E-1000").execute();
        actualOutput = output.toString();
        output.reset();
        expectedOutput = getExpectedOutput(output, "Unable to parse String '1.0E-1000' into integer.");
        assertEquals(expectedOutput, actualOutput);

        new ModifyCommand("-l 1 -rep 0").execute();
        actualOutput = output.toString();
        output.reset();
        expectedOutput = getExpectedOutput(output, "Provide a \"-rep\" number from 1 to 7");
        assertEquals(expectedOutput, actualOutput);

        new ModifyCommand("-l 1 -rep 8").execute();
        actualOutput = output.toString();
        output.reset();
        expectedOutput = getExpectedOutput(output, "Provide a \"-rep\" number from 1 to 7");
        assertEquals(expectedOutput, actualOutput);

        System.setOut(originalOut); // restore stream
    }
}
