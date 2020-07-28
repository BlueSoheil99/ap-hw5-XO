package gui;

import logic.client.XOClient;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends GamePanel implements Runnable {
    private XOClient client;

    private String[] stats;
    private boolean updateEnabled;
    private ScoreBoard scoreBoard;
    private JLabel title, welcomeMsg, statsLabel;
    private JButton multiBtn;
    // todo check if there is a need for having a quit btn


    public MenuPanel(XOClient client) {
        this.client = client;
        updateEnabled = true;
        createFields();
        render();
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (updateEnabled) {
            try {
                updateScoreBoard();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopUpdating() {
        updateEnabled = false;
    }

    private void createFields() {
        statsLabel = new JLabel();
        updateStates();
        updateScoreBoard();
        title = new JLabel("XO");
        welcomeMsg = new JLabel("Hi " + stats[0] + " :)  welcome to ");
        multiBtn = new JButton("MultiPlayer Game");
        title.setFont(titleFont);
        welcomeMsg.setFont(font3);
        statsLabel.setFont(font2);
        multiBtn.setFont(font1);

        multiBtn.addActionListener(e -> client.playMulti());
    }

    private void render() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 10, 10, 10);
        gc.gridy = GridBagConstraints.RELATIVE;
        gc.gridx = 0;

        add(welcomeMsg, gc);
        add(title, gc);
        add(statsLabel, gc);
        add(multiBtn, gc);
        gc.gridy = 0;
        gc.gridx = 1;
        gc.gridheight = 4;
        add(new JScrollPane(scoreBoard), gc);
    }


    private void updateStates() {
        stats = client.getStates();
        statsLabel.setText("Wins: " + stats[1] + "  Losses: " + stats[2] + "  Score: " + stats[3]);
    }

    public void updateScoreBoard() {
        //todo check this part later
        scoreBoard = new ScoreBoard(client.getBoardUpdates());
        scoreBoard.update(client.getBoardUpdates());
    }
}
