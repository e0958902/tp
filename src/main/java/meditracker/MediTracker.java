package meditracker;

import meditracker.command.Command;
import meditracker.command.CommandName;
import meditracker.command.CommandParser;
import meditracker.exception.ArgumentException;
import meditracker.exception.CommandNotFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.InvalidSimulatedTimeException;
import meditracker.logging.MediLogger;
import meditracker.storage.FileReaderWriter;
import meditracker.time.MediTrackerTime;
import meditracker.ui.Ui;

/**
 * The main class for the MediTracker application.
 * It initializes the user interface and runs the application loop.
 */
public class MediTracker {
    /**
     * Runs the MediTracker application.
     * This method displays a welcome message, reads user commands, and processes them until the user exits the
     * application.
     */
    public static void run() {
        //@@author nickczh-reused
        //Reused from https://github.com/nickczh/ip
        //with minor modifications
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
            } catch (CommandNotFoundException | ArgumentException e) {
                Ui.showErrorMessage(e);
                continue;
            } catch (HelpInvokedException e) {
                Ui.showHelpMessage(commandName);
                continue;
            }

            command.execute();
            isExit = command.isExit();
        }
    }

    /**
     * Starts the MediTracker application.
     * It creates a new MediTracker object and calls its run() method.
     *
     * @param args Command-line arguments for the program.
     */
    public static void main(String[] args) {
        try {
            MediTrackerTime.setUpSimulatedTime(args);
        } catch (InvalidSimulatedTimeException e) {
            System.out.println(e.getMessage());
            return;
        }
        MediLogger.initialiseMediLogger();

        FileReaderWriter.loadMediTrackerData(null);
        run();
    }
}
