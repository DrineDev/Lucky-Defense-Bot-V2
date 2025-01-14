import GUI.IntroFrame;

import javax.swing.*;
import java.io.IOException;
import Emulator.EmulatorBasic;

public class Main {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            IntroFrame introFrame = new IntroFrame();
            introFrame.setVisible(true);
        });
    }
}
