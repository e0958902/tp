package meditracker.library;

/**
 * Represents a search result containing details about a medication.
 */
public class SearchResult {
    private final String name;
    private final String illness;
    private final String sideEffects;

    /**
     * Constructs a SearchResult object with the specified medication details.
     *
     * @param name The name of the medication.
     * @param illness The illness(es) the medication is used to treat.
     * @param sideEffects The possible side effects of the medication.
     */
    public SearchResult(String name, String illness, String sideEffects) {
        this.name = name;
        this.illness = illness;
        this.sideEffects = sideEffects;
    }

    /**
     * Returns a string containing all the details of the medication.
     *
     * @return A string in the format "name | illness | sideEffects".
     */
    public String getAllMedicationDetails() {
        return this.name + "|" + this.illness + "|" + this.sideEffects;
    }

    /**
     * Gets the name of the medication.
     *
     * @return The name of the medication.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the illness(es) the medication is used to treat.
     *
     * @return The illness(es) the medication is used to treat.
     */
    public String getIllness() {
        return this.illness;
    }

    /**
     * Gets the possible side effects of the medication.
     *
     * @return The possible side effects of the medication.
     */
    public String getSideEffects() {
        return this.sideEffects;
    }

    /**
     * Overrides the toString method for the SearchResult class.
     *
     * @return A string representation of the SearchResult object,
     *         including the name of the medication, the illness it treats,
     *         and its possible side effects.
     */
    @Override
    public String toString() {
        return getName() + "; Treats: " + getIllness() + "; May cause: " + getSideEffects();
    }
}
