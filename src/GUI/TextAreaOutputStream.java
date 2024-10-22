package GUI;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

public class TextAreaOutputStream extends OutputStream {
    private final JTextArea textArea;

    public TextAreaOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        // Convert the byte to a character
        String output = String.valueOf((char) b);
        // Append the output to the JTextArea
        SwingUtilities.invokeLater(() -> {
            textArea.append(output);
            textArea.setCaretPosition(textArea.getDocument().getLength()); // Scroll to the bottom
        });
    }
}
