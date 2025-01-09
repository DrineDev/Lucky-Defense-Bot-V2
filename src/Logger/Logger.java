package Logger;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    // Define the date-time format pattern
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Log method to add timestamp and log the message
    public static void log(String message) {
        String timestamp = dtf.format(LocalTime.now());
        System.out.println("[" + timestamp + "] " + message);
    }
}