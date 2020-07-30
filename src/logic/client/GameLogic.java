package logic.client;

public class GameLogic {
    // this class has some duplicated codes and i thought it is cleaner than a code without duplicates

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

    private void setupTilesMatrix() {
        tilesMatrix = new String[7][7];
        for (int i = 0; i < 49; i++) {
            String tile = tiles[i];
            tilesMatrix[i / 7][i % 7] = tile;
        }
    }

    ///////////////
    ///////////////

    // check out matchTie.png to verify the functionality of this method
    public boolean checkForTie() {
        System.out.println("checked for tie");
        setupTilesMatrix();
        int ties = 0;
        if (checkTieForRow()) ties++;
        if (checkTieForColumn()) ties++;
        if (checkTieForDiagonal()) ties++;
        if (checkTieForAntiDiagonal()) ties++;
        if (!isPlayerWinPossible()) ties++;
        if (!isOpponentWinPossible()) ties++;

        return ties == 6;
    }

    private boolean checkTieForDiagonal() {
        int ties = 0;
        for (int i = 0; i < 4; i++) { // i is for row
            for (int j = 0; j < 4; j++) { //j is for column
                if (tilesMatrix[i][j] != null || tilesMatrix[i + 1][j + 1] != null ||
                        tilesMatrix[i + 2][j + 2] != null || tilesMatrix[i + 3][j + 3] != null) {
                    ties++;
                }
            }
        }
        return ties == 16;
    }

    private boolean checkTieForAntiDiagonal() {
        int ties = 0;
        for (int i = 0; i < 4; i++) { // i is for row
            for (int j = 3; j < 7; j++) { //j is for column
                if (tilesMatrix[i][j] != null || tilesMatrix[i + 1][j - 1] != null ||
                        tilesMatrix[i + 2][j - 2] != null || tilesMatrix[i + 3][j - 3] != null) {
                    ties++;
                }
            }
        }
        return ties == 16;
    }

    private boolean checkTieForColumn() {
        int ties = 0;
        for (int i = 0; i < 4; i++) { // i is for row
            for (int j = 0; j < 7; j++) { //j is for column
                if (tilesMatrix[i][j] != null || tilesMatrix[i + 1][j] != null ||
                        tilesMatrix[i + 2][j] != null || tilesMatrix[i + 3][j] != null) {
                    ties++;
                }
            }
        }
        return ties == 28;
    }

    private boolean checkTieForRow() {
        int ties = 0;
        for (int i = 0; i < 7; i++) { // i is for row
            for (int j = 0; j < 4; j++) { //j is for column
                if (tilesMatrix[i][j] != null || tilesMatrix[i][j + 1] != null ||
                        tilesMatrix[i][j + 2] != null || tilesMatrix[i][j + 3] != null) {
                    ties++;
                }
            }
        }
        return ties == 28;
    }

    private boolean isPlayerWinPossible() {
        int ties = 0;
        if (!checkForDiagonalPossibility(opponentSign)) ties++;
        if (!checkForAntiDiagonalPossibility(opponentSign)) ties++;
        if (!checkForRowPossibility(opponentSign)) ties++;
        if (!checkForColumnPossibility(opponentSign)) ties++;
        return ties != 4;
    }

    private boolean isOpponentWinPossible() {
        int ties = 0;
        if (!checkForDiagonalPossibility(playerSign)) ties++;
        if (!checkForAntiDiagonalPossibility(playerSign)) ties++;
        if (!checkForRowPossibility(playerSign)) ties++;
        if (!checkForColumnPossibility(playerSign)) ties++;
        return ties != 4;
    }

    private boolean checkForDiagonalPossibility(String forbiddenSign) {
        boolean thereIsPossibility = false;
        for (int i = 0; i < 4; i++) { // i is for row
            for (int j = 0; j < 4; j++) { //j is for column
                if (tilesMatrix[i][j] != forbiddenSign && tilesMatrix[i + 1][j + 1] != forbiddenSign &&
                        tilesMatrix[i + 2][j + 2] != forbiddenSign && tilesMatrix[i + 3][j + 3] != forbiddenSign) {
                    thereIsPossibility = true;
                    break;
                }
            }
        }
        return thereIsPossibility;
    }

    private boolean checkForAntiDiagonalPossibility(String forbiddenSign) {
        boolean thereIsPossibility = false;
        for (int i = 0; i < 4; i++) { // i is for row
            for (int j = 3; j < 7; j++) { //j is for column
                if (tilesMatrix[i][j] != forbiddenSign && tilesMatrix[i + 1][j - 1] != forbiddenSign &&
                        tilesMatrix[i + 2][j - 2] != forbiddenSign && tilesMatrix[i + 3][j - 3] != forbiddenSign) {
                    thereIsPossibility = true;
                    break;
                }
            }
        }
        return thereIsPossibility;
    }

    private boolean checkForColumnPossibility(String forbiddenSign) {
        boolean thereIsPossibility = false;
        for (int i = 0; i < 4; i++) { // i is for row
            for (int j = 0; j < 7; j++) { //j is for column
                if (tilesMatrix[i][j] != forbiddenSign && tilesMatrix[i + 1][j] != forbiddenSign &&
                        tilesMatrix[i + 2][j] != forbiddenSign && tilesMatrix[i + 3][j] != forbiddenSign) {
                    thereIsPossibility = true;
                    break;
                }
            }
        }
        return thereIsPossibility;
    }

    private boolean checkForRowPossibility(String forbiddenSign) {
        boolean thereIsPossibility = false;
        for (int i = 0; i < 7; i++) { // i is for row
            for (int j = 0; j < 4; j++) { //j is for column
                if (tilesMatrix[i][j] != forbiddenSign && tilesMatrix[i][j + 1] != forbiddenSign &&
                        tilesMatrix[i][j + 2] != forbiddenSign && tilesMatrix[i][j + 3] != forbiddenSign) {
                    thereIsPossibility = true;
                    break;
                }
            }
        }
        return thereIsPossibility;
    }

    ////////////////
    ////////////////

    public Integer[] checkForWin(boolean checkForPlayer) {
        if (checkForPlayer) System.out.println("checked for your win");
        else System.out.println("checked for opponent win");

        setupTilesMatrix();
        Integer[] winningTiles;
        String sign;
        if (checkForPlayer) sign = playerSign;
        else sign = opponentSign;

        winningTiles = checkForRow(sign);
        if (winningTiles == null) winningTiles = checkForColumn(sign);
        if (winningTiles == null) winningTiles = checkForDiagonal(sign);
        if (winningTiles == null) winningTiles = checkForAntiDiagonal(sign);
        return winningTiles;
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
