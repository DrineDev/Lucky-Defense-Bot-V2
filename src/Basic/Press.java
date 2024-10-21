package Basic;

import java.io.IOException;

public class Press {
    /**
     * Press within random coordinates in topLeft and bottomRight
     * @param topLeft
     * @param bottomRight
     * @param actionName
     */
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

    /**
     * Press coordinates instantly
     * @param coordinates
     * @param actionName
     */
    public static void press(Coordinates coordinates, String actionName) {
        Process process;
        try {
            process = Runtime.getRuntime().exec("adb shell input tap" + coordinates.toString());
            System.out.println(actionName + " pressed at: " + coordinates.toString());
        } catch (IOException i) {
            System.out.println("Pressing " + actionName + "failed.");
        }
    }
}
