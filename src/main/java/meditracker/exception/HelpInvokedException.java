package meditracker.exception;

import meditracker.argument.Argument;
import meditracker.argument.ArgumentList;
import meditracker.command.CommandName;

import java.util.List;

public class HelpInvokedException extends Exception {
    private final ArgumentList helpInvoker;

    public HelpInvokedException(ArgumentList argumentList) {
        helpInvoker = argumentList;
    }

    public String getMessage(CommandName commandName) {
        StringBuilder message = new StringBuilder();
        String usage = getUsage(commandName);
        String options = getOptions();

        message.append(usage).append(System.lineSeparator()).append(options);
        return message.toString();
    }

    private String getUsage(CommandName commandName) {
        StringBuilder usageString = new StringBuilder("Usage:");
        usageString.append(System.lineSeparator());

        String command = commandName.name().toLowerCase();
        usageString.append("\t").append(command);

        List<Argument> arguments = helpInvoker.getArguments();
        for (Argument argument : arguments) {
            boolean isOptional = argument.isOptional();
            String argumentFormat = HelpInvokedException.getArgumentFormat(argument);

            if (isOptional) {
                argumentFormat = String.format(" [%s]", argumentFormat);
            } else {
                argumentFormat = String.format(" (%s)", argumentFormat);
            }
            usageString.append(argumentFormat);
        }

        return usageString.toString();
    }

    private String getOptions() {
        StringBuilder optionsString = new StringBuilder("Options:");
        List<Argument> arguments = helpInvoker.getArguments();

        int maxWidth = arguments.stream()
                .map(HelpInvokedException::getArgumentFormat)
                .map(String::length)
                .max(Integer::compareTo).get() + 5;

        for (Argument argument : arguments) {
            String argumentFormat = HelpInvokedException.getArgumentFormat(argument);
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
