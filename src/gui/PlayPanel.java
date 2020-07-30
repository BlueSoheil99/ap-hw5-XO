package gui;

import logic.BoardListener;
import logic.client.XOClient;

import javax.swing.*;
import java.awt.*;

public class PlayPanel extends GamePanel {
    private XOClient client;
    private GameBoard board;

    private JButton exitBtn;
    private JLabel playerLabel, opponentLabel, turnLabel;
    private boolean isPlayerTurn;
    private boolean matchFinished = false;
    private Boolean hasPlayerWon;

    public PlayPanel(XOClient client, String opponentName, String playerSign, String opponentSign, boolean isPlayerTurn) {
        this.client = client;
        playerLabel = new JLabel(" You: " + playerSign);
        opponentLabel = new JLabel(" " + opponentName + ": " + opponentSign);
        turnLabel = new JLabel();
        opponentLabel.setFont(font1);
        playerLabel.setFont(font1);
        board = new GameBoard(playerSign, isPlayerTurn);
        this.isPlayerTurn = isPlayerTurn;

        setupExitBtn();
        setTurn();
        render();
    }

    private void setupExitBtn() {
        exitBtn = new JButton("exit");
        exitBtn.setFont(font3);

        exitBtn.addActionListener(e -> {
            if (!matchFinished) {
                int result = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to forfeit and lose the match?", "Confirm Forfeiting", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    endGame(false);
                    client.runMenu();
                }
            } else
                client.runMenu();
        });
    }

    private void render() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridheight = 5;
        gc.gridwidth = 4;
        add(board, gc);

        gc.gridx = 4;
        gc.gridheight = 1;
        gc.gridwidth = 2;
        add(playerLabel, gc);
        gc.gridy = 1;
        add(opponentLabel, gc);
        gc.gridy = 2;
        gc.gridheight = 2;
        add(turnLabel, gc);
        gc.gridy = 4;
        gc.gridheight = 1;
        add(exitBtn, gc);
    }

    private void setTurn() {
//        board.setEnabled(isPlayerTurn); todo uncomment it later and also enabling board has conflicts with gameBoard itself. check it later
        if (isPlayerTurn) {
            turnLabel.setText(" YOUR TURN ");
            turnLabel.setForeground(Color.GREEN);
        } else {
            turnLabel.setText("       WAIT       ");

            turnLabel.setForeground(Color.RED);
        }
        turnLabel.setFont(font2);
    }

    public void changeTurn() {
        isPlayerTurn = !isPlayerTurn;
        setTurn();
    }

    public void setBoardListener(BoardListener boardListener) {
        board.setBoardListener(boardListener);
    }

    public void playerWon(Integer[] winningTiles) {
        turnLabel.setText("   YOU WON  ");
        turnLabel.setForeground(Color.GREEN);
        board.setWinningTiles(true, winningTiles);
        endGame(true);
    }

    public void playerLost(Integer[] winningTiles) {
        turnLabel.setText("   YOU LOST ");
        turnLabel.setForeground(Color.RED);
        board.setWinningTiles(false, winningTiles);
        endGame(false);
    }

    public void tie() {
        turnLabel.setText("  MATCH TIED");
        turnLabel.setForeground(Color.BLUE);
        endGame(null);
    }

    private void endGame(Boolean playerWon) {
        disablePanel(playerWon);
        client.endMatch(hasPlayerWon);
    }
    public void disablePanel(Boolean playerWon){
        matchFinished = true;
        board.setEnabled(false);
        hasPlayerWon=playerWon;
    }

}
