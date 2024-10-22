package GUI;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTextPane textPane; // Use JTextPane instead of JTextArea

    public MainFrame() {
        ImageIcon icon = new ImageIcon("Resources/GUI Images/Luck.png");

        // Create JTextPane and configure it
        textPane = new JTextPane();
        textPane.setEditable(false); // Make it read-only
        JScrollPane scrollPane = new JScrollPane(textPane); // Add scroll functionality
        scrollPane.setPreferredSize(new Dimension(450, 400)); // Set preferred size for scroll pane

        Caret invisible = new InvisibleCaret();
        textPane.setCaret(invisible);

        // Create a panel for the JTextPane
        JPanel textPanePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10)); // Center the text panel
        textPanePanel.setBackground(new Color(0x654321)); // Set the same background color
        textPanePanel.add(scrollPane); // Add JScrollPane to textPanePanel

        // Create other components
        JPanel panel = new FirstPanel(); // Your existing first panel
        JButton exitButton = new ExitButton(); // Your existing exit button

        exitButton.setPreferredSize(new Dimension(100, 40));
        // Create a button panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(0x654321));  // Set background color

        // Create GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 3, 10, 3);  // Padding between components
        gbc.anchor = GridBagConstraints.EAST;  // Align to the right

        // Add the Exit button
        gbc.gridx = 0;  // Column 0
        gbc.gridy = 0;  // Row 0
        buttonPanel.add(exitButton, gbc);

        // Create a container panel for all components
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BorderLayout());
        containerPanel.setBackground(new Color(0x654321)); // Set background color
        containerPanel.add(textPanePanel, BorderLayout.CENTER); // Add textPanePanel to the center
        containerPanel.add(buttonPanel, BorderLayout.SOUTH); // Add button panel at the bottom

        // Setup the main frame
        this.setSize(500, 588);
        this.setLocationRelativeTo(null);
        this.setTitle("Lucky Defense Bot");
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.NORTH); // Add first panel at the top
        this.add(containerPanel, BorderLayout.CENTER); // Add container panel in the center
        this.setIconImage(icon.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    // Method to append text with a specific color to the JTextPane
    public void appendToPane(String message, String color) {
        StyledDocument doc = textPane.getStyledDocument();

        // Create a style for the color
        Style style = textPane.addStyle("ColorStyle", null);
        switch (color.toLowerCase()) {
            case "blue":
                StyleConstants.setForeground(style, Color.BLUE);
                break;
            case "green":
                StyleConstants.setForeground(style, Color.GREEN);
                break;
            default:
                StyleConstants.setForeground(style, Color.BLACK);
                break;
        }

        try {
            doc.insertString(doc.getLength(), message, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private static class InvisibleCaret extends DefaultCaret {
        @Override
        public void paint(Graphics g) {
            // Do nothing, this makes the caret invisible
        }
    }
}
