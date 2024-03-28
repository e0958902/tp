package meditracker.medication;

import meditracker.DailyMedication;
import meditracker.exception.FileReadWriteException;
import meditracker.storage.FileReaderWriter;
import meditracker.ui.Ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages 3 sub lists of DailyMedication for different times of the day
 *
 * @see DailyMedication
 */

public class SubDailyManager {
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
     * Gets the DailyMedication object from each of morning list
     * Also converts the index to 0-based indexing before being used.
     * Similarly, for afternoon and evening lists
     *
     * @param listIndex
     * @return
     * @throws IndexOutOfBoundsException
     */
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
