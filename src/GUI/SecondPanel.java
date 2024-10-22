package GUI;

import javax.swing.*;
import java.awt.*;

public class SecondPanel extends JPanel {

    public SecondPanel(){

        ImageIcon image1 = new ImageIcon("Resources/GUI Images/Welcome.png");
        ImageIcon image2 = new ImageIcon("Resources/GUI Images/To.png");
        ImageIcon image3 = new ImageIcon("Resources/GUI Images/Defense.png");
        JLabel welcomeImg = new JLabel(image1);
        JLabel toImg = new JLabel(image2);
        JLabel defenseImg = new JLabel(image3);

        welcomeImg.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Bottom space of 10 pixels
        toImg.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        defenseImg.setBorder((BorderFactory.createEmptyBorder(50, 0, 0, 0)));

        this.setLayout(new FlowLayout());
        this.setBorder(BorderFactory.createLineBorder(new Color(0xDAA520), 3));
        this.setBackground(new Color(0xFAF3E0));
        this.setPreferredSize(new Dimension(450, 400));
        this.add(welcomeImg, BorderLayout.NORTH);
        this.add(toImg, BorderLayout.CENTER);
        this.add(defenseImg, BorderLayout.SOUTH);
    }
}
