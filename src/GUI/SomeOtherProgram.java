package GUI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SomeOtherProgram {
    private final MainFrame mainFrame;

    public SomeOtherProgram(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void run() {
        // Create a DateTimeFormatter to format the current time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Print the current time with the first message
        String startTime = LocalDateTime.now().format(dtf);
        appendColoredText("[" + startTime + "] This is a message from the other program.\n", "blue");

        for (int i = 1; i <= 50; i++) {
            // Get the current time for each count message
            String currentTime = LocalDateTime.now().format(dtf);
            appendColoredText("[" + currentTime + "] Count: " + i + "\n", "black");

            try {
                Thread.sleep(1000); // Simulate some work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Print the current time when the program finishes
        String endTime = LocalDateTime.now().format(dtf);
        appendColoredText("[" + endTime + "] Other program finished.\n", "green");
    }

    // Method to append colored text to the JTextPane in MainFrame
    private void appendColoredText(String message, String color) {
        mainFrame.appendToPane(message, color);
    }
}
