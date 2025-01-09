package Basic;

import GUI.MainFrame;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static Logger.Logger.log;

public class Press {
    public static void press(Coordinates topLeft, Coordinates bottomRight, String actionName) {
        Process process;
        Coordinates temp = Coordinates.makeRandomCoordinate(topLeft, bottomRight);

        try {
            process = Runtime.getRuntime().exec("adb shell input tap " + temp);
            log(actionName + " pressed at: " + temp.toString() + ".");
        } catch (IOException i) {
            log(" Pressing " + actionName + " failed.");
        }
    }

    public static void press(Coordinates coordinates, String actionName) {
        Process process;

        try {
            process = Runtime.getRuntime().exec("adb shell input tap" + coordinates.toString());
            log(actionName + " pressed at: " + coordinates.toString() + ".");
        } catch (IOException i) {
            log(" Pressing " + actionName + "failed.");
        }
    }

    public static void press20x(Coordinates topLeft, Coordinates bottomRight, String actionName) {
        Process process;
        Coordinates temp = Coordinates.makeRandomCoordinate(topLeft, bottomRight);

        try {
            for(int i = 0; i < 20; i++) {
                process = Runtime.getRuntime().exec("adb shell input tap " + temp);
                Thread.sleep(100);
            }
            log(actionName + " pressed at: " + temp.toString() + ".");
        } catch (IOException | InterruptedException i) {
            log(" Pressing " + actionName + " failed.");
        }
    }
}
