package meditracker;

import meditracker.exception.FileReadWriteException;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;
import meditracker.medication.Repeat;
import meditracker.storage.FileReaderWriter;
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
    private static final List<DailyMedication> dailyMedications = new ArrayList<>();
    private static final List<DailyMedication> morningMedications = new ArrayList<>();
    private static final List<DailyMedication> afternoonMedications = new ArrayList<>();
    private static final List<DailyMedication> eveningMedications = new ArrayList<>();
    private static final LocalDate currentDate = LocalDate.now();

    /**
     * Creates DailyMedicationManager to save medications from MedicationManager
     * so that program can output to textfile
     *
     * @param medicationManager gets the medications list
     * @see DailyMedication
     */
    public static void createDailyMedicationManager(MedicationManager medicationManager) {
        assert medicationManager != null;
        for (Medication medication : medicationManager.getMedications()) {
            if (shouldAddToDailyList(medication)) {
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
        try {
            for (String line : lines) {
                parseImportedLine(line);
            }
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }
    }

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
     * Similarly, for getMorningMedication, getAfterMedication, getEveningMedication
     *
     * @param listIndex Index of the dailyMedications list to update (1-based indexing)
     * @return DailyMedication object at the corresponding index (0-based indexing)
     * @throws IndexOutOfBoundsException Out of range index specified
     */
    public static DailyMedication getDailyMedication(int listIndex) throws IndexOutOfBoundsException {
        listIndex--; // Decremented to 0-base indexing
        return dailyMedications.get(listIndex);
    }

    public static DailyMedication getMorningMedication(int listIndex) throws IndexOutOfBoundsException {
        listIndex--; // Decremented to 0-base indexing
        return morningMedications.get(listIndex);
    }

    public static DailyMedication getAfternoonMedication(int listIndex) throws IndexOutOfBoundsException {
        listIndex--; // Decremented to 0-base indexing
        return afternoonMedications.get(listIndex);
    }

    public static DailyMedication getEveningMedication(int listIndex) throws IndexOutOfBoundsException {
        listIndex--; // Decremented to 0-base indexing
        return eveningMedications.get(listIndex);
    }

    /**
     * Fetches the corresponding DailyMedication and set the medication to taken
     *
     * @param listIndex Index of the dailyMedications list to update (1-based indexing)
     * @throws FileReadWriteException when unable to write to textfile
     * @see DailyMedication#take()
     */
    public static void takeDailyMedication(int listIndex) throws FileReadWriteException {
        DailyMedication dailyMedication = getDailyMedication(listIndex);
        dailyMedication.take();
        try {
            FileReaderWriter.saveDailyMedicationData(getDailyMedicationStringData());
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
        DailyMedication dailyMedication = getDailyMedication(listIndex);
        dailyMedication.untake();
        try {
            FileReaderWriter.saveDailyMedicationData(getDailyMedicationStringData());
        } catch (FileReadWriteException e) {
            throw new FileReadWriteException("IO Error: Unable to write to text File");
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
            morningMedications.add(dailyMedication);
        } else if (period.equals("A")) {
            afternoonMedications.add(dailyMedication);
        } else {
            eveningMedications.add(dailyMedication);
        }
    }

    /**
     * Takes each DailyMedication object and adds to string of each object
     * to a list of String and return
     *
     * @return A list of DailyMedication object to string
     */
    private static List<String> getDailyMedicationStringData() {
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
     * Returns the total number of daily medications in the list.
     * Similarly, for the getMorningMedications/getAfternoonMedications/getEveningMedications
     * @return The total number of daily medications.
     */
    public static int getTotalDailyMedication() {
        return dailyMedications.size();
    }

    public static List<DailyMedication> getMorningMedications() {
        return morningMedications;
    }

    public static List<DailyMedication> getAfternoonMedications() {
        return afternoonMedications;
    }

    public static List<DailyMedication> getEveningMedications() {
        return eveningMedications;
    }

    /**
     * Checks if added medication is to be taken at morning/afternoon/evening
     *
     * @param medication list of medications from MedicationManager
     */
    public static void checkForDaily(Medication medication) {
        if (shouldAddToDailyList(medication)) {
            addToSubLists(medication);
        }
    }

    /**
     * Returns boolean if medication can be added to today's daily list based on the repeat setting
     *
     * @param medication list of medications from MedicationManager
     * @return true if medication can be added to today's list
     */
    private static boolean shouldAddToDailyList(Medication medication) {
        Repeat repeat = Repeat.repeatDays(medication.getRepeat());

        switch (repeat) {
        case DAILY:
            return true;                       //everyday
        case EVERY_OTHER_DAY:
            return isNDay(medication, 2); //every 2 days
        case EVERY_3_DAYS:
            return isNDay(medication, 3); //every 3 days
        case EVERY_4_DAYS:
            return isNDay(medication, 4); //every 4 days
        case EVERY_WEEK:
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
        DailyMedication dailyMedication = new DailyMedication(medication.getName());
        if(medication.getDosageMorning() != 0.0) {
            morningMedications.add(dailyMedication);
        }
        if(medication.getDosageAfternoon() != 0.0) {
            afternoonMedications.add(dailyMedication);
        }
        if(medication.getDosageEvening() != 0.0) {
            eveningMedications.add(dailyMedication);
        }
        try {
            FileReaderWriter.saveDailyMedicationData(getDailyMedicationStringData());
        } catch (FileReadWriteException e) {
            System.out.println("Cannot write into today.txt");
        }
    }
}
