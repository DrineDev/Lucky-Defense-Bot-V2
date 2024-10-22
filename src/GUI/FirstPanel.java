package GUI;

import javax.swing.*;
import java.awt.*;

public class FirstPanel extends JPanel {

    public FirstPanel(){

        ImageIcon titleIcon = new ImageIcon("Resources/GUI Images/FINALPLS.png");
        JLabel pictureLabel = new JLabel(titleIcon);

        JSeparator topSeparator = new JSeparator(SwingConstants.HORIZONTAL) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0xFFDAA5)); // Set your desired color here
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        topSeparator.setPreferredSize(new Dimension(0, 5)); // Set a thin separator height

        // Create bottom separator with custom color
        JSeparator bottomSeparator = new JSeparator(SwingConstants.HORIZONTAL) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0xFFDAA5)); // Set your desired color here
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        bottomSeparator.setPreferredSize(new Dimension(0, 5));

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(topSeparator, BorderLayout.NORTH);
        this.add(pictureLabel, BorderLayout.CENTER);
        this.add(bottomSeparator,BorderLayout.SOUTH);
        this.setBackground(new Color(0x4B3621));

    }
}
