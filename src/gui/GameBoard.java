package gui;

import javax.swing.*;
import java.awt.*;

public class GameBoard extends GamePanel {

    private int size = 4;
    private int tileSize = 100;
    private int gapSize = 10;

    private Tile[][] tiles = new Tile[size][size];
    private BoardListener boardListener;
    private String currentPlayer;

    public GameBoard(){ //todo make it package accessible
//        currentPlayer="X"
        setTiles();
        addTiles();
        setSize(new Dimension(size * (tileSize+gapSize)-gapSize,size * (tileSize+gapSize)-gapSize));
    }

    private void setTiles(){
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                Tile tile = new Tile();
//                tile.addMouseListener();
                tiles[i][j]=tile;
            }
        }
    }

    private void addTiles() {
        setLayout(new GridLayout(size , size,10,10));
        setBackground(Color.BLACK);
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                add(tiles[i][j]);

            }
        }
    }
}
