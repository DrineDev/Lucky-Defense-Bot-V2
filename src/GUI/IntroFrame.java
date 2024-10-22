package GUI;

import javax.swing.*;
import java.awt.*;

public class IntroFrame extends JFrame {

    public IntroFrame(){

        ImageIcon icon = new ImageIcon("Luck.png");

        JPanel panel = new FirstPanel();
        JPanel panel2 = new SecondPanel();
        JPanel containerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        JButton startButton = new StartButton(this);
        JButton exitButton = new ExitButton();

        panel.setMaximumSize(new Dimension(450, 400));
        panel2.setMaximumSize(new Dimension(450, 400));

        startButton.setPreferredSize(new Dimension(100, 40));
        exitButton.setPreferredSize(new Dimension(100, 40));

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(0x654321));  // Set background color

        // Create GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 3, 5, 3);  // Padding between components
        gbc.anchor = GridBagConstraints.EAST;  // Align to the right

        // Add the Start button at column 0 (left)
        gbc.gridx = 0;  // Column 0
        gbc.gridy = 0;  // Row 0
        buttonPanel.add(exitButton, gbc);

        // Add the Exit button next to the Start button at column 1 (right)
        gbc.gridx = 1;  // Column 1
        gbc.gridy = 0;  // Row 0 (same row as Start button)
        buttonPanel.add(startButton, gbc);

        // Wrap the button panel in a JScrollPane to allow scrolling if needed
        JPanel wrappedButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        wrappedButton.setPreferredSize(new Dimension(450, 100));  // Adjust this as needed
        wrappedButton.setBackground(new Color(0x654321));
        wrappedButton.add(buttonPanel);  // Add buttonPanel to wrappedButton

        containerPanel.setBackground(new Color(0x654321));
        containerPanel.add(panel2);
        containerPanel.add(wrappedButton);

        this.setSize(500, 588);
        this.setLocationRelativeTo(null);
        this.setTitle("Lucky Defense Bot");
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.NORTH);
        this.add(containerPanel, BorderLayout.CENTER);
        this.setIconImage(icon.getImage());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }
}
