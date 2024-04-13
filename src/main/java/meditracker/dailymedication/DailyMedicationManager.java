package meditracker.dailymedication;

import meditracker.exception.InsufficientQuantityException;
import meditracker.exception.MedicationNotFoundException;
import meditracker.exception.MedicationUnchangedException;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;
import meditracker.storage.FileReaderWriter;
import meditracker.time.MediTrackerTime;
import meditracker.time.Period;
import meditracker.ui.Ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages a list of DailyMedication and CRUD-operations (Create, Read, Update, Delete)
 *
 * @see DailyMedication
 */
public class DailyMedicationManager {
    private static final List<DailyMedication> morningMedications = new ArrayList<>();
    private static final List<DailyMedication> afternoonMedications = new ArrayList<>();
    private static final List<DailyMedication> eveningMedications = new ArrayList<>();
    private static final LocalDate currentDate = MediTrackerTime.getCurrentDate();

    /**
     * Prevents defaulting to the public constructor
     * that allows instantiation of the DailyMedicationManager class
     */
    private DailyMedicationManager() {
    }

    /**
     * Creates DailyMedicationManager to save medications from MedicationManager
     * so that program can output to textfile
     *
     * @see DailyMedication
     */
    public static void createDailyMedicationManager() {
        clearDailyMedication(); // For the case of overwriting the txt save file
        for (Medication medication : MedicationManager.getMedications()) {
            if (doesBelongToDailyList(medication)) {
                addToSubLists(medication);
            }
        }
    }

    /**
     * Reads each lines from textfile to process and save into DailyMedicationManager
     *
     * @param lines lines of String read from each row in the textfile
     */
    public static void importDailyMedicationManager(List<String> lines) {
        clearDailyMedication(); // For the case of overwriting with the txt save file
        try {
            for (String line : lines) {
                parseImportedLine(line);
            }
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }
    }

    /**
     * Separates each row by the separator and add into the DailyMedicationManager
     *
     * @param line each line read from the textfile
     * @return dailyMedication object to add into the DailyMedicationManager
     */
    private static void parseImportedLine(String line) {
        String[] fields = line.split("\\|");
        boolean isTaken = Boolean.parseBoolean(fields[1].trim());
        DailyMedication dailyMedication = new DailyMedication(fields[2].trim());
        if (isTaken) {
            dailyMedication.take();
        } else {
            dailyMedication.untake();
        }
        addImportToSubLists(fields[0], dailyMedication);
    }

    /**
     * Imports data from the read text file
     *
     * @param period time of the day
     * @param dailyMedication daily medication to be taken for the day to add to respective sub lists
     */
    private static void addImportToSubLists(String period, DailyMedication dailyMedication) {
        if (period.equals("M")) {
            addDailyMedication(dailyMedication, Period.MORNING);
        } else if (period.equals("A")) {
            addDailyMedication(dailyMedication, Period.AFTERNOON);
        } else {
            addDailyMedication(dailyMedication, Period.EVENING);
        }
    }

    /**
     * Clears and resets DailyMedicationManager for testing purpose
     */
    protected static void clearDailyMedication() {
        morningMedications.clear();
        afternoonMedications.clear();
        eveningMedications.clear();
    }

    /**
     * Adds a DailyMedication to the list of DailyMedication
     *
     * @param dailyMedication DailyMedication to be added to the list
     */
    public static void addDailyMedication(DailyMedication dailyMedication, Period period) {
        switch (period) {
        case MORNING:
            morningMedications.add(dailyMedication);
            break;
        case AFTERNOON:
            afternoonMedications.add(dailyMedication);
            break;
        case EVENING:
            eveningMedications.add(dailyMedication);
            break;
        default:
            System.out.println("Cannot add to sublist");
        }
    }

    /**
     * Removes the DailyMedication object in the corresponding Period list that
     * matches the specified name.
     *
     * @param name Name of the DailyMedications object to get
     * @param period Time period of day (Morning, afternoon or evening)
     * @throws MedicationNotFoundException No DailyMedication matching specified name found
     */
    public static void removeDailyMedication(String name, Period period)
            throws MedicationNotFoundException {
        int listIndex = getDailyMedicationIndex(name, period);
        switch (period) {
        case MORNING:
            morningMedications.remove(listIndex);
            break;
        case AFTERNOON:
            afternoonMedications.remove(listIndex);
            break;
        case EVENING:
            eveningMedications.remove(listIndex);
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + period);
        }
    }

    /**
     * Gets the DailyMedication object from the morning/afternoon/evening lists
     * Also converts the index to 0-based indexing before being used.
     *
     * @param listIndex Index of the dailyMedications list to update (1-based indexing)
     * @param period Time period of day (Morning, afternoon or evening)
     * @return DailyMedication object at the corresponding index (0-based indexing)
     * @throws IndexOutOfBoundsException Out of range index specified
     */
    public static DailyMedication getDailyMedication(int listIndex, Period period) throws IndexOutOfBoundsException {
        listIndex--; // Decremented to 0-base indexing

        switch (period) {
        case MORNING:
            return morningMedications.get(listIndex);
        case AFTERNOON:
            return afternoonMedications.get(listIndex);
        case EVENING:
            return eveningMedications.get(listIndex);
        default:
            throw new IllegalStateException("Unexpected value: " + period);
        }
    }

    /**
     * Gets the DailyMedication object from the morning/afternoon/evening lists
     *
     * @param name Name of the DailyMedications object to get
     * @param period Time period of day (Morning, afternoon or evening)
     * @return DailyMedication object that matches the specified name
     * @throws IndexOutOfBoundsException Out of range index specified
     * @throws MedicationNotFoundException No DailyMedication matching specified name found
     */
    public static DailyMedication getDailyMedication(String name, Period period)
            throws IndexOutOfBoundsException, MedicationNotFoundException {
        int listIndex = getDailyMedicationIndex(name, period); // 0-based indexing
        listIndex++; // Convert to 1-based indexing
        return getDailyMedication(listIndex, period);
    }

    /**
     * Gets the DailyMedication index in the morning/afternoon/evening lists
     *
     * @param name Name of the DailyMedications object to get
     * @param period Time period of day (Morning, afternoon or evening)
     * @return Index of the DailyMedication object that matches the specified name
     * @throws MedicationNotFoundException No DailyMedication matching specified name found
     */
    public static int getDailyMedicationIndex(String name, Period period)
            throws MedicationNotFoundException {
        List<DailyMedication> dailyMedications = getDailyMedications(period);
        for (int i = 0; i < dailyMedications.size(); i++) {
            DailyMedication dailyMedication = dailyMedications.get(i);
            if (dailyMedication.getName().equals(name)) {
                return i;
            }
        }
        throw new MedicationNotFoundException();
    }

    /**
     * Gets the relevant list of DailyMedication objects depending on the time period of day
     *
     * @param period Time period of day (Morning, afternoon or evening)
     * @return The relevant list of DailyMedication objects
     */
    public static List<DailyMedication> getDailyMedications(Period period) {
        switch (period) {
        case MORNING:
            return morningMedications;
        case AFTERNOON:
            return afternoonMedications;
        case EVENING:
            return eveningMedications;
        default:
            throw new IllegalStateException("Unexpected value: " + period);
        }
    }

    /**
     * Prints all medications to be taken today
     *
     * @param medications list of medications from MedicationManager
     */
    public static void printTodayMedications(List<Medication> medications) {
        System.out.println("Here are the Daily Medications you have to take today: ");
        printTodayMedications(medications, morningMedications, "Morning:");
        printTodayMedications(medications, afternoonMedications, "Afternoon:");
        printTodayMedications(medications, eveningMedications, "Evening:");
    }

    /**
     * Prints the sub lists according to the period of the day
     *
     * @param medications list of medications from MedicationManager
     * @param subList sublist of daily medication
     * @param period time of the day
     */
    public static void printTodayMedications(List<Medication> medications,
                                             List<DailyMedication> subList, String period) {
        if (!subList.isEmpty()) {
            System.out.println(period);
            Ui.printMedsLists(medications, subList, period);
        }
    }

    /**
     * Fetches the corresponding DailyMedication and set the medication to taken
     *
     * @param listIndex Index of the dailyMedications list to update (1-based indexing)
     * @param period Time period of day (Morning, afternoon or evening)
     * @throws IndexOutOfBoundsException If listIndex is outside of range of DailyMedication list
     * @throws InsufficientQuantityException Existing quantity insufficient for operation
     * @throws MedicationNotFoundException Medication object not found, unable to decrease quantity
     * @throws MedicationUnchangedException Medication object no state change, already taken
     * @see DailyMedication#take()
     */
    public static void takeDailyMedication(int listIndex, Period period)
            throws IndexOutOfBoundsException, InsufficientQuantityException, MedicationNotFoundException,
            MedicationUnchangedException {
        DailyMedication dailyMedication = DailyMedicationManager.getDailyMedication(listIndex, period);
        if (dailyMedication.isTaken()) {
            // Already taken, do not need to run additional code
            throw new MedicationUnchangedException();
        }

        MedicationManager.decreaseMedicationQuantity(dailyMedication.getName(), period);

        dailyMedication.take();
        FileReaderWriter.saveDailyMedicationData(null);
    }

    /**
     * Fetches the corresponding DailyMedication and set the medication to not taken
     *
     * @param listIndex Index of the dailyMedications list to update (1-based indexing)
     * @param period Time period of day (Morning, afternoon or evening)
     * @throws IndexOutOfBoundsException If listIndex is outside of range of DailyMedication list
     * @throws MedicationNotFoundException Medication object not found, unable to increase quantity
     * @throws MedicationUnchangedException Medication object no state change, already untaken
     * @see DailyMedication#untake()
     */
    public static void untakeDailyMedication(int listIndex, Period period)
            throws IndexOutOfBoundsException, MedicationNotFoundException, MedicationUnchangedException {
        DailyMedication dailyMedication = DailyMedicationManager.getDailyMedication(listIndex, period);
        if (!dailyMedication.isTaken()) {
            // Already untaken, do not need to run additional code
            throw new MedicationUnchangedException();
        }

        MedicationManager.increaseMedicationQuantity(dailyMedication.getName(), period);

        dailyMedication.untake();
        FileReaderWriter.saveDailyMedicationData(null);
    }

    /**
     * Takes each DailyMedication object and adds to string of each object
     * to a list of String and return
     *
     * @return A list of DailyMedication object to string
     */
    public static List<String> getDailyMedicationStringData() {
        List<String> dailyMedicationStrings = new ArrayList<>();
        for (DailyMedication morningMedication : morningMedications) {
            dailyMedicationStrings.add("M|" + morningMedication.isTaken() + "|" + morningMedication.getName());
        }
        for (DailyMedication afternoonMedication : afternoonMedications) {
            dailyMedicationStrings.add("A|" + afternoonMedication.isTaken() + "|" + afternoonMedication.getName());
        }
        for (DailyMedication eveningMedication : eveningMedications) {
            dailyMedicationStrings.add("E|" + eveningMedication.isTaken() + "|" + eveningMedication.getName());
        }
        return dailyMedicationStrings;
    }

    /**
     * Checks if added medication is to be taken at morning/afternoon/evening
     *
     * @param medication list of medications from MedicationManager
     */
    public static void checkForDaily(Medication medication) {
        if (doesBelongToDailyList(medication)) {
            addToSubLists(medication);
        }
    }

    /**
     * Returns boolean if medication can be added to today's daily list based on the repeat setting
     *
     * @param medication list of medications from MedicationManager
     * @return true if medication can be added to today's list
     */
    public static boolean doesBelongToDailyList(Medication medication) {
        int num = medication.getRepeat();

        switch (num) {
        case 1:
            return true;                     //everyday
        case 2:
            return isNDay(medication, 2); //every 2 days
        case 3:
            return isNDay(medication, 3); //every 3 days
        case 4:
            return isNDay(medication, 4); //every 4 days
        case 5:
            return isNDay(medication, 5); //every 5 days
        case 6:
            return isNDay(medication, 6); //every 6 days
        case 7:
            return isNDay(medication, 7); //every 7 days
        default:
            return false;
        }
    }

    /**
     * Determines the difference between today's date and the date medication was added
     *
     * @param medication list of medications from MedicationManager
     * @param n repeat set by user
     * @return true if the modulus of the date difference is 0
     */
    private static boolean isNDay(Medication medication, int n) {
        int dateNum = medication.getDayAdded();
        int currentDayValue = currentDate.getDayOfYear();
        int daysDiff = currentDayValue - dateNum;
        return daysDiff % n == 0;       //modulo to find out if dailyMedication can be added to today's list
    }

    /**
     * Adds dailyMedication into sub list morning/afternoon/evening
     * and writes into the text file
     *
     * @param medication list of medications from MedicationManager
     */
    private static void addToSubLists(Medication medication) {
        for (Period period : Period.values()) {
            switch (period) {
            case MORNING: // fall through
            case AFTERNOON: // fall through
            case EVENING:
                if (!medication.hasDosage(period)) {
                    continue;
                }
                DailyMedication dailyMedication = new DailyMedication(medication.getName());
                addDailyMedication(dailyMedication, period);
                break;
            default:
                break;
            }
        }
        FileReaderWriter.saveDailyMedicationData(null);
    }
}
