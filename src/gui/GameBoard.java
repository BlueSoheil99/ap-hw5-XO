package gui;

import logic.BoardListener;

import java.awt.*;

public class GameBoard extends GamePanel {

    private final int size = 7;
    private final int tileSize = 80;
    private final int gapSize = 5;

    private Tile[] tiles = new Tile[size * size];
    private BoardListener boardListener;
    private String playerSign, opponentSign;
    private boolean playerTurn;

    GameBoard(String playerSign, boolean playerTurn) {
        this.playerSign = playerSign;
        if (playerSign.equals("X")) opponentSign = "O";
        else opponentSign = "X";
        this.playerTurn = playerTurn;
        setPreferredSize(new Dimension(size * (tileSize + gapSize) - gapSize, size * (tileSize + gapSize) - gapSize));

        setTiles();
        addTiles();
    }

    private void setTiles() {
        for (int i = 0; i < size * size; i++) {
            Tile tile = new Tile(i);
            tiles[i] = tile;

            tile.addActionListener(e -> {
                Tile selectedTile = ((Tile) e.getSource());
//                if (!selectedTile.isFilled() && playerTurn) {
                if (!selectedTile.isFilled()) {
                    if (playerTurn) {
                        selectedTile.setXO(playerSign, false);
                        changeTurn();
                        if (boardListener != null) boardListener.selectPlayer(tile.getTileNumber());
                    } else {
                        setEnemyXO(tile.getTileNumber());
                        if (boardListener != null) boardListener.selectOpponent(tile.getTileNumber());
                    }
                }
            });
        }
    }

    void setEnemyXO(int tileNumber) {
        tiles[tileNumber].setXO(opponentSign, true);
        changeTurn();
    }

    private void changeTurn() {
        playerTurn = !playerTurn;

    }

    private void addTiles() {
        setLayout(new GridLayout(size, size, gapSize, gapSize));
        setBackground(Color.BLACK);
        for (int i = 0; i < size * size; i++) {
            add(tiles[i]);
        }
    }

    void setWinningTiles(boolean playerWon, Integer[] winningTiles) {
        for (int i = 0; i < 4; i++) {
            tiles[winningTiles[i]].setToWinnerTile(playerWon);
        }
    }

    public void setEnabled(boolean enabled) {
        for (Tile tile : tiles) tile.setEnabled(enabled);
    }

    void setBoardListener(BoardListener boardListener) {
        this.boardListener = boardListener;
    }
}
