package meditracker.exception;

/**
 * Exception thrown when Medication object has insufficient quantity available to take medication
 */
public class InsufficientQuantityException extends Exception {
    private final Double dosage;
    private final Double quantity;

    /**
     * Constructs a InsufficientQuantityException with the specified dosage and total quantity
     *
     * @param dosage Dosage of medication
     * @param quantity Total quantity of medication
     */
    public InsufficientQuantityException(double dosage, double quantity) {
        this.dosage = dosage;
        this.quantity = quantity;
    }

    @Override
    public String getMessage() {
        return String.format(
                "Insufficient quantity. Dosage Required -> %.1f, Quantity Available -> %.1f",
                dosage,
                quantity);
    }
}
