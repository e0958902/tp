package meditracker.dailymedication;

import meditracker.logging.MediLogger;
import meditracker.time.Period;

import java.util.logging.Logger;

// @@author T0nyLin
/**
 * Stores name and the status of daily medication (taken or not)
 */
public class DailyMedication {
    private static final Logger MEDILOGGER = MediLogger.getMediLogger();
    private Period period;
    private String name;
    private boolean isTaken;
    private double dosage;

    /**
     * Constructs DailyMedication with medication name and status of daily medication (taken or not)
     *
     * @param name Name of the medication to be taken
     * @param dosage Dosage to be taken at a particular time of the day
     * @param period Period of the day to take medication
     */
    public DailyMedication(String name, double dosage, Period period) {
        this.name = name;
        this.isTaken = false;
        this.dosage = dosage;
        this.period = period;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void take() {
        isTaken = true;
    }

    public void untake() {
        isTaken = false;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public double getDosage() {
        return dosage;
    }

    public void setDosage(double dosage) {
        this.dosage = dosage;
    }

    /**
     * String format to print on the application when user runs list -t today
     *
     * @return Formatted String in the way it should look like
     */
    @Override
    public String toString() {
        String takenIcon = isTaken ? "[X]" : "[ ]"; // X identify medication as taken
        return String.format("%s %s | %.1f", takenIcon, name, dosage);
    }

    /**
     * String format to save the today's medication data into text file
     *
     * @return Formatted String to save in text file
     */
    public String toStringData() {
        return String.format("%s|%b|%s|%f", period.badge, isTaken, name, dosage);
    }

    /**
     * Parses String read from text file
     *
     * @param data the String to split according to the specified splitter
     * @return Daily Medication to be saved into the DailyMedication lists
     */
    public static DailyMedication fromStringData(String data) {
        String[] fields = data.split("\\|");

        Period period = Period.getPeriod(fields[0].toUpperCase());
        double dosage;

        if (fields.length != 4) {
            MEDILOGGER.warning("Unable to import data from text file. "
                    + "Potentially due to corruption of data. --> (Skipping over medication for "
                    + fields[0] + " period)");
            return null;
        }

        if (period == Period.UNKNOWN) {
            MEDILOGGER.warning("Assigned medication period not recognised. \"" + fields[2]
                    + "\" not imported into today's list for " + period + " period.");
            return null;
        }

        try {
            dosage = Double.parseDouble(fields[3]);
        } catch (NumberFormatException e) {
            MEDILOGGER.warning("Medication dosage not recognised. \"" + fields[3]
                    + "\" not imported into today's list for " + period + " period.");
            return null;
        }

        DailyMedication dailyMedication = new DailyMedication(fields[2].trim(), dosage, period);

        boolean isTaken = Boolean.parseBoolean(fields[1].toLowerCase().trim());
        if (isTaken) {
            dailyMedication.take();
        } else {
            dailyMedication.untake();
        }

        return dailyMedication;
    }
}
// @@author
