package meditracker.medication;

import meditracker.argument.ArgumentName;
import meditracker.exception.InsufficientQuantityException;
import meditracker.exception.MediTrackerException;
import meditracker.exception.MedicationNotFoundException;
import meditracker.storage.FileReaderWriter;
import meditracker.time.Period;
import meditracker.ui.Ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The MedicationManager class represents a list of medications.
 * It contains an ArrayList of Medication objects.
 */
public class MedicationManager {
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
     * Clears and resets MedicationManager
     * Used by tests and overwriting from the JSON save file.
     */
    protected static void clearMedication() {
        medications.clear();
    }

    /**
     * Adds a Medication to the list of Medication
     *
     * @param medication Medication to be added to the list
     * @throws MediTrackerException When a duplicate medication is found
     */
    public static void addMedication(Medication medication) throws MediTrackerException {
        medication.checkValidity();
        checkForDuplicateMedication(medication.getName());
        medications.add(medication);
        FileReaderWriter.saveMediTrackerData(null);
    }

    protected static void addMedicationWithoutChecks(Medication medication) {
        medications.add(medication);
        FileReaderWriter.saveMediTrackerData(null);
    }

    /**
     * Checks for duplicate medication in the list of medications.
     *
     * @param name Name of the medication to retrieve
     * @throws MediTrackerException When a duplicate medication is found
     */
    private static void checkForDuplicateMedication(String name) throws MediTrackerException {
        name = name.toLowerCase();
        for (Medication medication : medications) {
            if (medication.getName().toLowerCase().equals(name)) {
                throw new MediTrackerException("Medication already exists in the list!");
            }
        }
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

    /**
     * Gets the Medication object from the medications list.
     * Uses the Medication name to retrieve from the list.
     *
     * @param name Name of the medication to retrieve
     * @return Corresponding Medication object with the matched name
     * @throws MedicationNotFoundException No Medication matching the specified name found
     */
    public static Medication getMedication(String name) throws MedicationNotFoundException {
        for (Medication medication : medications) {
            if (medication.getName().equals(name)) {
                return medication;
            }
        }
        throw new MedicationNotFoundException();
    }

    /**
     * Gets the Medication object from the medications list.
     * Uses the Medication quantity to retrieve medications from the list.
     * Shows all medications that is equal to or less than specified quantity,
     *
     * @param quantity Quantity of the medication to retrieve in double
     * @throws MedicationNotFoundException No Medication matching specified name found
     */
    public static void showMedicationsByQuantity(Double quantity) throws MedicationNotFoundException {
        int medicationsFound = 0;

        for (Medication medication : medications) {
            Double medicationListQuantity = medication.getQuantity();
            if (Double.compare(medicationListQuantity, quantity) <= 0) {
                medicationsFound++;
                Ui.printSpecificMed(medication);
            }
        }
        if (medicationsFound == 0) {
            throw new MedicationNotFoundException();
        }
    }

    /**
     * Gets the Medication object from the medications list.
     * Uses the Medication name to retrieve medications from the list.
     * Shows all medications that contains the name.
     *
     * @param name Name of the medication to find and retrieve
     * @throws MedicationNotFoundException No Medication matching specified name found
     */
    public static void showMedicationsByName(String name) throws MedicationNotFoundException {
        int medicationsFound = 0;
        String nameToSearch = name.toLowerCase();
        for (Medication medication : medications) {
            String medicationName = medication.getName().toLowerCase();
            if (medicationName.contains(nameToSearch)) {
                medicationsFound++;
                Ui.printSpecificMed(medication);
            }
        }
        if (medicationsFound == 0) {
            throw new MedicationNotFoundException();
        }
    }

    /**
     * Gets the Medication object from the medications list.
     * Uses the Medication expiry to retrieve medications from the list.
     * Shows all medications that will expire by the year that the user has input.
     *
     * @param expiry Expiry of the medication to find and retrieve
     * @throws MedicationNotFoundException No Medication matching specified name found
     */
    public static void showMedicationsByExpiry(String expiry) throws MedicationNotFoundException {
        int medicationsFound = 0;

        for (Medication medication : medications) {
            int medicationYear = medication.getExpiryDate().getYear();
            int userYear = Integer.parseInt(expiry);

            if (medicationYear <= userYear) {
                medicationsFound++;
                Ui.printSpecificMed(medication);
            }
        }
        if (medicationsFound == 0) {
            throw new MedicationNotFoundException();
        }
    }

    /**
     * Gets the Medication object from the medications list.
     * Uses the Medication remarks to retrieve medications from the list.
     *
     * @param remarks Remarks of the medication to find and retrieve
     * @throws MedicationNotFoundException No Medication matching specified name found
     */
    public static void showMedicationsByRemarks(String remarks) throws MedicationNotFoundException {
        int medicationsFound = 0;
        String remarksToSearch = remarks.toLowerCase();
        for (Medication medication : medications) {
            String medicationRemarks = medication.getRemarks().toLowerCase();
            if (medicationRemarks.contains(remarksToSearch)) {
                medicationsFound++;
                Ui.printSpecificMed(medication);
            }
        }
        if (medicationsFound == 0) {
            throw new MedicationNotFoundException();
        }
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
        FileReaderWriter.saveMediTrackerData(null);
    }

    /**
     * Gets the dosage from the Medication object based the specified Period
     *
     * @param medication Medication object to obtain the dosage from
     * @param period Time period of day to reference
     * @return The appropriate dosage depending on the time Period
     */
    public static Double getMedicationDosage(Medication medication, Period period) {
        double dosage;
        switch (period) {
        case MORNING:
            dosage = medication.getDosageMorning();
            break;
        case AFTERNOON:
            dosage = medication.getDosageAfternoon();
            break;
        case EVENING:
            dosage = medication.getDosageEvening();
            break;
        case UNKNOWN:
        case NONE:
        default:
            dosage = 0.0;
            break;
        }

        return dosage;
    }

    /**
     * Shows the number of medications in the medication list.
     * Also lists all the medications in the medication list.
     */
    public static void printAllMedications() {
        assert medications != null;
        Ui.printMedicationList(medications);
    }

    /**
     * Prints the specific medication specific according to the medication list index.
     * 
     * @param listIndex The index of the medication in the Medication list
     * @throws IndexOutOfBoundsException Out of range index specified
     * @throws NullPointerException Trying to access non-existent object
     * @throws NumberFormatException When a string is keyed in for the index
     */
    public static void printSpecificMedication(int listIndex)
            throws IndexOutOfBoundsException, NullPointerException, NumberFormatException  {
        assert medications != null;
        Medication medication = MedicationManager.getMedication(listIndex);
        Ui.printSpecificMed(medication);
    }

    /**
     * Populates the MedicationManager from the save file.
     * If there are corrupt data, it may be substituted with placeholder values.
     *
     * @param medInfoList The List of medication information that contains the (String, String) key-value.
     */
    public static void addMedicationFromSaveFile(List<Map<String, String>> medInfoList) {
        clearMedication(); // Reset for the case of overwriting data with another JSON file.
        for (Map<String, String> medInfo : medInfoList) {
            Medication medication = new Medication();
            for (String key : medInfo.keySet()) {
                ArgumentName keyEnum = ArgumentName.getEnumOfArgumentValue(key);
                if (keyEnum == null) {
                    continue;
                }
                String value = medInfo.get(key);

                try {
                    medication.setMedicationValue(keyEnum, value);
                } catch (MediTrackerException e) {
                    Ui.showErrorMessage(e);
                }
            }
            try {
                addMedication(medication);
            } catch (MediTrackerException e) {
                Ui.showErrorMessage(e);
            }
        }
    }

    /**
     * Increases the medication quantity based on the specified time period
     *
     * @param medicationName Name of the medication to increase medication quantity
     * @param period Time period of day to reference
     * @throws MedicationNotFoundException No Medication matching specified name found
     */
    public static void increaseMedicationQuantity(String medicationName, Period period)
            throws MedicationNotFoundException {
        Medication medication = getMedication(medicationName);
        double dosage = getMedicationDosage(medication, period);
        double oldQuantity = medication.getQuantity();
        double newQuantity = oldQuantity + dosage;

        String message = String.format("Medication quantity increased: %.1f -> %.1f",
                oldQuantity,
                newQuantity);
        Ui.showInfoMessage(message);

        medication.setQuantity(newQuantity);
        FileReaderWriter.saveMediTrackerData(null);
    }

    /**
     * Decreases the medication quantity based on the specified time period
     *
     * @param medicationName Name of the medication to decrease medication quantity
     * @param period Time period of day to reference
     * @throws MedicationNotFoundException No Medication matching specified name found
     * @throws InsufficientQuantityException Existing quantity insufficient for operation
     */
    public static void decreaseMedicationQuantity(String medicationName, Period period)
            throws MedicationNotFoundException, InsufficientQuantityException {
        Medication medication = getMedication(medicationName);
        double dosage = getMedicationDosage(medication, period);
        double oldQuantity = medication.getQuantity();
        double newQuantity = oldQuantity - dosage;

        if (newQuantity < 0) {
            throw new InsufficientQuantityException(dosage, oldQuantity);
        }

        String message = String.format("Medication quantity decreased: %.1f -> %.1f",
                oldQuantity,
                newQuantity);
        Ui.showInfoMessage(message);

        medication.setQuantity(newQuantity);
        FileReaderWriter.saveMediTrackerData(null);
    }
}
