package meditracker.command;

/**
 * The CommandName enum represents the names of commands supported by the application.
 */
public enum CommandName {
    UNKNOWN("unknown", ""),
    EXIT("exit", "Exits MediTracker."),
    HELP("help", "Lists all available commands and their description."),
    ADD("add", "Adds a medication to the medication manager."),
    VIEW("view", "Get information of a specific medication."),
    MODIFY("modify", "Modify medication information."),
    LIST("list", "Summary of medications for the day."),
    DELETE("delete", "Delete medication information."),
    SEARCH("search", "Access medicine database."),
    TAKE("take", "Record taking of medication."),
    UNTAKE("untake", "Record untaking of medication."),
    SAVE("save", "Saves the JSON file to the specified path."),
    LOAD("load", "Loads the JSON file from the specified path.");

    public final String value;
    public final String description;

    /**
     * Constructs a CommandName enum with the specified string value.
     *
     * @param value The string value associated with the command name.
     * @param description The string description associated with the command.
     */
    CommandName(String value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * Returns the CommandName enum based on the provided string label.
     *
     * @param label The string label representing a command name.
     * @return The CommandName enum corresponding to the label, or UNKNOWN if not found.
     */
    // @@author Baeldung
    // Reused from https://www.baeldung.com/java-enum-values
    // with minor modifications
    public static CommandName valueOfLabel(String label) {
        for (CommandName e : values()) {
            if (e.value.equals(label)) {
                return e;
            }
        }
        return CommandName.UNKNOWN;
    }
}
