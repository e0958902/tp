package meditracker.exception;

public class InsufficientQuantityException extends Exception {
    Double dosage;
    Double quantity;

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
