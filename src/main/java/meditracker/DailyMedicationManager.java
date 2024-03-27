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
 * @see DailyMedication
 */
public class DailyMedicationManager {
    private static final List<DailyMedication> dailyMedications = new ArrayList<>();
    private static final List<DailyMedication> morningMedications = new ArrayList<>();
    private static final List<DailyMedication> afternoonMedications = new ArrayList<>();
    private static final List<DailyMedication> eveningMedications = new ArrayList<>();
    private static final Medication medication = null;
    private static final LocalDate currentDate = LocalDate.now();

    /**
     * Creates DailyMedicationManager to save medications from MedicationManager
     * so that program can output to textfile
     *
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
                DailyMedication dailyMedication = parseImportedLine(line);
                addDailyMedication(dailyMedication);
                // TODO
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
        try {
            FileReaderWriter.saveDailyMedicationData(getDailyMedicationStringData());
        } catch (FileReadWriteException e) {
            System.out.println("Cannot write into today.txt");
        }
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
     * @see DailyMedication#take()
     */
    public static void takeDailyMedication(int listIndex) {
        DailyMedication dailyMedication = getDailyMedication(listIndex);
        dailyMedication.take();
        try {
            FileReaderWriter.saveDailyMedicationData(getDailyMedicationStringData());
        } catch (FileReadWriteException e) {
            // TODO: Handle exception
        }
    }

    /**
     * Fetches the corresponding DailyMedication and set the medication to not taken
     *
     * @param listIndex Index of the dailyMedications list to update (1-based indexing)
     * @see DailyMedication#untake()
     */
    public static void untakeDailyMedication(int listIndex) {
        DailyMedication dailyMedication = getDailyMedication(listIndex);
        dailyMedication.untake();
        try {
            FileReaderWriter.saveDailyMedicationData(getDailyMedicationStringData());
        } catch (FileReadWriteException e) {
            // TODO: Handle exception
        }
    }

    public static void printTodayMedications() {
        System.out.println("Here are the Daily Medications you have to take today: ");
        printTodayMedications(morningMedications, "Morning:");
        printTodayMedications(afternoonMedications, "Afternoon:");
        printTodayMedications(eveningMedications, "Evening:");
    }

    public static void printTodayMedications(List<DailyMedication> medicationList, String period) {
        if (!medicationList.isEmpty()) {
            System.out.println(period);
            Ui.printMedsList(medicationList);
        }
    }

    /**
     * Separates each row by the separator and add into the DailyMedicationManager
     * 
     * @param line each line read from the textfile
     * @return dailyMedication object to add into the DailyMedicationManager
     */
    private static DailyMedication parseImportedLine(String line) {
        String[] fields = line.split("\\|");
        boolean isTaken = Boolean.parseBoolean(fields[1].trim());
        DailyMedication dailyMedication = new DailyMedication(fields[2].trim());
        if (isTaken) {
            dailyMedication.take();
        } else {
            dailyMedication.untake();
        }
        return dailyMedication;
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
        }for (DailyMedication afternoonMedication : afternoonMedications) {
            dailyMedicationStrings.add("A|" + afternoonMedication.isTaken() + "|" + afternoonMedication.getName());
        }for (DailyMedication eveningMedication : eveningMedications) {
            dailyMedicationStrings.add("E|" + eveningMedication.isTaken() + "|" + eveningMedication.getName());
        }
        return dailyMedicationStrings;
    }

    /**
     * Returns the total number of daily medications in the list.
     *
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

    public static void checkForDaily(Medication medication) {
        if (shouldAddToDailyList(medication)) {
            addToSubLists(medication);
        }
    }

    // Method to determine if an item should be added to the daily list based on repeat setting
    private static boolean shouldAddToDailyList(Medication medication) {
        Repeat repeat = Repeat.repeatDays(medication.getRepeat());

        switch (repeat) {
        case DAILY:
            return true;
        case EVERY_OTHER_DAY:
            return isNDay(medication, 2);
        case EVERY_3_DAYS:
            return isNDay(medication, 3);
        case EVERY_4_DAYS:
            return isNDay(medication, 4);
        case EVERY_WEEK:
            return isNDay(medication, 7);
        default:
            return false;
        }
    }

    private static boolean isNDay(Medication medication, int n) {
        int dateNum = medication.getDayAdded();
        int currentDayValue = currentDate.getDayOfYear();
        int daysDiff = currentDayValue - dateNum;
        return daysDiff % n == 0;
    }

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
    }
}
