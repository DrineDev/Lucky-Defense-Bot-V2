package Basic;

import java.io.IOException;

public class Press {
    public static void press(Coordinates topLeft, Coordinates bottomRight, String actionName) {
        Process process;
        Coordinates temp = Coordinates.makeRandomCoordinate(topLeft, bottomRight);
        try {
            process = Runtime.getRuntime().exec("adb shell input tap " + temp.toString());
            System.out.println(actionName + " pressed at: " + temp.toString());
        } catch (IOException i) {
            System.out.println("Pressing " + actionName + " failed.");
        }
    }
}
