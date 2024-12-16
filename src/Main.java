import Basic.Coordinates;
import Basic.Screenshot;
import GUI.IntroFrame;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(IntroFrame::new);
    }
}
