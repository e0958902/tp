package meditracker.command;

import meditracker.argument.ArgumentList;
import meditracker.argument.DosageMorningArgument;
import meditracker.argument.NameArgument;
import meditracker.argument.QuantityArgument;
import meditracker.argument.RemarksArgument;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListCommandTest {
    ArgumentList testArgumentList = new ArgumentList(
            new NameArgument(false),
            new QuantityArgument(false),
            new DosageMorningArgument(false),
            new RemarksArgument(false)
    );

    @BeforeEach
    @AfterEach
    void resetManagers() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        DailyMedicationManagerTest.resetDailyMedicationManager();
        MedicationManagerTest.resetMedicationManager();
    }

    @Test
    void execute_listCommand_expect() throws MediTrackerException {
        String medicationName = "Medication_B";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedExpiryDate = LocalDate.parse("2025-08-01", dateTimeFormatter);
        Medication medication = new Medication(
                medicationName,
                30.0,
                10.0,
                0.0,
                0.0,
                parsedExpiryDate,
                "for_flu_or_allergy",
                1,
                87
        );

        MedicationManager.addMedication(medication);

        assert MedicationManager.getTotalMedications() > 0 : "Total medications in medication must be greater " +
                "than 0 after adding in" + medicationName;

        Medication addedMedication = MedicationManager.getMedication(1);
        assertEquals(addedMedication.getName(), medicationName);
    }

    // Solution below adapted by https://stackoverflow.com/questions/32241057/
    @Test
    void execute_listAllMedications_expectToShowAllMedicationsInMedicationList()
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException, UnknownArgumentFoundException, MediTrackerException {
        String medicationNameOne = "Dexamethasone";
        Double medicationQuantityOne = 20.0;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String expiryDateOne = "2025-01-31";
        LocalDate parsedExpiryDateOne = LocalDate.parse(expiryDateOne, dateTimeFormatter);
        String medicationRemarksOne = "aggression, weight gain, nausea";
        Medication medicationOne = new Medication(
                medicationNameOne,
                medicationQuantityOne,
                10.0,
                10.0,
                10.0,
                parsedExpiryDateOne,
                medicationRemarksOne,
                1,
                94);
        String medicationNameTwo = "Dextromethorphan";
        Double medicationQuantityTwo = 30.0;
        String expiryDateTwo = "2026-02-28";
        LocalDate parsedExpiryDateTwo = LocalDate.parse(expiryDateTwo, dateTimeFormatter);
        String medicationRemarksTwo = "restlessness, nausea";
        Medication medicationTwo = new Medication(
                medicationNameTwo,
                medicationQuantityTwo,
                10.0,
                10.0,
                10.0,
                parsedExpiryDateTwo,
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

        String inputString = "list -t all";
        ListCommand command = new ListCommand(inputString);
        command.execute();

        // Reset back to System.out
        System.setOut(oldOut);

        // Output contains the content from the stream
        String output = content.toString();

        String title = "You have " + MedicationManager.getTotalMedications() + " medications listed below."
                + System.lineSeparator();

        String headerFormat = "   %-30s %-10s %-12s %-30s%n";
        String bodyFormat = "%-30.30s %-10.1f %-12s %-30s %n";

        String name = "Name";
        String quantity = "Quantity";
        String expiryDate = "Expiry";
        String remarks = "Remarks";
        String successMessage = "Your list of medications has been successfully shown!";

        String body = String.format(title +
                        headerFormat +
                        "1. " + bodyFormat +
                        "2. " + bodyFormat +
                        successMessage +
                        System.lineSeparator(),
                name, quantity, expiryDate, remarks,
                medicationNameOne, medicationQuantityOne, expiryDateOne, medicationRemarksOne,
                medicationNameTwo, medicationQuantityTwo, expiryDateTwo, medicationRemarksTwo);

        assertEquals(output, body);
    }

    @Test
    void listAllMedication_extraFlagAfterCommand_showErrorMessage() throws DuplicateArgumentFoundException,
            HelpInvokedException, ArgumentNoValueException, ArgumentNotFoundException, UnknownArgumentFoundException {
        PrintStream oldOut = System.out;

        // Create a ByteArrayOutputStream to get the output from the call to print
        ByteArrayOutputStream content = new ByteArrayOutputStream();

        // Change System.out to point out to our stream
        System.setOut(new PrintStream(content));

        // Execute the view by name command
        String inputString = "list -t all -a";
        ListCommand command = new ListCommand(inputString);
        command.execute();

        // Reset back to System.out
        System.setOut(oldOut);

        // Output contains the content from the stream
        String output = content.toString();

        assertTrue(output.contains("ERROR: List type -> \"AFTERNOON\" not compatible with \"list -t all\" command."));
    }

    @Test
    void listAllMedication_extraWordsAfterCommand_showErrorMessage() throws DuplicateArgumentFoundException,
            HelpInvokedException, ArgumentNoValueException, ArgumentNotFoundException, UnknownArgumentFoundException {
        PrintStream oldOut = System.out;

        // Create a ByteArrayOutputStream to get the output from the call to print
        ByteArrayOutputStream content = new ByteArrayOutputStream();

        // Change System.out to point out to our stream
        System.setOut(new PrintStream(content));

        // Execute the view by name command
        String inputString = "list -t all asdf";
        ListCommand command = new ListCommand(inputString);
        command.execute();

        // Reset back to System.out
        System.setOut(oldOut);

        // Output contains the content from the stream
        String output = content.toString();

        assertTrue(output.contains("ERROR: Unknown list type -> \"all asdf\""));
    }

    @Test
    void listAllMedication_unknownFlagAfterCommand_showErrorMessage() {
        String testArgumentString = "list -t all -asdf";

        assertThrows(
                UnknownArgumentFoundException.class,
                () -> testArgumentList.parse(testArgumentString)
        );
    }

    @Test
    void listDailyMedication_extraWordsAfterCommand_showErrorMessage() throws DuplicateArgumentFoundException,
            HelpInvokedException, ArgumentNoValueException, ArgumentNotFoundException, UnknownArgumentFoundException {
        PrintStream oldOut = System.out;

        // Create a ByteArrayOutputStream to get the output from the call to print
        ByteArrayOutputStream content = new ByteArrayOutputStream();

        // Change System.out to point out to our stream
        System.setOut(new PrintStream(content));

        // Execute the view by name command
        String inputString = "list -t today asdf";
        ListCommand command = new ListCommand(inputString);
        command.execute();

        // Reset back to System.out
        System.setOut(oldOut);

        // Output contains the content from the stream
        String output = content.toString();

        assertTrue(output.contains("ERROR: Unknown list type -> \"today asdf\""));
    }

    @Test
    void listDailyMedication_extraFlagsAfterCommand_showErrorMessage() throws DuplicateArgumentFoundException,
            HelpInvokedException, ArgumentNoValueException, ArgumentNotFoundException, UnknownArgumentFoundException {
        PrintStream oldOut = System.out;

        // Create a ByteArrayOutputStream to get the output from the call to print
        ByteArrayOutputStream content = new ByteArrayOutputStream();

        // Change System.out to point out to our stream
        System.setOut(new PrintStream(content));

        // Execute the view by name command
        String inputString = "list -t today -a -m";
        ListCommand command = new ListCommand(inputString);
        command.execute();

        // Reset back to System.out
        System.setOut(oldOut);

        // Output contains the content from the stream
        String output = content.toString();

        assertTrue(output.contains("ERROR: Unknown list type -> \"UNKNOWN\""));
    }

    @Test
    void listDailyMedication_unknownFlagAfterCommand_showErrorMessage() {
        String testArgumentString = "list -t today -a -asd";

        assertThrows(
                UnknownArgumentFoundException.class,
                () -> testArgumentList.parse(testArgumentString)
        );
    }

    @Test
    void listDailyMedication_extraWordsAfterFlag_showErrorMessage() {
        String testArgumentString = "list -t today -a asdf";

        assertThrows(
                UnknownArgumentFoundException.class,
                () -> testArgumentList.parse(testArgumentString)
        );
    }
}
