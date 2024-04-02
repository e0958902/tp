package meditracker.argument;

import meditracker.command.AddCommand;
import meditracker.command.CommandName;
import meditracker.command.DeleteCommand;
import meditracker.command.ListCommand;
import meditracker.command.ModifyCommand;
import meditracker.command.TakeCommand;
import meditracker.command.UntakeCommand;
import meditracker.command.SaveCommand;

import java.util.List;

/**
 * ArgumentHelper class to handle generating the help messages,
 * consisting of the general command usage and a breakdown of the
 * various command argument options.
 */
public class ArgumentHelper {

    /**
     * Gets the help message for the corresponding Command
     *
     * @param commandName Enum of the Command that is getting the help message
     * @return The help message for that Command
     */
    public static String getHelpMessage(CommandName commandName) {
        switch (commandName) {
        case ADD:
            return AddCommand.HELP_MESSAGE;
        case MODIFY:
            return ModifyCommand.HELP_MESSAGE;
        case LIST:
            return ListCommand.HELP_MESSAGE;
        case DELETE:
            return DeleteCommand.HELP_MESSAGE;
        case TAKE:
            return TakeCommand.HELP_MESSAGE;
        case UNTAKE:
            return UntakeCommand.HELP_MESSAGE;
        case SAVE:
            return SaveCommand.HELP_MESSAGE;
        case SEARCH: // fall through
        case EXIT: // fall through
        case UNKNOWN: // fall through
        default:
            throw new IllegalStateException("No available help message for " + commandName);
        }
    }

    /**
     * Generates the help message for the corresponding Command
     *
     * @param commandName Enum of the Command that is generating the help message
     * @param argumentList Corresponding list of Argument for that Command
     * @return The help message generated for the corresponding Command
     */
    public static String getHelpMessage(CommandName commandName, ArgumentList argumentList) {
        StringBuilder message = new StringBuilder();
        String usage = getUsage(commandName, argumentList);
        String options = getOptions(argumentList);

        message.append(usage).append(System.lineSeparator()).append(options);
        return message.toString();
    }

    /**
     * Generates the usage for the corresponding Command
     *
     * @param commandName Enum of the Command that is generating the help message
     * @param argumentList Corresponding list of Argument for that Command
     * @return The usage for the corresponding Command
     */
    private static String getUsage(CommandName commandName, ArgumentList argumentList) {
        StringBuilder usageString = new StringBuilder("Usage:");
        usageString.append(System.lineSeparator());

        String command = commandName.name().toLowerCase();
        usageString.append("\t").append(command);

        List<Argument> arguments = argumentList.getArguments();
        for (Argument argument : arguments) {
            boolean isOptional = argument.isOptional();
            String argumentFormat = ArgumentHelper.getArgumentFormat(argument);

            if (isOptional) {
                argumentFormat = String.format(" [%s]", argumentFormat);
            } else {
                argumentFormat = String.format(" (%s)", argumentFormat);
            }
            usageString.append(argumentFormat);
        }

        return usageString.toString();
    }

    /**
     * Generates the command options for the corresponding Command
     *
     * @param argumentList Corresponding list of Argument for that Command
     * @return The command options for the corresponding Command
     */
    private static String getOptions(ArgumentList argumentList) {
        StringBuilder optionsString = new StringBuilder("Options:");
        List<Argument> arguments = argumentList.getArguments();

        int maxWidth = arguments.stream()
                .map(ArgumentHelper::getArgumentFormat)
                .map(String::length)
                .max(Integer::compareTo).get() + 5;

        for (Argument argument : arguments) {
            String argumentFormat = ArgumentHelper.getArgumentFormat(argument);
            String helpMessage = argument.getHelp();

            String formatSpecifier = "\t%-" + maxWidth + "s%s";
            String argumentHelpMessage = String.format(formatSpecifier, argumentFormat, helpMessage);
            optionsString.append(System.lineSeparator());
            optionsString.append(argumentHelpMessage);
        }

        return optionsString.toString();
    }

    /**
     * Formats the argument depending on whether it has value or not
     *
     * @param argument Argument to format
     * @return The string in the format of flag (and name)
     */
    private static String getArgumentFormat(Argument argument) {
        String flag = argument.getFlag();
        String name = argument.getName().value;

        boolean hasValue = argument.hasValue();
        if (hasValue) {
            return String.format("%s %s", flag, name);
        } else {
            return String.format("%s", flag);
        }
    }
}
