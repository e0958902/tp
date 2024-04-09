package meditracker.command;

import meditracker.ui.Ui;

public class HelpCommand extends Command {

    @Override
    public void execute() {
        System.out.print(System.lineSeparator());
        Ui.showLine();
        System.out.println("Here are the commands you can use with MediTracker:" +
                System.lineSeparator());
        getCommandNamesAndDescription();
        System.out.println(System.lineSeparator());
        System.out.println("For more details on each command, simply type in the command name.");
    }

    public void getCommandNamesAndDescription() {
        for (CommandName commandName : CommandName.values()) {
            if (commandName.equals(CommandName.UNKNOWN)) {
                continue;
            }
            System.out.println(commandName.value + ": " + commandName.description);
        }
    }

    public HelpCommand() {

    }
}
