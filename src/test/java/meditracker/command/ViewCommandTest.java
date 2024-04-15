package meditracker.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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

/**
 * This test file is to perform tests on ViewCommands
 */
public class ViewCommandTest {

    @BeforeEach
    @AfterEach
    void resetManagers() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        DailyMedicationManagerTest.resetDailyMedicationManager();
        MedicationManagerTest.resetMedicationManager();
    }

    @Test
    void execute_viewCommandByIndex_expectMedicationShownByIndex()
            throws HelpInvokedException, MediTrackerException, ArgumentException {
        String medicationName = "Medication_I";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedExpiryDate = LocalDate.parse("2024-08-01", dateTimeFormatter);
        Medication medication = new Medication(
                        medicationName,
                1000.0,
                15.0,
                0.0,
                15.0,
                parsedExpiryDate,
                "drowsiness",
                1,
                90);
        MedicationManager.addMedication(medication);

        assert MedicationManager.getTotalMedications() > 0 : "Total medications in medication must be greater "
                + "than 0 after adding in" + medicationName;

        int medicationIndex = 1;
        String inputString = "view -l " + medicationIndex;

        ViewCommand command = new ViewCommand(inputString);
        command.execute();

        Medication expectedMedication = MedicationManager.getMedication(medicationIndex);
        assertEquals(expectedMedication.getName(), medicationName);
    }

    // Solution below adapted by https://stackoverflow.com/questions/32241057/
    @Test
    void execute_viewCommandByName_expectMedicationShownByName()
            throws HelpInvokedException, MediTrackerException, ArgumentException {
        String medicationName = "Medication_N";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedExpiryDate = LocalDate.parse("2024-08-01", dateTimeFormatter);
        Medication medication = new Medication(
                medicationName,
                1000.0,
                15.0,
                0.0,
                15.0,
                parsedExpiryDate,
                "drowsiness",
                1,
                91);
        MedicationManager.addMedication(medication);

        assert MedicationManager.getTotalMedications() > 0 : "Total medications in medication must be greater "
                + "than 0 after adding in" + medicationName;

        // Store current System.out
        PrintStream oldOut = System.out;

        // Create a ByteArrayOutputStream to get the output from the call to print
        ByteArrayOutputStream content = new ByteArrayOutputStream();

        // Change System.out to point out to our stream
        System.setOut(new PrintStream(content));

        // Execute the view by name command
        String inputString = "view -n " + medicationName;
        ViewCommand command = new ViewCommand(inputString);
        command.execute();

        // Reset back to System.out
        System.setOut(oldOut);

        // Output contains the content from the stream
        String output = content.toString();

        assertTrue(output.contains("Name: " + medicationName));
    }

    @Test
    void execute_viewCommandByQuantity_expectMedicationShownByQuantity()
            throws HelpInvokedException, MediTrackerException, ArgumentException {
        String medicationName = "Medication_Q";
        Double medicationQuantity = 1231.5;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedExpiryDate = LocalDate.parse("2027-12-01", dateTimeFormatter);
        Medication medication = new Medication(
                medicationName,
                medicationQuantity,
                10.0,
                10.0,
                10.0,
                parsedExpiryDate,
                "depression",
                1,
                92);
        MedicationManager.addMedication(medication);

        assert MedicationManager.getTotalMedications() > 0 : "Total medications in medication must be greater "
                + "than 0 after adding in" + medicationName;
        // Store current System.out
        PrintStream oldOut = System.out;

        // Create a ByteArrayOutputStream to get the output from the call to print
        ByteArrayOutputStream content = new ByteArrayOutputStream();

        // Change System.out to point out to our stream
        System.setOut(new PrintStream(content));

        // Execute the view by name command
        String inputString = "view -q " + medicationQuantity;
        ViewCommand command = new ViewCommand(inputString);
        command.execute();

        // Reset back to System.out
        System.setOut(oldOut);

        // Output contains the content from the stream
        String output = content.toString();

        assertTrue(output.contains("Name: " + medicationName));
        assertTrue(output.contains("Quantity: " + medicationQuantity));
    }

    @Test
    void execute_viewCommandByExpiry_expectMedicationShownByExpiry()
            throws HelpInvokedException, MediTrackerException, ArgumentException {
        String medicationName = "Medication_E";
        Double medicationQuantity = 30.0;
        String medicationExpiryYear = "2024";
        String medicationExpiry = medicationExpiryYear + "-11-20";

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedExpiryDate = LocalDate.parse(medicationExpiry, dateTimeFormatter);

        Medication medication = new Medication(
                medicationName,
                medicationQuantity,
                10.0,
                10.0,
                10.0,
                parsedExpiryDate,
                "depression",
                1,
                93);
        MedicationManager.addMedication(medication);

        assert MedicationManager.getTotalMedications() > 0 : "Total medications in medication must be greater "
                + "than 0 after adding in" + medicationName;
        // Store current System.out
        PrintStream oldOut = System.out;

        // Create a ByteArrayOutputStream to get the output from the call to print
        ByteArrayOutputStream content = new ByteArrayOutputStream();

        // Change System.out to point out to our stream
        System.setOut(new PrintStream(content));

        // Execute the view by name command
        String inputString = "view -e " + medicationExpiryYear;
        ViewCommand command = new ViewCommand(inputString);
        command.execute();

        // Reset back to System.out
        System.setOut(oldOut);

        // Output contains the content from the stream
        String output = content.toString();

        assertTrue(output.contains("Name: " + medicationName));
        assertTrue(output.contains("Quantity: " + medicationQuantity));
        assertTrue(output.contains("Expiry Date: " + medicationExpiry));
    }

    @Test
    void execute_viewCommandByRemarks_expectMedicationShownByRemarks()
            throws HelpInvokedException, MediTrackerException, ArgumentException {
        String medicationNameOne = "Dexamethasone";
        String medicationRemarksOne = "aggression, weight gain, nausea";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedExpiryDateOne = LocalDate.parse("2025-01-31", dateTimeFormatter);
        Medication medicationOne = new Medication(
                medicationNameOne,
                20.0,
                10.0,
                10.0,
                10.0,
                parsedExpiryDateOne,
                medicationRemarksOne,
                1,
                94);
        String medicationNameTwo = "Dextromethorphan";
        String medicationRemarksTwo = "restlessness, nausea";
        LocalDate parsedExpiryDateTwo = LocalDate.parse("2025-02-31", dateTimeFormatter);
        Medication medicationTwo = new Medication(
                medicationNameTwo,
                30.0,
                10.0,
                10.0,
                10.0,
                parsedExpiryDateTwo,
                medicationRemarksTwo,
                1,
                95);
        MedicationManager.addMedication(medicationOne);
        MedicationManager.addMedication(medicationTwo);

        assert MedicationManager.getTotalMedications() > 1 : "Total medications in medication must be greater "
                + "than 0 after adding in" + medicationNameOne + "and" + medicationNameTwo;
        // Store current System.out
        PrintStream oldOut = System.out;

        // Create a ByteArrayOutputStream to get the output from the call to print
        ByteArrayOutputStream content = new ByteArrayOutputStream();

        // Change System.out to point out to our stream
        System.setOut(new PrintStream(content));

        // Execute the view by name command
        String commonRemark = "nausea";
        String inputString = "view -r " + commonRemark;
        ViewCommand command = new ViewCommand(inputString);
        command.execute();

        // Reset back to System.out
        System.setOut(oldOut);

        // Output contains the content from the stream
        String output = content.toString();

        assertTrue(output.contains("Name: " + medicationNameOne));
        assertTrue(output.contains("Remarks: " + medicationRemarksOne));
        assertTrue(output.contains("Name: " + medicationNameTwo));
        assertTrue(output.contains("Remarks: " + medicationRemarksTwo));
    }

    @Test
    void execute_viewCommandWithMultipleFlags_expectErrorMessage()
            throws HelpInvokedException, MediTrackerException, ArgumentException {
        String medicationName = "Medication_F";
        Double medicationQuantity = 30.0;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedExpiryDate = LocalDate.parse("2024-08-01", dateTimeFormatter);
        Medication medication = new Medication(
                medicationName,
                medicationQuantity,
                25.0,
                25.0,
                0.0,
                parsedExpiryDate,
                "blurry_vision",
                1,
                96);
        MedicationManager.addMedication(medication);

        assert MedicationManager.getTotalMedications() > 0 : "Total medications in medication must be greater "
                + "than 0 after adding in" + medicationName;
        // Store current System.out
        PrintStream oldOut = System.out;

        // Create a ByteArrayOutputStream to get the output from the call to print
        ByteArrayOutputStream content = new ByteArrayOutputStream();

        // Change System.out to point out to our stream
        System.setOut(new PrintStream(content));

        // Execute the view by name command
        String inputString = "view -n " + medicationName + "-q " + medicationQuantity;
        ViewCommand command = new ViewCommand(inputString);
        command.execute();

        // Reset back to System.out
        System.setOut(oldOut);

        // Output contains the content from the stream
        String output = content.toString();

        assertTrue(output.contains("ERROR: Medicine can not be found."));
    }
}
