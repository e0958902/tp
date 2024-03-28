package meditracker.argument;

import meditracker.command.AddCommand;
import meditracker.command.CommandName;
import meditracker.command.DeleteCommand;
import meditracker.command.ListCommand;
import meditracker.command.ModifyCommand;
import meditracker.command.TakeCommand;
import meditracker.command.UntakeCommand;

import java.util.List;

public class ArgumentHelper {
    public static String getHelpMessage(CommandName commandName) {
        switch (commandName) {
        case ADD:
            return AddCommand.helpMessage;
        case MODIFY:
            return ModifyCommand.helpMessage;
        case LIST:
            return ListCommand.helpMessage;
        case DELETE:
            return DeleteCommand.helpMessage;
        case TAKE:
            return TakeCommand.helpMessage;
        case UNTAKE:
            return UntakeCommand.helpMessage;
        case SEARCH: // fall through
        case EXIT: // fall through
        case UNKNOWN: // fall through
        default:
            throw new IllegalStateException("No available help message for " + commandName);
        }
    }

    public static String getHelpMessage(CommandName commandName, ArgumentList argumentList) {
        StringBuilder message = new StringBuilder();
        String usage = getUsage(commandName, argumentList);
        String options = getOptions(argumentList);

        message.append(usage).append(System.lineSeparator()).append(options);
        return message.toString();
    }

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
