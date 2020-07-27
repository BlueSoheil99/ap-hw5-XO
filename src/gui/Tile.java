package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Tile extends JButton {
    private int size = 45;
    private boolean enable = true;
    private Font tileFont = new Font("Helvetica", Font.BOLD, 45);

    Tile() {
        drawTile();
        setPreferredSize(new Dimension(size, size));
        setMinimumSize(new Dimension(size, size));
        addMouseListener(getListener());
    }

    private void drawTile() {
        setBorder(null);
        setBackground(Color.WHITE);
        setForeground(Color.black);
    }

    private MouseListener getListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (enable){
                    setX();
                    setBackground(Color.WHITE);
                    enable = false;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (enable) setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (enable) setBackground(Color.WHITE);
            }
        };
    }

    void setX() {
        setText("X");
        setFont(tileFont);
    }

    void setO() {

    }


}
