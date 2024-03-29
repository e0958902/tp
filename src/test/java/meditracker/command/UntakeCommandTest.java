package meditracker.command;

import meditracker.dailymedication.DailyMedication;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.dailymedication.Period;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.FileReadWriteException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.medication.MedicationManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class UntakeCommandTest {

    @BeforeEach
    public void resetDailyMedicationManager() throws InvocationTargetException,
            IllegalAccessException, NoSuchMethodException {
        Method resetDailyMedicationManagerMethod
                = DailyMedicationManager.class.getDeclaredMethod("clearDailyMedication");
        resetDailyMedicationManagerMethod.setAccessible(true);
        resetDailyMedicationManagerMethod.invoke(DailyMedicationManager.class);
    }

    @Test
    void execute_inOrderArgument_expectDailyMedicationUntaken()
            throws ArgumentNotFoundException, DuplicateArgumentFoundException, HelpInvokedException,
            FileReadWriteException {
        MedicationManager medicationManager = new MedicationManager();
        DailyMedication dailyMedication = new DailyMedication("Medication_A");
        DailyMedicationManager.addDailyMedication(dailyMedication, Period.MORNING);    //only doing for MORNING sub list

        String inputString = "untake -l 1";
        UntakeCommand command = new UntakeCommand(inputString);
        command.execute(null);

        assertFalse(dailyMedication.isTaken());
    }
}
