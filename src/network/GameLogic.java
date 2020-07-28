package network;

import gui.Tile;

public class GameLogic {
    private String[] tiles;
    private String[][] tilesMatrix;
    private String playerSign;
    private String opponentSign;

    GameLogic(String[] tiles, String playerSign) {
        this.tiles = tiles;
        setPlayerSign(playerSign);
    }

    private void setPlayerSign(String playerSign) {
        this.playerSign = playerSign;
        if (playerSign.equals("X"))
            opponentSign = "O";
        else
            opponentSign = "X";
    }

    public Integer[] checkForWin(boolean checkForPlayer) {
        if (checkForPlayer) System.out.println("checked for your win");
        else System.out.println("checked for opponent win");

        setupTilesMatrix();
        Integer[] winningTiles;
        String sign;
        if (checkForPlayer == true) sign = playerSign;
        else sign = opponentSign;

        winningTiles = checkForRow(sign);
        if (winningTiles == null) winningTiles = checkForColumn(sign);
        if (winningTiles == null) winningTiles = checkForDiagonal(sign);
        if (winningTiles == null) winningTiles = checkForAntiDiagonal(sign);
        return winningTiles;
    }

    private void setupTilesMatrix() {
        tilesMatrix = new String[7][7];
        for (int i = 0; i < 49; i++) {
            String tile = tiles[i];
            tilesMatrix[i / 7][i % 7] = tile;
        }
    }

    private Integer[] checkForDiagonal(String sign) {
        Integer[] matchedTiles = null;
        for (int i = 0; i < 4; i++) { // i is for row
            for (int j = 0; j < 4; j++) { //j is for column
                if (tilesMatrix[i][j] != null && tilesMatrix[i + 1][j + 1] != null &&
                        tilesMatrix[i + 2][j + 2] != null && tilesMatrix[i + 3][j + 3] != null) {
                    if (tilesMatrix[i][j].equals(sign) && tilesMatrix[i + 1][j + 1].equals(sign) &&
                            tilesMatrix[i + 2][j + 2].equals(sign) && tilesMatrix[i + 3][j + 3].equals(sign)) {
                        matchedTiles = new Integer[4];
                        matchedTiles[0] = i * 7 + j;
                        matchedTiles[1] = (i + 1) * 7 + (j + 1);
                        matchedTiles[2] = (i + 2) * 7 + (j + 2);
                        matchedTiles[3] = (i + 3) * 7 + (j + 3);
                    }
                }
            }
        }
        return matchedTiles;
    }

    private Integer[] checkForAntiDiagonal(String sign) {
        Integer[] matchedTiles = null;
        for (int i = 0; i < 4; i++) { // i is for row
            for (int j = 3; j < 7; j++) { //j is for column
                if (tilesMatrix[i][j] != null && tilesMatrix[i + 1][j - 1] != null &&
                        tilesMatrix[i + 2][j - 2] != null && tilesMatrix[i + 3][j - 3] != null) {
                    if (tilesMatrix[i][j].equals(sign) && tilesMatrix[i + 1][j - 1].equals(sign) &&
                            tilesMatrix[i + 2][j - 2].equals(sign) && tilesMatrix[i + 3][j - 3].equals(sign)) {
                        matchedTiles = new Integer[4];
                        matchedTiles[0] = i * 7 + j;
                        matchedTiles[1] = (i + 1) * 7 + (j - 1);
                        matchedTiles[2] = (i + 2) * 7 + (j - 2);
                        matchedTiles[3] = (i + 3) * 7 + (j - 3);
                    }
                }
            }
        }
        return matchedTiles;
    }

    private Integer[] checkForColumn(String sign) {
        Integer[] matchedTiles = null;
        for (int i = 0; i < 4; i++) { // i is for row
            for (int j = 0; j < 7; j++) { //j is for column
                if (tilesMatrix[i][j] != null && tilesMatrix[i + 1][j] != null &&
                        tilesMatrix[i + 2][j] != null && tilesMatrix[i + 3][j] != null) {
                    if (tilesMatrix[i][j].equals(sign) && tilesMatrix[i + 1][j].equals(sign) &&
                            tilesMatrix[i + 2][j].equals(sign) && tilesMatrix[i + 3][j].equals(sign)) {
                        matchedTiles = new Integer[4];
                        matchedTiles[0] = i * 7 + j;
                        matchedTiles[1] = (i + 1) * 7 + j;
                        matchedTiles[2] = (i + 2) * 7 + j;
                        matchedTiles[3] = (i + 3) * 7 + j;
                    }
                }
            }
        }
        return matchedTiles;
    }

    private Integer[] checkForRow(String sign) {
        Integer[] matchedTiles = null;
        for (int i = 0; i < 7; i++) { // i is for row
            for (int j = 0; j < 4; j++) { //j is for column
                if (tilesMatrix[i][j] != null && tilesMatrix[i][j + 1] != null &&
                        tilesMatrix[i][j + 2] != null && tilesMatrix[i][j + 3] != null) {
                    if (tilesMatrix[i][j].equals(sign) && tilesMatrix[i][j + 1].equals(sign) &&
                            tilesMatrix[i][j + 2].equals(sign) && tilesMatrix[i][j + 3].equals(sign)) {
                        matchedTiles = new Integer[4];
                        matchedTiles[0] = i * 7 + j;
                        matchedTiles[1] = i * 7 + (j + 1);
                        matchedTiles[2] = i * 7 + (j + 2);
                        matchedTiles[3] = i * 7 + (j + 3);
                    }
                }
            }
        }
        return matchedTiles;

    }


}
