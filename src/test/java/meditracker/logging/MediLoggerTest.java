package meditracker.logging;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;


public class MediLoggerTest {

    @Test
    public void getMediLogger_noInput_loggerReturned() {
        Logger mediLogger = MediLogger.getMediLogger();
        assertNotNull(mediLogger);
    }
}
