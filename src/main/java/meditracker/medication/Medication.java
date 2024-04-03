package meditracker.medication;

/**
 * The Medication class represents a medication.
 * It stores information about the medication such as name, quantity, dosage, expiry date, intake frequency, remarks,
 * and whether it has been taken.
 */
public class Medication {

    // Medication attributes are intentionally declared String. To be modified later.
    private String name;
    private Double quantity;
    private Double dosageMorning;
    private Double dosageAfternoon;
    private Double dosageEvening;
    private String expiryDate;
    private String remarks;
    private int repeat;
    private int dayAdded;

    /**
     * Constructs a new Medication object with default placeholder values.
     * Used by MedicationManager to populate medication data from the save file.
     */
    public Medication() {
        final double placeholderValue = -1.0;

        setName("");
        setQuantity(placeholderValue);
        setDosageMorning(placeholderValue);
        setDosageAfternoon(placeholderValue);
        setDosageEvening(placeholderValue);
        setExpiryDate("");
        setRemarks("");
        setRepeat((int) placeholderValue);
        setDayAdded((int) placeholderValue);
    }

    /**
     * Constructs a Medication object with the specified information.
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
                      String expiryDate, String remarks, int repeat, int dayAdded) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getDosageMorning() {
        return dosageMorning;
    }

    public void setDosageMorning(Double dosageMorning) {
        this.dosageMorning = dosageMorning;
    }

    public Double getDosageAfternoon() {
        return dosageAfternoon;
    }

    public void setDosageAfternoon(Double dosageAfternoon) {
        this.dosageAfternoon = dosageAfternoon;
    }

    public Double getDosageEvening() {
        return dosageEvening;
    }

    public void setDosageEvening(Double dosageEvening) {
        this.dosageEvening = dosageEvening;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getDayAdded() {
        return dayAdded;
    }

    public void setDayAdded(int dayAdded) {
        this.dayAdded = dayAdded;
    }

    @Override
    public String toString() {
        return getName() + " | " + getQuantity() +  " | " + getExpiryDate() + " | " + getRemarks();
    }

    /**
     * Replaces Medication info with the specified Medication object info
     *
     * @param medication Medication object values to replace with
     */
    public void revertMedication(Medication medication) {
        setName(medication.getName());
        setQuantity(medication.getQuantity());
        setDosageMorning(medication.getDosageMorning());
        setDosageAfternoon(medication.getDosageAfternoon());
        setDosageEvening(medication.getDosageEvening());
        setExpiryDate(medication.getExpiryDate());
        setRemarks(medication.getRemarks());
        setRepeat(medication.getRepeat());
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

        newMedication.setName(medication.getName());
        newMedication.setQuantity(medication.getQuantity());
        newMedication.setDosageMorning(medication.getDosageMorning());
        newMedication.setDosageAfternoon(medication.getDosageAfternoon());
        newMedication.setDosageEvening(medication.getDosageEvening());
        newMedication.setExpiryDate(medication.getExpiryDate());
        newMedication.setRemarks(medication.getRemarks());
        newMedication.setRepeat(medication.getRepeat());
        newMedication.setDayAdded(medication.getDayAdded());

        return newMedication;
    }
}
