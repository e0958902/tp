package meditracker.storage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

public class FilePathCheckerTest {
    @Test
    public void isValidJsonFullPath_validFullPaths_returnedTrue() {
        boolean result;

        // OS check adapted from: https://www.baeldung.com/java-detect-os
        if (System.getProperty("os.name").startsWith("Windows")) {
            Path validRootDirectory = Path.of("C:/noFolderExists/noFileExists.jsON");
            result = FilePathChecker.isValidFullPath(validRootDirectory);
            assertTrue(result);
        }

        Path emptyFolderName = Path.of("data/aa/////b/c//json.jSoN");
        result = FilePathChecker.isValidFullPath(emptyFolderName);
        assertTrue(result);

        Path validSubDirectory1 = Path.of("/common/contains/com/json.JSON");
        result = FilePathChecker.isValidFullPath(validSubDirectory1);
        assertTrue(result);

        Path validSubdirectory2 = Path.of("\\prnter\\auxiliary\\null\\json.json");
        result = FilePathChecker.isValidFullPath(validSubdirectory2);
        assertTrue(result);
    }

    @Test
    public void isValidJsonFullPath_invalidExtensions_returnedFalse() {
        boolean result;

        result = FilePathChecker.isValidFullPath(null);
        assertFalse(result);

        Path emptyPath = Path.of("");
        result = FilePathChecker.isValidFullPath(emptyPath);
        assertFalse(result);

        Path rootPath = Path.of("/");
        result = FilePathChecker.isValidFullPath(rootPath);
        assertFalse(result);

        Path emptyExtension = Path.of("data/outfile");
        result = FilePathChecker.isValidFullPath(emptyExtension);
        assertFalse(result);

        Path invalidExtension = Path.of("data/notJsonExtension.exe");
        result = FilePathChecker.isValidFullPath(invalidExtension);
        assertFalse(result);
    }

    @Test
    public void isValidJsonFullPath_invalidFolders_returnedFalse() {
        boolean result;

        // OS check adapted from: https://www.baeldung.com/java-detect-os
        if (System.getProperty("os.name").startsWith("Windows")) {
            Path invalidRootDirectory = Path.of("M:/noFolderExists/noFileExists.json");
            result = FilePathChecker.isValidFullPath(invalidRootDirectory);
            assertFalse(result);
        }

        Path illegalFolderOne = Path.of("CoM1/out.json");
        result = FilePathChecker.isValidFullPath(illegalFolderOne);
        assertFalse(result);

        Path illegalFolderTwo = Path.of("newFolder/prn/out.json");
        result = FilePathChecker.isValidFullPath(illegalFolderTwo);
        assertFalse(result);

        Path traversalFolders = Path.of("../../testIllegal.json");
        result = FilePathChecker.isValidFullPath(traversalFolders);
        assertFalse(result);

        Path invalidSubDirectory = Path.of("/common/contains/./json.JSON");
        result = FilePathChecker.isValidFullPath(invalidSubDirectory);
        assertFalse(result);
    }

    @Test
    public void containsIllegalCharacters_invalidCharacters_returnedTrue() {
        boolean result;

        result = FilePathChecker.containsIllegalCharacters("/data/<out.json");
        assertTrue(result);

        result = FilePathChecker.containsIllegalCharacters("/data/out>.json");
        assertTrue(result);

        // The check for colon is done by `containsIllegalFolderNames` (for unix)
        // Furthermore, the `Path.of` will throw an `InvalidPathException` if it encounters the colon (for windows)
        result = FilePathChecker.containsIllegalCharacters("/data/:fold/out.json");
        assertFalse(result);
        try {
            Path path = Path.of("/data/:fold/out.json");
            result = FilePathChecker.containsIllegalFolderNames(path);
            assertTrue(result);
        } catch (InvalidPathException e) {
            assertTrue(true);
        }

        result = FilePathChecker.containsIllegalCharacters("/data/\"IllegalQuote\"/out.json");
        assertTrue(result);

        result = FilePathChecker.containsIllegalCharacters("/data/ou|t.json");
        assertTrue(result);

        result = FilePathChecker.containsIllegalCharacters("/data/?out.json");
        assertTrue(result);

        result = FilePathChecker.containsIllegalCharacters("/data/*/out.json");
        assertTrue(result);

        result = FilePathChecker.containsIllegalCharacters("/data/'IllegalQuote1'/out.json");
        assertTrue(result);
    }
}
