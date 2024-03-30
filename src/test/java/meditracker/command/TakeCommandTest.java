package meditracker.command;

import meditracker.dailymedication.DailyMedication;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.dailymedication.Period;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.FileReadWriteException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TakeCommandTest {

    @BeforeEach
    public void resetDailyMedicationManager() throws InvocationTargetException,
            IllegalAccessException, NoSuchMethodException {
        Method resetDailyMedicationManagerMethod
                = DailyMedicationManager.class.getDeclaredMethod("clearDailyMedication");
        resetDailyMedicationManagerMethod.setAccessible(true);
        resetDailyMedicationManagerMethod.invoke(DailyMedicationManager.class);
    }

    @Test
    void execute_inOrderArgument_expectDailyMedicationTaken()
            throws ArgumentNotFoundException, DuplicateArgumentFoundException, HelpInvokedException,
            FileReadWriteException {
        DailyMedication dailyMedication = new DailyMedication("Medication_A");
        DailyMedicationManager.addDailyMedication(dailyMedication, Period.MORNING);  //only test Morning for now

        String inputString = "take -l 1";
        TakeCommand command = new TakeCommand(inputString);
        command.execute();

        assertTrue(dailyMedication.isTaken());
    }
}
