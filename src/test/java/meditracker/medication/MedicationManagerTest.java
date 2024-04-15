package meditracker.medication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This test file is to perform tests on the MedicationManager
 */
public class MedicationManagerTest {

    // @@author e0958902
    /**
     * Resets the MedicationManager
     *
     * @throws InvocationTargetException when it fails to call the reset method
     * @throws IllegalAccessException when the method can not access the specified class
     * @throws NoSuchMethodException when getDeclareMethod does not accept the specified parameter
     */
    public static void resetMedicationManager() throws InvocationTargetException,
            IllegalAccessException, NoSuchMethodException {
        Method resetMedicationManagerMethod = MedicationManager.class.getDeclaredMethod("clearMedication");
        resetMedicationManagerMethod.setAccessible(true);
        resetMedicationManagerMethod.invoke(MedicationManager.class);
    }
    // @@author
}
