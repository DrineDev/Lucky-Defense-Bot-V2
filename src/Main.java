import GUI.IntroFrame;

import javax.swing.*;
import java.io.IOException;
import Emulator.EmulatorBasic;

public class Main {

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(IntroFrame::new);
//        EmulatorBasic.openEmulator();
    }
}
