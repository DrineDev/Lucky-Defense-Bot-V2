package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

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
        // Disable the button to prevent multiple clicks
        this.setEnabled(false);

        // Create a confirmation dialog
        int result = JOptionPane.showConfirmDialog(
                mainFrame,
                "Are you sure you want to restart the application?",
                "Confirm Restart",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            restartApplication();
        } else {
            // Re-enable the button if user cancels
            this.setEnabled(true);
        }
    }

    private void restartApplication() {
        SwingUtilities.invokeLater(() -> {
            try {
                String javaHome = System.getProperty("java.home");
                String javaBin = javaHome + "/bin/java";
                String classpath = System.getProperty("java.class.path");
                String className = Main.class.getName();
                String libPath = "lib\\OpenCV";

                ProcessBuilder builder = new ProcessBuilder(
                        javaBin,
                        "-Djava.library.path=" + libPath,
                        "-cp", classpath,
                        className
                );
                builder.directory(new File(System.getProperty("user.dir")));
                builder.environment().put("PATH", System.getenv("PATH") + ";" + libPath);

                builder.start();

                mainFrame.dispose();
                System.exit(0);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        mainFrame,
                        "Failed to restart the application: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                this.setEnabled(true);
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

