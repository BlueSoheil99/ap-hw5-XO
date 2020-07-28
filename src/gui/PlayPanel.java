package gui;

import network.XOClient;

import javax.swing.*;
import java.awt.*;

public class PlayPanel extends GamePanel {
    private XOClient client;
    private GameBoard board;

    private JButton exitBtn;
    private JLabel playerLabel, opponentLabel, turnLabel;
    private boolean isPlayerTurn;
    private boolean matchFinished = false;

    public PlayPanel(XOClient client, String opponentName, String playerSign, String opponentSign, boolean isPlayerTurn) {
        this.client = client;
        playerLabel = new JLabel("You: " + playerSign);
        opponentLabel = new JLabel(opponentName + ": " + opponentSign);
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
                    //todo loosing stuff
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
        board.setEnabled(isPlayerTurn);
        if (isPlayerTurn) {
            turnLabel = new JLabel("YOUR TURN");
            turnLabel.setForeground(Color.GREEN);
        } else {
            turnLabel = new JLabel("WAIT");
            turnLabel.setForeground(Color.RED);
        }
        turnLabel.setFont(font2);

    }

    public void setBoardListener(BoardListener boardListener) {
        board.setBoardListener(boardListener);
    }

    public void playerWon(Integer[] winningTiles) {
        turnLabel = new JLabel("YOU WON");
        turnLabel.setForeground(Color.GREEN);
        board.setEnabled(false);
        board.setWinningTiles(true , winningTiles);
    }

    public void playerLost(Integer[] winningTiles) {
        turnLabel = new JLabel("YOU LOST");
        turnLabel.setForeground(Color.RED);
        board.setEnabled(false);
        board.setWinningTiles(false , winningTiles);


    }
}
