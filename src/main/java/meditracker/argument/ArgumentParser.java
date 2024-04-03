package meditracker.argument;

import meditracker.exception.ArgumentNoValueException;
import meditracker.exception.ArgumentNotFoundException;
import meditracker.exception.DuplicateArgumentFoundException;
import meditracker.exception.HelpInvokedException;
import meditracker.exception.UnknownArgumentFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * ArgumentParser class to handle parsing of user input
 * All arguments have to be specified with a flag.
 */
class ArgumentParser {
    protected final Map<ArgumentName, String> parsedArguments = new HashMap<>();
    private final ArgumentList argumentList;

    /**
     * Constructs ArgumentParser that parses raw input into corresponding key value pairs
     *
     * @param argumentList List of argument
     * @param rawInput Raw input to be parsed
     * @throws DuplicateArgumentFoundException Duplicate argument flag found
     * @throws ArgumentNoValueException When argument requires value but no value specified
     * @throws UnknownArgumentFoundException When unknown argument flags found in user input
     * @throws HelpInvokedException When help argument is used or help message needed
     */
    public ArgumentParser(ArgumentList argumentList, String rawInput)
            throws DuplicateArgumentFoundException, ArgumentNoValueException, UnknownArgumentFoundException,
            HelpInvokedException {
        this.argumentList = argumentList;

        List<String> rawInputSplit = List.of(rawInput.split(" "));
        checkForUnknownArguments(rawInputSplit);

        SortedMap<Integer, Argument> indexes = getArgumentIndexes(rawInputSplit);

        if (indexes.isEmpty()) {
            throw new HelpInvokedException();
        }
        getArgumentValues(indexes, rawInputSplit);
    }

    /**
     * Checks for missing required arguments
     *
     * @throws ArgumentNotFoundException Argument flag specified not found
     */
    public void checkForMissingRequiredArguments() throws ArgumentNotFoundException {
        for (Argument argument: argumentList.getArguments()) {
            String flag = argument.getFlag();
            boolean isFoundInParsedArgs = parsedArguments.containsKey(argument.getName());
            boolean isRequired = !argument.isOptional();
            boolean isMissing = isRequired && !isFoundInParsedArgs;

            if (isMissing) {
                // arg keyword not found in additional input
                String errorContext = String.format("Missing \"%s\" argument", flag);
                throw new ArgumentNotFoundException(errorContext);
            }
        }
    }

    // @@author wenenhoe-reused
    // Reused from https://github.com/wenenhoe/ip with minor modifications
    /**
     * Checks if unknown argument flags are found in user input
     *
     * @param rawInputSplit List of raw input split by spaces
     * @throws UnknownArgumentFoundException When unknown argument flags found in user input
     */
    public void checkForUnknownArguments(List<String> rawInputSplit) throws UnknownArgumentFoundException {
        List<String> argumentFlags = argumentList.getArguments().stream()
                .map(Argument::getFlag)
                .collect(Collectors.toList());

        /*
         * regex pattern to find all flags with the form of `-*`
         * where `*` represents any number of alphabetic characters
         */
        String pattern = "^-.+$";
        List<String> unknownFlags = rawInputSplit.stream()
                .filter((input) -> input.matches(pattern) && !argumentFlags.contains(input))
                .collect(Collectors.toList());

        if (!unknownFlags.isEmpty()) {
            String unknownFlagsString = String.join(" ", unknownFlags);
            String errorContext = String.format("Unknown argument flags found: %s", unknownFlagsString);
            throw new UnknownArgumentFoundException(errorContext);
        }
    }
    // @@author

    /**
     * Obtains argument value using start and end index of the raw input list
     *
     * @param rawInputSplit List of raw input split by spaces
     * @param startIndex Start index in rawInputSplit of argument value
     * @param endIndex End index in rawInputSplit of argument value
     * @return Corresponding argument value, joined with spaces
     */
    private static String getArgumentValue(List<String> rawInputSplit, int startIndex, int endIndex) {
        List<String> argContentList = rawInputSplit.subList(startIndex, endIndex);
        return String.join(" ", argContentList).strip();
    }

    /**
     * Obtains the argument index from the raw input list
     *
     * @param rawInputSplit List of raw input split by spaces
     * @param flag Argument flag to index
     * @return Index of the argument flag
     * @throws DuplicateArgumentFoundException Duplicate argument flag found
     */
    private static int getArgumentIndex(List<String> rawInputSplit, String flag)
            throws DuplicateArgumentFoundException {
        int firstFlagIndex = rawInputSplit.indexOf(flag);
        int lastFlagIndex = rawInputSplit.lastIndexOf(flag);

        if (firstFlagIndex != lastFlagIndex) {
            String errorContext = String.format("Duplicate \"%s\" argument found", flag);
            throw new DuplicateArgumentFoundException(errorContext);
        }
        return firstFlagIndex;
    }

    // @@author wenenhoe-reused
    // Reused from https://github.com/wenenhoe/ip with minor modifications
    /**
     * Sorts a list of argument flags and their corresponding indexes
     *
     * @param rawInputSplit List of raw input split by spaces
     * @return A sorted map of arguments and their corresponding indexes
     * @throws DuplicateArgumentFoundException Duplicate argument flag found
     */
    private SortedMap<Integer, Argument> getArgumentIndexes(List<String> rawInputSplit)
            throws DuplicateArgumentFoundException {
        SortedMap<Integer, Argument> indexes = new TreeMap<>();
        for (Argument argument: argumentList.getArguments()) {
            String flag = argument.getFlag();
            int flagIndex = ArgumentParser.getArgumentIndex(rawInputSplit, flag);

            boolean isNotFound = flagIndex == -1;
            if (!isNotFound) {
                indexes.put(flagIndex, argument);
            }
        }
        return indexes;
    }
    // @@author

    // @@author wenenhoe-reused
    // Reused from https://github.com/wenenhoe/ip with modifications to support
    // arguments without corresponding value
    /**
     * Obtains a map of argument flags and their corresponding value, using a sorted ordering
     * of the argument flags indexes.
     *
     * @param indexes A sorted map of arguments and their corresponding indexes
     * @param rawInputSplit List of raw input split by spaces
     * @throws ArgumentNoValueException When argument requires value but no value specified
     */
    private void getArgumentValues(SortedMap<Integer, Argument> indexes, List<String> rawInputSplit)
            throws ArgumentNoValueException {
        Argument argument = indexes.get(indexes.firstKey());
        ArgumentName argKey = argument.getName();
        boolean hasValue = argument.hasValue();
        int startIndex = indexes.firstKey() + 1; // position after argument flag
        int endIndex;

        boolean isSkipFirst = false;
        for (Map.Entry<Integer, Argument> index: indexes.entrySet()) {
            if (!isSkipFirst) {
                isSkipFirst = true; // Skips first map entry
                continue;
            }

            endIndex = index.getKey();
            String argValue = ArgumentParser.getArgumentValue(rawInputSplit, startIndex, endIndex);
            if (hasValue && argValue.isEmpty()) {
                String errorContext = String.format("No value found for argument \"%s\"", argument.getFlag());
                throw new ArgumentNoValueException(errorContext);
            }
            parsedArguments.put(argKey, argValue);

            argument = index.getValue();
            argKey = argument.getName();
            hasValue = argument.hasValue();
            startIndex = endIndex + 1; // position after argument flag
        }

        endIndex = rawInputSplit.size();
        String argValue = ArgumentParser.getArgumentValue(rawInputSplit, startIndex, endIndex);
        if (hasValue && argValue.isEmpty()) {
            String errorContext = String.format("No value found for argument \"%s\"", argument.getFlag());
            throw new ArgumentNoValueException(errorContext);
        }
        parsedArguments.put(argKey, argValue);
    }
    // @@author
}
