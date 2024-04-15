package meditracker.medication;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import meditracker.time.MediTrackerTime;
import org.json.JSONException;
import org.json.JSONObject;

import meditracker.argument.ArgumentName;
import meditracker.exception.MediTrackerException;
import meditracker.time.Period;

// @@author nickczh
/**
 * The Medication class represents a medication.
 * It stores information about the medication such as name, quantity, dosage, expiry date, intake frequency, remarks,
 * and whether it has been taken.
 */
public class Medication {

    private String name;
    private Double quantity;
    private Double dosageMorning;
    private Double dosageAfternoon;
    private Double dosageEvening;
    private LocalDate expiryDate;
    private String remarks;
    private int repeat;
    private int dayAdded;

    /**
     * Constructs a new Medication object with null values.
     */
    public Medication() {
    }

    /**
     * Constructs a Medication object with the specified information.
     *
     * @param name The name of the medication.
     * @param quantity The quantity of the medication.
     * @param dosageMorning The morning dosage of the medication.
     * @param dosageAfternoon The afternoon dosage of the medication.
     * @param dosageEvening The evening dosage of the medication.
     * @param expiryDate The expiry date of the medication.
     * @param remarks Any remarks or notes about the medication.
     * @param repeat The repeat frequency of the medication.
     */
    public Medication(String name, Double quantity,
                      Double dosageMorning, Double dosageAfternoon, Double dosageEvening,
                      LocalDate expiryDate, String remarks, int repeat, int dayAdded) {
        this.name = name;
        this.quantity = quantity;
        this.dosageMorning = dosageMorning;
        this.dosageAfternoon = dosageAfternoon;
        this.dosageEvening = dosageEvening;
        this.expiryDate = expiryDate;
        this.remarks = remarks;
        this.repeat = repeat;
        this.dayAdded = dayAdded;
    }

    // @@author

    /**
     * Checks the validity of the Medication object
     *
     * @throws MediTrackerException If Medication has uninitialised values or has no dosages
     */
    public void checkValidity() throws MediTrackerException {
        boolean isUninitialised =
                name == null ||
                quantity == null ||
                dosageMorning == null ||
                dosageAfternoon == null ||
                dosageEvening == null ||
                expiryDate == null ||
                remarks == null ||
                repeat == 0 ||
                dayAdded == 0;
        if (isUninitialised) {
            throw new MediTrackerException("Medication has uninitialised values. Discarding Medication.");
        }

        if (hasNoDosages()) {
            throw new MediTrackerException("Medication has no dosages. " +
                    "Please ensure at least 1 period of day has dosage (-dM, -dA and/or -dE).");
        }
    }

    // @@author nickczh

    public String getName() {
        return name;
    }

    /**
     * Sets the name of medication without checks
     *
     * @param name Name of the medication
     */
    protected void setNameUnchecked(String name) {
        this.name = name;
    }

    /**
     * Sets the name if it contains alphabetic characters and spaces only
     *
     * @param name Name of the medication
     * @throws MediTrackerException If medication name contains non-alphabetic characters
     */
    protected void setName(String name) throws MediTrackerException {
        // Check if the medication name contains only alphabetic characters
        boolean isAlphabetic = name.matches("^[a-zA-Z ]+$");

        // If the name contains non-alphabetic characters, throw an exception
        if (!isAlphabetic) {
            throw new MediTrackerException("Please enter a proper medication name.");
        }
        this.name = name;
    }

    public Double getQuantity() {
        return quantity;
    }

    protected void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getDosageMorning() {
        return dosageMorning;
    }

    protected void setDosageMorning(Double dosageMorning) {
        this.dosageMorning = dosageMorning;
    }

    public Double getDosageAfternoon() {
        return dosageAfternoon;
    }

    protected void setDosageAfternoon(Double dosageAfternoon) {
        this.dosageAfternoon = dosageAfternoon;
    }

    public Double getDosageEvening() {
        return dosageEvening;
    }

    protected void setDosageEvening(Double dosageEvening) {
        this.dosageEvening = dosageEvening;
    }

    // @@author

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    // @@author nickczh

    public String getRemarks() {
        return remarks;
    }

    /**
     * Checks and sets the remarks. Defaults to Nil if remarks is null.
     *
     * @param remarks Remarks value to checked and set
     */
    protected void setRemarks(String remarks) {
        this.remarks = Objects.requireNonNullElse(remarks, "Nil");
    }

    // @@author

    // @@author T0nyLin

    public int getRepeat() {
        return repeat;
    }

    /**
     * Sets the repeat value of Medication object without checks
     *
     * @param repeat  Repeat value to be set
     */
    protected void setRepeatUnchecked(int repeat) {
        this.repeat = repeat;
    }

    /**
     * Checks if repeat is within range of 1 to 7
     *
     * @param repeatStr Repeat value to be parsed, checked and set
     * @throws MediTrackerException When the value is not within the specified range
     */
    protected void setRepeat(String repeatStr) throws MediTrackerException {
        int repeat = convertStringToInteger(repeatStr);
        if (repeat < 1 || repeat > 7) {
            throw new MediTrackerException("Provide a \"-rep\" number from 1 to 7");
        }
        this.repeat = repeat;
    }

    public int getDayAdded() {
        return dayAdded;
    }

    protected void setDayAdded(int dayAdded) {
        this.dayAdded = dayAdded;
    }

    // @@author

    // @@author annoy-o-mus
    /**
     * Calls the setter method with the parsed data based on the argumentName specified.
     *
     * @param argumentName ArgumentName that identifies what type of data it is.
     * @param argumentValue Value of the data to be (converted and) set.
     * @throws MediTrackerException If it fails the checks and/or parsing.
     */
    public void setMedicationValue(ArgumentName argumentName, String argumentValue) throws MediTrackerException {
        switch (argumentName) {
        case NAME:
            setName(argumentValue);
            break;
        case QUANTITY:
            double quantity = convertStringToDouble(argumentValue);
            setQuantity(quantity);
            break;
        case DOSAGE_MORNING:
            double dosageMorning = convertStringToDouble(argumentValue);
            setDosageMorning(dosageMorning);
            break;
        case DOSAGE_AFTERNOON:
            double dosageAfternoon = convertStringToDouble(argumentValue);
            setDosageAfternoon(dosageAfternoon);
            break;
        case DOSAGE_EVENING:
            double dosageEvening = convertStringToDouble(argumentValue);
            setDosageEvening(dosageEvening);
            break;
        case EXPIRATION_DATE:
            LocalDate expiryDate = convertStringToLocalDate(argumentValue);
            setExpiryDate(expiryDate);
            break;
        case REMARKS:
            setRemarks(argumentValue);
            break;
        case REPEAT:
            setRepeat(argumentValue);
            break;
        case DAY_ADDED:
            int dayAdded = convertStringToInteger(argumentValue);
            setDayAdded(dayAdded);
            break;
        default:
            throw new MediTrackerException("Unexpected argument name: " + argumentName);
        }
    }
    // @@author

    @Override
    public String toString() {
        return getName() + " | " + getQuantity() + " | " + getExpiryDate() + " | " + getRemarks();
    }

    /**
     * Checks if Medication has dosage at given Period
     *
     * @param period Period to check for dosage
     * @return True if medication has dosage at Period, else false
     */
    public boolean hasDosage(Period period) {
        switch (period) {
        case MORNING:
            return getDosageMorning() > 0.0;
        case AFTERNOON:
            return getDosageAfternoon() > 0.0;
        case EVENING:
            return getDosageEvening() > 0.0;
        default:
            return false;
        }
    }

    /**
     * Checks if medication has any dosages
     *
     * @return True if medication has no dosages for morning, afternoon and evening, else false
     */
    public boolean hasNoDosages() {
        boolean hasNoDosageMorning = !hasDosage(Period.MORNING);
        boolean hasNoDosageAfternoon = !hasDosage(Period.AFTERNOON);
        boolean hasNoDosageEvening = !hasDosage(Period.EVENING);
        return hasNoDosageMorning && hasNoDosageAfternoon && hasNoDosageEvening;
    }

    /**
     * Replaces Medication info with the specified Medication object info
     *
     * @param medication Medication object values to replace with
     */
    public void revertMedication(Medication medication) throws MediTrackerException {
        try {
            setName(medication.getName());
            setRepeat(String.valueOf(medication.getRepeat()));
        } catch (MediTrackerException e) {
            // critical error as this should not happen
            throw new MediTrackerException("Critical issue occurred, unable to revert Medication.");
        }

        setQuantity(medication.getQuantity());
        setDosageMorning(medication.getDosageMorning());
        setDosageAfternoon(medication.getDosageAfternoon());
        setDosageEvening(medication.getDosageEvening());
        setExpiryDate(medication.getExpiryDate());
        setRemarks(medication.getRemarks());
        setDayAdded(medication.getDayAdded());
    }

    /**
     * Makes a deep copy of the specified Medication object
     *
     * @param medication Medication object to deep copy
     * @return A deep copy of the specified Medication object
     */
    public static Medication deepCopy(Medication medication) {
        Medication newMedication = new Medication();

        newMedication.setNameUnchecked(medication.getName());
        newMedication.setQuantity(medication.getQuantity());
        newMedication.setDosageMorning(medication.getDosageMorning());
        newMedication.setDosageAfternoon(medication.getDosageAfternoon());
        newMedication.setDosageEvening(medication.getDosageEvening());
        newMedication.setExpiryDate(medication.getExpiryDate());
        newMedication.setRemarks(medication.getRemarks());
        newMedication.setRepeatUnchecked(medication.getRepeat());
        newMedication.setDayAdded(medication.getDayAdded());

        return newMedication;
    }

    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o) {
            return true;
        }
        // null check
        if (o == null) {
            return false;
        }
        // type check and cast
        if (getClass() != o.getClass()) {
            return false;
        }
        Medication medication = (Medication) o;
        // field comparison
        return Objects.equals(getName(), medication.getName())
                && Objects.equals(getQuantity(), medication.getQuantity())
                && Objects.equals(getDosageMorning(), medication.getDosageMorning())
                && Objects.equals(getDosageAfternoon(), medication.getDosageAfternoon())
                && Objects.equals(getDosageEvening(), medication.getDosageEvening())
                && Objects.equals(getExpiryDate(), medication.getExpiryDate())
                && Objects.equals(getRemarks(), medication.getRemarks())
                && Objects.equals(getRepeat(), medication.getRepeat())
                && Objects.equals(getDayAdded(), medication.getDayAdded());
    }

    /**
     * Converts a String to a double.
     *
     * @param doubleString The String object to be converted to a double type.
     * @return The value of type double.
     * @throws MediTrackerException If unable to parse string to double or not storable in JSON.
     */
    private static double convertStringToDouble(String doubleString) throws MediTrackerException {
        double value;
        try {
            value = Double.parseDouble(doubleString);
        } catch (NumberFormatException e) {
            throw new MediTrackerException("Unable to parse String '" + doubleString + "' into double.");
        } catch (NullPointerException e) {
            throw new MediTrackerException("Null Pointer passed for conversion to double.");
        }

        try {
            JSONObject.testValidity(value);
        } catch (JSONException e) {
            String errorContext = String.format(
                    "Provided value \"%s\" not supported as it is either too large or NaN.",
                    doubleString);
            throw new MediTrackerException(errorContext);
        }

        return value;
    }

    /**
     * Converts a String to an integer.
     *
     * @param integerString The String object to be converted to an integer type.
     * @return The value of type integer.
     * @throws MediTrackerException If unable to parse string to integer or not storable in JSON.
     */
    private static int convertStringToInteger(String integerString) throws MediTrackerException {
        int value;
        try {
            value = Integer.parseInt(integerString);
        } catch (NumberFormatException e) {
            throw new MediTrackerException("Unable to parse String '" + integerString + "' into integer.");
        } catch (NullPointerException e) {
            throw new MediTrackerException("Null Pointer passed for conversion to integer.");
        }

        try {
            JSONObject.testValidity(value);
        } catch (JSONException e) {
            String errorContext = String.format(
                    "Provided value \"%s\" not supported as it is either too large or NaN.",
                    integerString);
            throw new MediTrackerException(errorContext);
        }

        return value;
    }

    /**
     * Converts String to LocalDate
     * Also checks if the user input String is expired or is an invalid date format
     *
     * @param expiryDateString The String object to be converted to an LocalDate type in yyyy-MM-dd format
     * @throws MediTrackerException When the date entered is in wrong format or is already expired
     */
    private static LocalDate convertStringToLocalDate(String expiryDateString) throws MediTrackerException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedExpiryDate;
        LocalDate currentDate = MediTrackerTime.getCurrentDate();
        try {
            parsedExpiryDate = LocalDate.parse(expiryDateString, dateTimeFormatter);
            if (parsedExpiryDate.isBefore(currentDate) || parsedExpiryDate.equals(currentDate)) {
                throw new MediTrackerException("You are not allowed to enter expired medications!");
            }
        } catch (DateTimeParseException e) {
            throw new MediTrackerException("Please enter a valid expiry date in yyyy-MM-dd!");
        }
        return parsedExpiryDate;
    }
}
