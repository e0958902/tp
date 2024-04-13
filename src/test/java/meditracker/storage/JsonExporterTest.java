package meditracker.storage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import meditracker.MediTrackerConfig;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;


/**
 * A class to test the JSON export functionality.
 */
public class JsonExporterTest {
    private static Path fileToExport = null;

    /**
     * Pre-populate the medication manager with some medications that we need to simulate data exporting. They can be
     * potentially errornous (i.e. empty field where they are not supposed to be).
     */
    @BeforeAll
    public static void initiateMedicationManager()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method addMedicationWithoutChecksMethod
                = MedicationManager.class.getDeclaredMethod("addMedicationWithoutChecks", Medication.class);
        addMedicationWithoutChecksMethod.setAccessible(true);

        Medication med1 = new Medication(
                "Test Valid Medication 1",
                69.0,
                null,
                null,
                null,
                "2024-11-23",
                "No Remarks",
                1,
                87
        );

        Medication med2 = new Medication(
                "Test Valid Medication 2",
                10000.0,
                null,
                null,
                null,
                "2025-01-01",
                "",
                1,
                87
        );

        Medication med3 = new Medication(
                "Invalid Medication 4",
                999.0,
                0.0,
                0.0,
                0.0,
                "2025-01-01",
                "null",
                1,
                87
        );

        addMedicationWithoutChecksMethod.invoke(MedicationManager.class, med1);
        addMedicationWithoutChecksMethod.invoke(MedicationManager.class, med2);
        addMedicationWithoutChecksMethod.invoke(MedicationManager.class, med3);
    }

    @BeforeEach
    public void setUpWriteFile() {
        Path jsonSaveFile = MediTrackerConfig.getDefaultJsonSaveFilePath();
        Path jsonFolder = FileReaderWriter.getFullPathComponent(jsonSaveFile, true);
        fileToExport = FileReaderWriter.createTempSaveFile(jsonFolder);
    }

    @AfterEach
    public void cleanup() {
        try {
            if (fileToExport != null) {
                Files.deleteIfExists(fileToExport);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void placeHolder() {
        JsonExporter.saveMedicationDataToJson(fileToExport);
    }
}
