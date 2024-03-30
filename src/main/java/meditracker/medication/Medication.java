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
    private Double dosage;
    private Double dosageMorning;
    private Double dosageAfternoon;
    private Double dosageEvening;
    private String expiryDate;
    private String intakeFreq;
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
        setDosage(placeholderValue);
        setDosageMorning(placeholderValue);
        setDosageAfternoon(placeholderValue);
        setDosageEvening(placeholderValue);
        setExpiryDate("");
        setIntakeFreq("");
        setRemarks("");
        setRepeat((int) placeholderValue);
        setDayAdded((int) placeholderValue);
    }

    /**
     * Constructs a Medication object with the specified information.
     * @param name The name of the medication.
     * @param quantity The quantity of the medication.
     * @param dosage The dosage of the medication.
     * @param dosageMorning The morning dosage of the medication.
     * @param dosageAfternoon The afternoon dosage of the medication.
     * @param dosageEvening The evening dosage of the medication.
     * @param expiryDate The expiry date of the medication.
     * @param intakeFreq The intake frequency of the medication in a day.
     * @param remarks Any remarks or notes about the medication.
     * @param repeat The repeat frequency of the medication.
     */
    public Medication(String name, Double quantity, Double dosage, Double dosageMorning, Double dosageAfternoon,
                      Double dosageEvening, String expiryDate,
                      String intakeFreq, String remarks, int repeat, int dayAdded) {
        this.name = name;
        this.quantity = quantity;
        this.dosage = dosage;
        this.dosageMorning = dosageMorning;
        this.dosageAfternoon = dosageAfternoon;
        this.dosageEvening = dosageEvening;
        this.expiryDate = expiryDate;
        this.intakeFreq = intakeFreq;
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

    public Double getDosage() {
        return dosage;
    }

    public void setDosage(Double dosage) {
        this.dosage = dosage;
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

    public String getIntakeFreq() {
        return intakeFreq;
    }

    public void setIntakeFreq(String intakeFreq) {
        this.intakeFreq = intakeFreq;
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
}
