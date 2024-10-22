package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartButton extends JButton implements ActionListener {

    private final Color originalColor = new Color(0xFFDAA5);  // Light golden background
    private final Color hoverColor = new Color(0xFFB347);
    private final Color clickColor = new Color(0xCC8400);
    private IntroFrame introFrame;
    private MainFrame mainFrame;

    public StartButton(IntroFrame introFrame) {
        this.introFrame = introFrame;
        this.mainFrame = mainFrame;
        this.setText("Start");
        this.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        this.setForeground(new Color(0x654321));
        this.setBackground(new Color(0xFFDAA5));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        this.setFocusable(false);
        this.setContentAreaFilled(false);
        this.addActionListener(this);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(originalColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(clickColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Revert to hover color (if still hovering) or original color (if not hovering)
                if (getBounds().contains(e.getPoint())) {
                    setBackground(hoverColor);  // Mouse is still over the button
                } else {
                    setBackground(originalColor);  // Mouse has moved away
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Hide the IntroFrame
        SwingUtilities.getWindowAncestor(this).dispose(); // Close the IntroFrame

        // Create and show the main frame
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true); // Ensure it's visible

        // Start the other program in a new thread
        new Thread(() -> {
            SomeOtherProgram otherProgram = new SomeOtherProgram(mainFrame);
            otherProgram.run(); // This will use System.out to print messages
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(clickColor);
        } else if (getModel().isRollover()) {
            g.setColor(hoverColor);
        } else {
            g.setColor(originalColor);
        }
        g.fillRect(0, 0, getWidth(), getHeight());  // Fill the background with the color
        super.paintComponent(g);  // Continue painting the rest of the component
    }

}
