package meditracker.dailymedication;

import meditracker.time.Period;

// @@author T0nyLin
/**
 * Stores name and the status of daily medication (taken or not)
 */
public class DailyMedication {
    private Period period;
    private String name;
    private boolean isTaken;
    private double dosage;

    /**
     * Constructs DailyMedication with medication name and status of daily medication (taken or not)
     *
     * @param name Name of the medication to be taken
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

    @Override
    public String toString() {
        String takenIcon = isTaken ? "[X]" : "[ ]"; // X identify medication as taken
        return String.format("%s %s | %.1f", takenIcon, name, dosage);
    }

    public String toStringData() {
        return String.format("%c|%b|%s|%f", period.badge, isTaken, name, dosage);
    }

    public static DailyMedication fromStringData(String data) {
        String[] fields = data.split("\\|");
        if (fields.length != 4) {
            return null;
        }

        Period period = Period.getPeriod(fields[0].charAt(0));
        double dosage;
        try {
            dosage = Double.parseDouble(fields[3]);
        } catch (NumberFormatException e) {
            return null;
        }

        DailyMedication dailyMedication = new DailyMedication(fields[2].trim(), dosage, period);

        boolean isTaken = Boolean.parseBoolean(fields[1].trim());
        if (isTaken) {
            dailyMedication.take();
        } else {
            dailyMedication.untake();
        }

        return dailyMedication;
    }
}
// @@author
