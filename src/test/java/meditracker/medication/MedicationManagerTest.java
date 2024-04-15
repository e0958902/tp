package meditracker.medication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This test file is to perform tests on the MedicationManager
 */

public class MedicationManagerTest {

    /**
     * Resets the MedicationManager
     *
     * @throws InvocationTargetException when it fails to call the reset method
     * @throws IllegalAccessException when the method can not access the specified class
     * @throws NoSuchMethodException
     */

    // @@author e0958902
    public static void resetMedicationManager() throws InvocationTargetException,
            IllegalAccessException, NoSuchMethodException {
        Method resetMedicationManagerMethod = MedicationManager.class.getDeclaredMethod("clearMedication");
        resetMedicationManagerMethod.setAccessible(true);
        resetMedicationManagerMethod.invoke(MedicationManager.class);
    }
    // @@author
}
