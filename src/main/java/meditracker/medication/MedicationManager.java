package meditracker.medication;

import static meditracker.storage.FileReaderWriter.saveMediTrackerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import meditracker.argument.ArgumentName;
import meditracker.logging.MediLogger;
import meditracker.ui.Ui;

/**
 * The MedicationManager class represents a list of medications.
 * It contains an ArrayList of Medication objects.
 */
public class MedicationManager {
    private static Logger logger = MediLogger.getMediLogger();
    /** The list of medications stored in an ArrayList. */
    private static List<Medication> medications = new ArrayList<>();

    /**
     * Prevents defaulting to the public constructor
     * that allows instantiation of the MedicationManager class
     */
    private MedicationManager() {
    }

    /**
     * Gets the size of list of medications
     * @return Total number of medications
     */
    public static Integer getTotalMedications() {
        return medications.size();
    }

    /**
     * Clears and resets MedicationManager for testing purpose
     */
    protected static void clearMedication() {
        medications.clear();
    }

    /**
     * Adds a Medication to the list of Medication
     *
     * @param medication Medication to be added to the list
     */
    public static void addMedication(Medication medication) {
        medications.add(medication);
        saveMediTrackerData();
    }

    /**
     * Gets the Medication object from the medications list.
     * Also converts the index to 0-based indexing before being used.
     *
     * @param listIndex Index of the medications list to get (1-based indexing)
     * @return Medication object at the corresponding index (0-based indexing)
     * @throws IndexOutOfBoundsException Out of range index specified
     */
    public static Medication getMedication(int listIndex) throws IndexOutOfBoundsException {
        listIndex--; // Decremented to 0-base indexing
        return medications.get(listIndex);
    }

    public static List<Medication> getMedications() {
        return medications;
    }

    /**
     * Deletes the Medication object from the medications list.
     * Also converts the index to 0-based indexing before being used.
     *
     * @param listIndex Index of the medications list to delete (1-based indexing)
     * @throws IndexOutOfBoundsException Out of range index specified
     */
    public static void removeMedication(int listIndex) throws IndexOutOfBoundsException {
        listIndex--; // Decremented to 0-base indexing
        medications.remove(listIndex);
        saveMediTrackerData();
    }

    /**
     * Shows the number of medications in the medication list.
     * Also lists all the medications in the medication list.
     */
    public static void printAllMedications() {
        assert medications != null;
        System.out.println("You have " + getTotalMedications() + " medications listed below.");
        System.out.println("Format: Name | Quantity | Expiry Date | Remarks");
        Ui.printMedsList(medications);
    }

    //@@author annoy-o-mus
    /**
     * Converts a String to a double.
     * Introduced to help populate the Medication object from the save file.
     *
     * @param doubleString The String object to be converted to a double type.
     * @return The value of type double. Placeholder value of -1.0 if an exception is thrown.
     */
    private static double convertStringToDouble(String doubleString) {
        double placeholderValue = -1.0;

        try {
            return Double.parseDouble(doubleString);
        } catch (NumberFormatException e) {
            logger.warning("Possibly corrupt data. Unable to parse String '" + doubleString
                    + "' into double. Using placeholder value -1.0");
            return placeholderValue;
        } catch (NullPointerException e) {
            logger.warning("Null Pointer passed for conversion to double. Using placeholder value -1.0");
            return placeholderValue;
        }
    }


    //@@author annoy-o-mus
    /**
     * Populates the MedicationManager from the save file.
     * If there are corrupt data, it may be substituted with placeholder values.
     *
     * @param medInfoList The List of medication information that contains the (String, String) key-value.
     */
    public static void addMedicationFromSaveFile(List<Map<String, String>> medInfoList) {
        for (Map<String, String> medInfo : medInfoList) {
            Medication medication = new Medication();
            for (String key : medInfo.keySet()) {
                ArgumentName keyEnum = ArgumentName.getEnumOfArgumentValue(key);
                if (keyEnum == null) {
                    continue;
                }
                String value = medInfo.get(key);

                switch (keyEnum) {
                case NAME:
                    medication.setName(value);
                    break;
                case QUANTITY:
                    double qty = convertStringToDouble(value);
                    medication.setQuantity(qty);
                    break;
                case DOSAGE:
                    double dose = convertStringToDouble(value);
                    medication.setDosage(dose);
                    break;
                case DOSAGE_MORNING:
                    double doseMorning = convertStringToDouble(value);
                    medication.setDosageMorning(doseMorning);
                    break;
                case DOSAGE_AFTERNOON:
                    double doseAfternoon = convertStringToDouble(value);
                    medication.setDosageAfternoon(doseAfternoon);
                    break;
                case DOSAGE_EVENING:
                    double doseEvening = convertStringToDouble(value);
                    medication.setDosageEvening(doseEvening);
                    break;
                case EXPIRATION_DATE:
                    medication.setExpiryDate(value);
                    break;
                case INTAKE_FREQUENCY:
                    medication.setIntakeFreq(value);
                    break;
                case REMARKS:
                    medication.setRemarks(value);
                    break;
                case REPEAT:
                    int repeatValue = (int) convertStringToDouble(value);
                    medication.setRepeat(repeatValue);
                    break;
                case DAY_ADDED:
                    int dayValue = (int) convertStringToDouble(value);
                    medication.setDayAdded(dayValue);
                    break;
                default:
                    logger.warning("Unhandled ArgumentName Enum Type " + keyEnum.value);
                }
            }
            addMedication(medication);
        }
    }
}
