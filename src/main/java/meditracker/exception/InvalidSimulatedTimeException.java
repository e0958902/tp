package meditracker.exception;

/**
 * An exception class thrown when the input does not fulfil the format of the simulated time.
 */
public class InvalidSimulatedTimeException extends Exception {
    private String specificErrorMessage;

    public InvalidSimulatedTimeException(String specificErrorMessage) {
        this.specificErrorMessage = specificErrorMessage;
    }

    @Override
    public String getMessage() {
        String correctSyntaxMessage = "Please provide the simulated time in the following format: "
                + "YYYY-MM-DDTHH:MM:SSZ";
        String usageExampleMessage = "Examples: 2021-01-01T21:13:00Z, 2024-04-29T04:05:06Z";

        return specificErrorMessage + System.lineSeparator() + correctSyntaxMessage
                + System.lineSeparator() + usageExampleMessage;
    }
}
