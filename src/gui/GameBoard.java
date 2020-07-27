package gui;

import java.awt.*;

public class GameBoard extends GamePanel {

    private int size = 4;
    private int tileSize = 100;
    private int gapSize = 10;

    private Tile[][] tiles = new Tile[size][size]; //todo make it as Tile[size*size]
    private BoardListener boardListener;
    private String currentPlayer;
    private boolean yourTurn;

    public GameBoard() { //todo make it package accessible
        super();
        currentPlayer = "X";
        yourTurn = true;

        setTiles();
        addTiles();
        setSize(new Dimension(size * (tileSize + gapSize) - gapSize, size * (tileSize + gapSize) - gapSize));
    }

    private void setTiles() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                Tile tile = new Tile();
                tiles[i][j] = tile;

                tile.addActionListener(e -> {
                    Tile selectedTile = ((Tile) e.getSource());
                    if (!selectedTile.isFilled()) {
                        selectedTile.setXO(currentPlayer, !yourTurn);
                        //todo sync with enemy + check result in here or in boardListener
                        //if ( boardListener != null ) boardListener.update();
                        yourTurn = !yourTurn;
                        if (currentPlayer.equals("X")) currentPlayer = "O";
                        else currentPlayer = "X";
                    }
                });
            }
        }
    }

    private void addTiles() {
        //todo combine with setTiles method
        setLayout(new GridLayout(size, size, 10, 10));
        setBackground(Color.BLACK);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                add(tiles[i][j]);
            }
        }
    }
}
