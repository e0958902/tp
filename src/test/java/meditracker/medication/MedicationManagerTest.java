package meditracker.medication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MedicationManagerTest {

    // @@author e0958902
    public static void resetMedicationManager() throws InvocationTargetException,
            IllegalAccessException, NoSuchMethodException {
        Method resetMedicationManagerMethod
                = MedicationManager.class.getDeclaredMethod("clearMedication");
        resetMedicationManagerMethod.setAccessible(true);
        resetMedicationManagerMethod.invoke(MedicationManager.class);
    }
    // @@author
}
