package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Tile extends JButton {
    private int tileNumber;
    private boolean available = true;
    private Font tileFont = new Font("Helvetica", Font.BOLD, 45);

    Tile(int tileNumber) {
        this.tileNumber = tileNumber;
        drawTile();
//        setPreferredSize(new Dimension(size, size));
//        setMinimumSize(new Dimension(size, size));
        addMouseListener(getListener());
    }

    private void drawTile() {
        setBorder(null);
        setBackground(Color.WHITE);
        setForeground(Color.WHITE);
    }

    private MouseListener getListener() {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (available) setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (available) setBackground(Color.WHITE);
            }
        };
    }

    boolean isFilled() {
        return !available;
    }
    int getTileNumber(){
        return tileNumber;
    }

    void setXO(String XorO, boolean isEnemy) {
        if (available) {
            available = false;
            setText(XorO);
            setFont(tileFont);
            if (isEnemy) setBackground(new Color(179, 0, 0));
            else setBackground(new Color(0, 179, 0));
        }
    }

    void setToWinnerTile(boolean playerIsWinner) {
        //to show the winning tiles
        setForeground(new Color(249, 217, 9));
        if (playerIsWinner) setBackground(new Color(0, 255, 0));  //for winner
        else setBackground(new Color(255, 0, 0)); //for loser
    }

}
