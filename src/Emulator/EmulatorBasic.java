package Emulator;

import java.io.IOException;

public class EmulatorBasic {

    public static void openEmulator() { // open emulator
        try {
            Process process = Runtime.getRuntime().exec("C:/Program Files/Netease/MuMuPlayerGlobal-12.0/shell/MuMuPlayer.exe");
        } catch (IOException i) {
            System.out.println("Process opening failed.");
        }
    }
}
