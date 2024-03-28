package meditracker.medication;

import meditracker.argument.ArgumentName;
import meditracker.logging.MediLogger;
import meditracker.ui.Ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The MedicationManager class represents a list of medications.
 * It contains an ArrayList of Medication objects.
 */
public class MedicationManager {
    private static Logger logger = MediLogger.getMediLogger();
    /** The list of medications stored in an ArrayList. */
    private List<Medication> medications = new ArrayList<>();

    /**
     * Constructs an empty MedicationList.
     */
    public MedicationManager() {
    }

    /**
     * Constructs a MedicationManager with the specified list of medications.
     * @param medications The list of medications to be stored.
     */
    public MedicationManager(List<Medication> medications) {
        this.medications = medications;
    }

    /**
     * Gets the size of list of medications
     * @return Total number of medications
     */
    public Integer getTotalMedications() {
        return medications.size();
    }

    /**
     * Adds a Medication to the list of Medication
     *
     * @param medication Medication to be added to the list
     */
    public void addMedication(Medication medication) {
        medications.add(medication);
    }

    /**
     * Gets the Medication object from the medications list.
     * Also converts the index to 0-based indexing before being used.
     *
     * @param listIndex Index of the medications list to get (1-based indexing)
     * @return Medication object at the corresponding index (0-based indexing)
     * @throws IndexOutOfBoundsException Out of range index specified
     */
    public Medication getMedication(int listIndex) throws IndexOutOfBoundsException {
        listIndex--; // Decremented to 0-base indexing
        return medications.get(listIndex);
    }

    public List<Medication> getMedications() {
        return medications;
    }

    /**
     * Deletes the Medication object from the medications list.
     * Also converts the index to 0-based indexing before being used.
     *
     * @param listIndex Index of the medications list to delete (1-based indexing)
     * @throws IndexOutOfBoundsException Out of range index specified
     */
    public void removeMedication(int listIndex) throws IndexOutOfBoundsException {
        listIndex--; // Decremented to 0-base indexing
        medications.remove(listIndex);
    }

    /**
     * Shows the number of medications in the medication list.
     * Also lists all the medications in the medication list.
     */
    public void printAllMedications() {
        assert medications != null;
        System.out.println("You have " + getTotalMedications() + " medications listed below.");
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
            logger.warning("Possibly corrupt data. Unable to parse String '" + doubleString +
                    "' into double. Using placeholder value -1.0");
            return placeholderValue;
        } catch (NullPointerException e ) {
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
    public void addMedicationFromSaveFile(List<Map<String,String>> medInfoList) {
        for (Map<String,String> medInfo : medInfoList) {
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
                    medication.setRepeat(value);
                    break;
                default:
                    logger.warning("Unhandled ArgumentName Enum Type " + keyEnum.value);
                }
            }
            this.addMedication(medication);
        }
    }
}
