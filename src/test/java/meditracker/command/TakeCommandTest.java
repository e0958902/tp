package meditracker.command;

import meditracker.dailymedication.DailyMedication;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.dailymedication.DailyMedicationManagerTest;
import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;
import meditracker.medication.MedicationManagerTest;
import meditracker.time.Period;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TakeCommandTest {

    @BeforeEach
    @AfterEach
    public void resetManagers() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        DailyMedicationManagerTest.resetDailyMedicationManager();
        MedicationManagerTest.resetMedicationManager();
    }

    @Test
    void execute_inOrderArgument_expectDailyMedicationTaken()
            throws ArgumentNotFoundException, ArgumentNoValueException, DuplicateArgumentFoundException,
            HelpInvokedException {
        Medication medication = new Medication(
                "Medication_A",
                60.0,
                10.0,
                0.0,
                0.0,
                "01/07/25",
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
}
