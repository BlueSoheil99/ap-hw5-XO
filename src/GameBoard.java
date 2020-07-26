import javax.swing.*;
import javax.swing.text.Position;
import java.awt.*;

public class GameBoard extends JPanel {
    private int size = 4;
    private Tile[][] tiles = new Tile[size][size];

    GameBoard(){
        setTiles();
        addTiles();
        setSize(new Dimension(700,700));

    }

    private void setTiles(){
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                tiles[i][j]=new Tile();
            }
        }
    }

    private void addTiles() {
        setLayout(new GridLayout(size , size));
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                add(tiles[i][j]);
            }
        }
    }
}
