package meditracker.storage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * A class that perform checks on the supplied file path.
 * This does not check for file permissions (to be created, read, accessed etc.).
 */
public class FilePathChecker {

    /**
     * Checks if the Path object contains illegal folder names.
     * They are generally targeted towards the Windows filesystem, but it also catches misuse of periods, potentially
     * as a way of path traversal.
     *
     * @param path The Path object to check for illegal names (and improper use of periods).
     * @return true if illegal names are found, false otherwise.
     */
    private static boolean containsIllegalFolderNames(Path path) {
        Iterator<Path> splitPath = path.iterator();
        while (splitPath.hasNext()) {
            Path subpath = splitPath.next();
            String subpathString = subpath.toString().toLowerCase();

            if (subpathString.contains("..")) { // Path should not have multiple periods
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
        String[] illegalCharacters = {"<", ">", ":", "\"", "'", "|", "?", "*"};

        for (String illegalChar : illegalCharacters) {
            if (inputToCheck.contains(illegalChar)) {
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
            return false;
        }

        String fileNameString = fileName.toString().toLowerCase();
        if (!fileNameString.endsWith(".json")) {
            return false;
        }

        return true;
    }
}
