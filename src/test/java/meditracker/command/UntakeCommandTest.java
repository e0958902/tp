package meditracker.command;

import meditracker.dailyMeds.DailyMedication;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.FileReadWriteException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.medication.MedicationManager;
import meditracker.dailyMeds.SubDailyManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class UntakeCommandTest {
    @Test
    void execute_inOrderArgument_expectDailyMedicationUntaken()
            throws ArgumentNotFoundException, DuplicateArgumentFoundException, FileReadWriteException {
        SubDailyManager.clearAllSubLists();
        MedicationManager medicationManager = new MedicationManager();
        DailyMedication dailyMedication = new DailyMedication("Medication_A");
        SubDailyManager.addToMorningList(dailyMedication);

        String inputString = "untake -l 1";
        UntakeCommand command = new UntakeCommand(inputString);
        command.execute(null);

        assertFalse(dailyMedication.isTaken());
    }
}
