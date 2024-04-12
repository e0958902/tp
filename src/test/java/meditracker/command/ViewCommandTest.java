package meditracker.command;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ViewCommandTest {

    @BeforeEach
    @AfterEach
    void resetManagers() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        DailyMedicationManagerTest.resetDailyMedicationManager();
        MedicationManagerTest.resetMedicationManager();
    }

    @Test
    void execute_viewCommandByIndex_expectMedicationShownByIndex()
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException, MediTrackerException {
        String medicationName = "Medication_I";
        Medication medication = new Medication(
                        medicationName,
                1000.0,
                15.0,
                0.0,
                15.0,
                "2024-08-01",
                "drowsiness",
                1,
                90);
        MedicationManager.addMedication(medication);

        assert MedicationManager.getTotalMedications() > 0 : "Total medications in medication must be greater " +
                "than 0 after adding in" + medicationName;

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
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException, MediTrackerException {
        String medicationName = "Medication_N";
        Medication medication = new Medication(
                medicationName,
                1000.0,
                15.0,
                0.0,
                15.0,
                "2024-08-01",
                "drowsiness",
                1,
                91);
        MedicationManager.addMedication(medication);

        assert MedicationManager.getTotalMedications() > 0 : "Total medications in medication must be greater " +
                "than 0 after adding in" + medicationName;

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
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException, MediTrackerException {
        String medicationName = "Medication_Q";
        Double medicationQuantity = 1231.5;
        Medication medication = new Medication(
                medicationName,
                medicationQuantity,
                10.0,
                10.0,
                10.0,
                "2027-12-01",
                "depression",
                1,
                92);
        MedicationManager.addMedication(medication);

        assert MedicationManager.getTotalMedications() > 0 : "Total medications in medication must be greater " +
                "than 0 after adding in" + medicationName;
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
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException, MediTrackerException {
        String medicationName = "Medication_E";
        Double medicationQuantity = 30.0;
        String medicationExpiryYear = "2024";
        String medicationExpiry = medicationExpiryYear + "-11-20";
        Medication medication = new Medication(
                medicationName,
                medicationQuantity,
                10.0,
                10.0,
                10.0,
                medicationExpiry,
                "depression",
                1,
                93);
        MedicationManager.addMedication(medication);

        assert MedicationManager.getTotalMedications() > 0 : "Total medications in medication must be greater " +
                "than 0 after adding in" + medicationName;
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
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException, MediTrackerException {
        String medicationNameOne = "Dexamethasone";
        String medicationRemarksOne = "aggression, weight gain, nausea";
        Medication medicationOne = new Medication(
                medicationNameOne,
                20.0,
                10.0,
                10.0,
                10.0,
                "2025-01-31",
                medicationRemarksOne,
                1,
                94);
        String medicationNameTwo = "Dextromethorphan";
        String medicationRemarksTwo = "restlessness, nausea";
        Medication medicationTwo = new Medication(
                medicationNameTwo,
                30.0,
                10.0,
                10.0,
                10.0,
                "2025-02-31",
                medicationRemarksTwo,
                1,
                95);
        MedicationManager.addMedication(medicationOne);
        MedicationManager.addMedication(medicationTwo);

        assert MedicationManager.getTotalMedications() > 1 : "Total medications in medication must be greater " +
                "than 0 after adding in" + medicationNameOne + "and" + medicationNameTwo ;
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
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException, MediTrackerException {
        String medicationName = "Medication_F";
        Double medicationQuantity = 30.0;
        Medication medication = new Medication(
                medicationName,
                medicationQuantity,
                25.0,
                25.0,
                0.0,
                "2024-08-01",
                "blurry_vision",
                1,
                96);
        MedicationManager.addMedication(medication);

        assert MedicationManager.getTotalMedications() > 0 : "Total medications in medication must be greater " +
                "than 0 after adding in" + medicationName;
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
