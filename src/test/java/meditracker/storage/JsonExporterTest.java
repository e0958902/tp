package meditracker.storage;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import meditracker.exception.FileReadWriteException;
import meditracker.medication.Medication;
import meditracker.medication.MedicationManager;


//@@author annoy-o-mus
/**
 * A class to test the JSON export functionality.
 */
public class JsonExporterTest {
    private static File exportedJsonFile = null;

    /**
     * Pre-populate the medication manager with some medications that we need to simulate data exporting. They can be
     * potentially errornous (i.e. empty field where they are not supposed to be).
     */
    @BeforeAll
    public static void initiateMedicationManager() {
        Medication med1 = new Medication(
                "Test Valid Medication 1",
                69.0,
                null,
                null,
                null,
                "23/11/24",
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
                "01/01/25",
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
                "",
                "null",
                1,
                87
        );

        MedicationManager.addMedication(med1);
        MedicationManager.addMedication(med2);
        MedicationManager.addMedication(med3);
    }

    @BeforeEach
    public void setUpWriteFile() {

        try {
            exportedJsonFile = FileReaderWriter.createJsonSaveFile();
        } catch (FileReadWriteException e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    public void cleanup() {
        if (exportedJsonFile != null && exportedJsonFile.exists()) {
            exportedJsonFile.delete();
        }
    }

    @Test
    public void placeHolder() {
        JsonExporter.saveMedicationDataToJson(exportedJsonFile);
    }
}
