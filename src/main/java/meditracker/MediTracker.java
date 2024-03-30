package meditracker;

import meditracker.argument.ArgumentHelper;
import meditracker.command.Command;
import meditracker.command.CommandName;
import meditracker.command.CommandParser;
import meditracker.dailymedication.DailyMedicationManager;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.CommandNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.FileReadWriteException;
import meditracker.exception.HelpInvokedException;
import meditracker.logging.MediLogger;
import meditracker.medication.MedicationManager;
import meditracker.storage.FileReaderWriter;
import meditracker.ui.Ui;

import java.util.List;

/**
 * The main class for the MediTracker application.
 * It initializes the user interface and runs the application loop.
 */
public class MediTracker {

    private MedicationManager medicationManager;

    /**
     * Constructs a new MediTracker object and initializes both medicationManager and
     * dailyMedicationManager.
     */
    public MediTracker() {
        DailyMedicationManager.createDailyMedicationManager();
    }

    /**
     * Constructs a new MediTracker object with data from save file for DailyMedicationManager
     *
     * @param dailyMedicationList Daily medication
     */
    public MediTracker(List<String> dailyMedicationList) {
        DailyMedicationManager.importDailyMedicationManager(dailyMedicationList);
    }

    /**
     * Runs the MediTracker application.
     * This method displays a welcome message, reads user commands, and processes them until the user exits the
     * application.
     *
     * @throws FileReadWriteException when there is error to write into text file.
     */
    public void run() throws FileReadWriteException {
        //@@author nickczh-reused
        //Reused from https://github.com/nickczh/ip
        //with minor modifications
        FileReaderWriter.loadMediTrackerData(medicationManager);
        Ui.showWelcomeMessage();
        boolean isExit = false;
        while (!isExit) {
            Ui.showLine();
            String fullCommand = Ui.readCommand();

            CommandParser commandParser;
            try {
                commandParser = new CommandParser(fullCommand);
            } catch (CommandNotFoundException e) {
                // Just pressing enter into console, skip processing
                continue;
            }
            CommandName commandName = commandParser.getCommandName();

            Command command;
            try {
                command = commandParser.getCommand();
            } catch (ArgumentNotFoundException | DuplicateArgumentFoundException | CommandNotFoundException e) {
                System.out.println(e.getMessage());
                continue;
            } catch (HelpInvokedException e) {
                String helpMessage = ArgumentHelper.getHelpMessage(commandName);
                System.out.println(helpMessage);
                continue;
            }

            try {
                command.execute();
            } catch (FileReadWriteException e) {
                throw new FileReadWriteException("IO Error: Unable to write to text File");
            }
            isExit = command.isExit();
        }
    }

    /**
     * Starts the MediTracker application.
     * It creates a new MediTracker object and calls its run() method.
     *
     * @param args Command-line arguments.
     * @throws FileReadWriteException when there is error to write into text file.
     */
    public static void main(String[] args) throws FileReadWriteException {
        MediLogger.initialiseLogger();

        List<String> dailyMedicationList = FileReaderWriter.loadDailyMedicationData();
        if (dailyMedicationList == null) {
            new MediTracker().run();
        } else {
            new MediTracker(dailyMedicationList).run();
        }
    }
}
