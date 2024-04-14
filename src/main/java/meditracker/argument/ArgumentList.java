package meditracker.argument;

import meditracker.exception.ArgumentException;
import meditracker.exception.HelpInvokedException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ArgumentList class for managing a list of Argument
 * Calls on ArgumentParser when parse method is invoked
 * @see ArgumentParser
 */
public class ArgumentList {
    private final List<Argument> arguments;

    /**
     * Constructs ArgumentList to take in variable length of Argument
     * Assertion test is used to check no flag collision for the
     * arguments specified
     *
     * @param arguments Arguments to be included in the list
     */
    public ArgumentList(Argument... arguments) {
        List<Argument> newArguments = new ArrayList<>(List.of(arguments));
        newArguments.add(new HelpArgument());
        this.arguments = newArguments;

        // assertion test: check for flag collisions
        Set<String> flags = new HashSet<>();
        for (Argument argument: arguments) {
            String flag = argument.getFlag();
            assert !flags.contains(flag);
            flags.add(flag);
        }
    }

    /**
     * Parses user raw input arguments into ArgumentName and
     * corresponding argument value
     *
     * @param rawInput Raw input to be parsed
     * @return A map of argument name as key and the corresponding value
     * @throws HelpInvokedException When help argument is used or help message needed
     * @throws ArgumentException Argument flag specified not found,
     *              or when argument requires value but no value specified,
     *              or when unknown argument flags found in user input,
     *              or when duplicate argument flag found
     * @see ArgumentParser
     */
    public Map<ArgumentName, String> parse(String rawInput) throws HelpInvokedException, ArgumentException {
        ArgumentParser argumentParser = new ArgumentParser(this, rawInput);

        boolean hasCalledForHelp = argumentParser.parsedArguments.containsKey(ArgumentName.HELP);
        if (hasCalledForHelp) {
            throw new HelpInvokedException();
        }

        argumentParser.checkForMissingRequiredArguments(); // throws ArgumentException
        return argumentParser.parsedArguments;
    }

    public List<Argument> getArguments() {
        return arguments;
    }
}
