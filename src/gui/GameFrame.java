package gui;

import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("XO");
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void initFrame(JPanel panel) {
        setContentPane(panel);
        setPreferredSize(panel.getSize());
//        setMinimumSize(panel.getSize()); // without it frame gets smaller and smaller
        pack();
        revalidate();
        setLocationRelativeTo(null);
    }
}
