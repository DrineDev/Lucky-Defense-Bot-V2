package Basic;

import GUI.MainFrame;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Press {
    /**
     * Press within random coordinates in topLeft and bottomRight
     * @param topLeft
     * @param bottomRight
     * @param actionName
     */

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void press(Coordinates topLeft, Coordinates bottomRight, String actionName) {
        Process process;
        String currentTime = LocalDateTime.now().format(dtf);
        Coordinates temp = Coordinates.makeRandomCoordinate(topLeft, bottomRight);

        try {
            process = Runtime.getRuntime().exec("adb shell input tap " + temp.toString());
            System.out.println("[" + currentTime + "]" + actionName + " pressed at: " + temp.toString());
        } catch (IOException i) {
            System.out.println("[" + currentTime + "]" + " Pressing " + actionName + " failed.");
        }
    }

    /**
     * Press coordinates instantly
     * @param coordinates
     * @param actionName
     */
    public static void press(Coordinates coordinates, String actionName) {
        Process process;
        String currentTime = LocalDateTime.now().format(dtf);

        try {
            process = Runtime.getRuntime().exec("adb shell input tap" + coordinates.toString());
            System.out.println("[" + currentTime + "]" + actionName + " pressed at: " + coordinates.toString());
        } catch (IOException i) {
            System.out.println("[" + currentTime + "]" + " Pressing " + actionName + "failed.");
        }
    }


}
