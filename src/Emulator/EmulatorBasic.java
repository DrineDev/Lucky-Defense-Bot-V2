package Emulator;

import GUI.MainFrame;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmulatorBasic {

    /**
     * Open emulator
     */
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void openEmulator() {
        String currentTime = LocalDateTime.now().format(dtf);
        // open emulator
        try {
            Process process;
            process = Runtime.getRuntime().exec("C:/Program Files/Netease/MuMuPlayerGlobal-12.0/shell/MuMuPlayer.exe");
            Thread.sleep(5000);
            process = Runtime.getRuntime().exec("adb connect 127.0.0.1:7555");
            Thread.sleep(5000);
            process = Runtime.getRuntime().exec("adb shell wm size 540x960");
            Thread.sleep(5000);
        } catch (IOException | InterruptedException i) {
            System.out.println("[" + currentTime + "]" + " Process opening failed.");
        }
    }

}
