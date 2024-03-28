package meditracker;

import meditracker.exception.FileReadWriteException;
import meditracker.medication.SubDailyManager;
import meditracker.storage.FileReaderWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a list of DailyMedication and CRUD-operations (Create, Read, Update, Delete)
 *
 * @see DailyMedication
 */
public class DailyMedicationManager {
    private static final List<DailyMedication> dailyMedications = new ArrayList<>();

    /**
     * Clears and resets DailyMedicationManager for testing purpose
     */
    protected static void clearDailyMedication() {
        dailyMedications.clear();
    }

    /**
     * Adds a DailyMedication to the list of DailyMedication
     *
     * @param dailyMedication DailyMedication to be added to the list
     */
    public static void addDailyMedication(DailyMedication dailyMedication) {
        dailyMedications.add(dailyMedication);
    }

    /**
     * Gets the DailyMedication object from the dailyMedications list.
     * Also converts the index to 0-based indexing before being used.
     *
     * @param listIndex Index of the dailyMedications list to update (1-based indexing)
     * @return DailyMedication object at the corresponding index (0-based indexing)
     * @throws IndexOutOfBoundsException Out of range index specified
     */
    public static DailyMedication getDailyMedication(int listIndex) throws IndexOutOfBoundsException {
        listIndex--; // Decremented to 0-base indexing
        return dailyMedications.get(listIndex);
    }

    /**
     * Fetches the corresponding DailyMedication and set the medication to taken
     *
     * @param listIndex Index of the dailyMedications list to update (1-based indexing)
     * @throws FileReadWriteException when unable to write to textfile
     * @see DailyMedication#take()
     */
    public static void takeDailyMedication(int listIndex) throws FileReadWriteException {
        DailyMedication dailyMedication = SubDailyManager.getMorningMedication(listIndex);
        dailyMedication.take();
        try {
            FileReaderWriter.saveDailyMedicationData(SubDailyManager.getDailyMedicationStringData());
        } catch (FileReadWriteException e) {
            throw new FileReadWriteException("IO Error: Unable to write to JSON File");
        }
    }

    /**
     * Fetches the corresponding DailyMedication and set the medication to not taken
     *
     * @param listIndex Index of the dailyMedications list to update (1-based indexing)
     * @throws FileReadWriteException when unable to write to textfile
     * @see DailyMedication#untake()
     */
    public static void untakeDailyMedication(int listIndex) throws FileReadWriteException {
        DailyMedication dailyMedication = SubDailyManager.getMorningMedication(listIndex);
        dailyMedication.untake();
        try {
            FileReaderWriter.saveDailyMedicationData(SubDailyManager.getDailyMedicationStringData());
        } catch (FileReadWriteException e) {
            throw new FileReadWriteException("IO Error: Unable to write to text File");
        }
    }

    /**
     * Returns the total number of daily medications in the list.
     *
     * @return The total number of daily medications.
     */
    public static int getTotalDailyMedication() {
        return dailyMedications.size();
    }
}
