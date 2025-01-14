package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RefreshButton extends JButton implements ActionListener {

    private final Color originalColor = new Color(0xFFDAA5);  // Light golden background
    private final Color hoverColor = new Color(0xFFB347);
    private final Color clickColor = new Color(0xCC8400);
    private final MainFrame mainFrame;

    public RefreshButton(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        this.setText("Refresh");
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
                if (getBounds().contains(e.getPoint())) {
                    setBackground(hoverColor);
                } else {
                    setBackground(originalColor);
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Stop the current application logic
        System.out.println("Stopping application...");
        System.exit(0);

        // Restart the application in a new thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Simulate restarting the app
                IntroFrame introFrame = new IntroFrame();
                introFrame.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
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
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}

