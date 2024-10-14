package Emulator;

import java.io.IOException;

public class EmulatorBasic {

    public static void openEmulator() { // open emulator
        try {
            Process process;
            process = Runtime.getRuntime().exec("C:/Program Files/Netease/MuMuPlayerGlobal-12.0/shell/MuMuPlayer.exe");
            Thread.sleep(5000);
            process = Runtime.getRuntime().exec("adb connect 127.0.0.1:7555");
            Thread.sleep(5000);
            process = Runtime.getRuntime().exec("adb shell wm size 540x960");
            Thread.sleep(5000);
        } catch (IOException | InterruptedException i) {
            System.out.println("Process opening failed.");
        }
    }
}
