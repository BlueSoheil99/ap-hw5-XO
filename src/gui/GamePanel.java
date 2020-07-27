package gui;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    protected Font titleFont = new Font("Chiller" , Font.BOLD , 100);
    protected Font font1 = new Font(Font.SANS_SERIF , Font.BOLD , 45);
    protected Font font2 = new Font(Font.SANS_SERIF , Font.BOLD , 35);
    protected Font font3 = new Font(Font.SANS_SERIF , Font.BOLD , 25);

    GamePanel(){
        setBackground(new Color(212, 249, 254));
        setSize(new Dimension(800, 600));
    }

}
