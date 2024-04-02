package meditracker.storage;

import meditracker.logging.MediLogger;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * A class that perform checks on the supplied file path.
 * This does not check for file permissions (to be created, read, accessed etc.).
 */
public class FilePathChecker {
    private static final Logger MEDILOGGER = MediLogger.getMediLogger();

    /**
     * Checks if the Path object contains illegal folder names.
     * They are generally targeted towards the Windows filesystem, but it also catches misuse of periods, potentially
     * as a way of path traversal.
     *
     * @param path The Path object to check for illegal names (and improper use of periods).
     * @return true if illegal names are found, false otherwise.
     */
    static boolean containsIllegalFolderNames(Path path) {
        Iterator<Path> splitPath = path.iterator();
        while (splitPath.hasNext()) {
            Path subpath = splitPath.next();
            String subpathString = subpath.toString().toLowerCase();
            // Path should not have multiple periods or colons
            if (subpathString.contains("..") || subpathString.contains(":")) {
                MEDILOGGER.warning("Sub-path contains .. or : which is not allowed.");
                return true;
            }

            if (subpathString.endsWith(".") || subpathString.endsWith(" ")) {
                MEDILOGGER.warning("Sub-path ends with '.' or (space) which is not allowed.");
                return true;
            }

            // Credit: https://stackoverflow.com/a/31976060
            switch (subpathString) {
            case "con": // fallthrough
            case "prn": // fallthrough
            case "aux": // fallthrough
            case "nul": // fallthrough
            case "com1": // fallthrough
            case "com2": // fallthrough
            case "com3": // fallthrough
            case "com4": // fallthrough
            case "com5": // fallthrough
            case "com6": // fallthrough
            case "com7": // fallthrough
            case "com8": // fallthrough
            case "com9": // fallthrough
            case "lpt1": // fallthrough
            case "lpt2": // fallthrough
            case "lpt3": // fallthrough
            case "lpt4": // fallthrough
            case "lpt5": // fallthrough
            case "lpt6": // fallthrough
            case "lpt7": // fallthrough
            case "lpt8": // fallthrough
            case "lpt9": // fallthrough
            case ".": // Path should not have a lone period.
                MEDILOGGER.warning("Path contains potentially invalid folder: " + subpathString);
                return true;
            default:
                continue;
            }
        }
        return false;
    }


    /**
     * Checks if the Path object has a valid root.
     * This applies to windows filesystem where there are multiple local drives with different drive letters.
     * Relative paths should always return true.
     *
     * @param path The path object to be checked for the presence of the valid root.
     * @return true if it has a valid root, false otherwise.
     */
    private static boolean containsValidRoot(Path path) {
        Path absolutePath = path.toAbsolutePath();
        Path root = absolutePath.getRoot();

        if (root == null) {
            assert false;
            return false;
        }

        if (Files.exists(root)) {
            return true;
        } else {
            MEDILOGGER.warning("File Root " + root + " does not exist on the filesystem.");
            return false;
        }
    }

    /**
     * Checks the String input if it contains potentially illegal characters as a valid filepath.
     * The Path object does not contain illegal characters as the conversion from String to Path would have thrown
     * an `InvalidPathException` error.
     *
     * @param inputToCheck The String input to check for illegal characters
     * @return true if an illegal character is found, false otherwise.
     */
    public static boolean containsIllegalCharacters (String inputToCheck) {
        // Credit: https://stackoverflow.com/a/31976060
        String[] illegalCharacters = {"<", ">", "\"", "'", "|", "?", "*"};

        for (String illegalChar : illegalCharacters) {
            if (inputToCheck.contains(illegalChar)) {
                MEDILOGGER.warning("String contains potentially illegal character: " + illegalChar);
                return true;
            }
        }
        return false;
    }

    /**
     * Runs a series of checks on the Path object to ensure that the naming of the filesystem path is sound.
     *
     * @param path The Path object.
     * @return true if the Path object is valid for the filesystem, false otherwise.
     */
    public static boolean isValidFullPath(Path path) {
        if (path == null) {
            return false;
        }

        if (!containsValidRoot(path)) {
            return false;
        }

        if (containsIllegalFolderNames(path)) {
            return false;
        }

        Path fileName = FileReaderWriter.getFullPathComponent(path,false);
        if (fileName == null) {
            MEDILOGGER.warning("There is no valid file name supplied.");
            return false;
        }

        String fileNameString = fileName.toString().toLowerCase();
        if (!fileNameString.endsWith(".json")) {
            MEDILOGGER.warning("File does not end in .json");
            return false;
        }

        return true;
    }

    /**
     * Validates the path input and return the Path object if successfully validated.
     *
     * @param fileLocationArgument The argument specifying the location of the file (as String)
     * @return The Path object corresponding to the argument for the save file if it passes validation checks.
     *     null otherwise.
     */
    public static Path validateUserPathArgument(String fileLocationArgument) {

        assert (fileLocationArgument != null);
        if (fileLocationArgument == null) {
            return null;
        }

        boolean hasIllegalCharacters = FilePathChecker.containsIllegalCharacters(fileLocationArgument);
        if (hasIllegalCharacters) {
            System.out.println("The supplied input contains potentially illegal characters. Please ensure that "
                    + "the supplied path does not have illegal character");
            return null;
        }

        Path pathOfSaveFile;
        try {
            pathOfSaveFile = Path.of(fileLocationArgument);
        } catch (InvalidPathException e) {
            MEDILOGGER.severe(e.getMessage());
            System.out.println("Unable to convert input into Path object. Data is not saved.");
            return null;
        }

        boolean isValidFilePath = FilePathChecker.isValidFullPath(pathOfSaveFile);
        if (!isValidFilePath) {
            System.out.println("Path contains invalid folder names or missing valid file extension (.json).");
            System.out.println("Please ensure the path contains valid folder names and ends with .json");
            return null;
        }

        return pathOfSaveFile;
    }
}
