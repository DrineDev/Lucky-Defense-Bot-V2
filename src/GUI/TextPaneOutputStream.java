package GUI;

import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.io.IOException;
import java.io.OutputStream;

public class TextPaneOutputStream extends OutputStream {
    private final JTextPane textPane;

    public TextPaneOutputStream(JTextPane textPane) {
        this.textPane = textPane;
    }

    @Override
    public void write(int b) throws IOException {
        // Convert the byte to a character
        String output = String.valueOf((char) b);

        // Append the output to the JTextPane
        SwingUtilities.invokeLater(() -> {
            try {
                StyledDocument doc = textPane.getStyledDocument();
                doc.insertString(doc.getLength(), output, null);
                textPane.setCaretPosition(doc.getLength()); // Scroll to the bottom
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
