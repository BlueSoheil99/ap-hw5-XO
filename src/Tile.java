import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Tile extends JLabel implements MouseListener {
    private int size = 60;

    Tile(){
        setPreferredSize(new Dimension(100,100));
        setBackground(Color.BLACK);
    }

    void setX(){
        setText("X");
        setFont(new Font("Helvetica",Font.BOLD,45));
    }
    void setO(){

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        setX();
    }


    @Override
    public void mousePressed(MouseEvent e) {

    }


    @Override
    public void mouseReleased(MouseEvent e) {

    }


    @Override
    public void mouseEntered(MouseEvent e) {

    }


    @Override
    public void mouseExited(MouseEvent e) {

    }

}
