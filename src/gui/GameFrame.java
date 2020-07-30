package gui;

import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("XO");
        setResizable(false);
        setVisible(true);
    }

    public void initFrame(JPanel panel) {
        setContentPane(panel);
        setPreferredSize(panel.getSize());
        if (panel.getClass()==LoginAndRegisterPanel.class) setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        else setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        pack();
        revalidate();
        setLocationRelativeTo(null);
    }
}
