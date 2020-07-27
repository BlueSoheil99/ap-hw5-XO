package gui;

import java.awt.*;

public class GameBoard extends GamePanel {

    private final int size = 7;
    private final int tileSize = 80;
    private final int gapSize = 5;

    private Tile[] tiles = new Tile[size * size];
    private BoardListener boardListener;
    private String playerSign;
    private boolean yourTurn;

    public GameBoard(String playerSign) { //todo make it package accessible
        this.playerSign = playerSign;
        yourTurn = true; //todo

        setTiles();
        addTiles();
        setPreferredSize(new Dimension(size * (tileSize + gapSize) - gapSize, size * (tileSize + gapSize) - gapSize));
    }

    private void setTiles() {
        for (int i = 0; i < size * size; i++) {
            Tile tile = new Tile();
            tiles[i] = tile;

            tile.addActionListener(e -> {
                Tile selectedTile = ((Tile) e.getSource());
                if (!selectedTile.isFilled()) {
                    selectedTile.setXO(playerSign, !yourTurn);
                    //todo sync with enemy + check result in here or in boardListener
                    //if ( boardListener != null ) boardListener.update();
                    yourTurn = !yourTurn;
                    if (playerSign.equals("X")) playerSign = "O";
                    else playerSign = "X";
                }
            });
        }
    }

    private void addTiles() {
        //todo combine with setTiles method
        setLayout(new GridLayout(size, size, gapSize, gapSize));
        setBackground(Color.BLACK);
        for (int i = 0; i < size * size; i++) {
            add(tiles[i]);
        }
    }

    public void setEnabled(boolean enabled) {
        //todo
    }
}
