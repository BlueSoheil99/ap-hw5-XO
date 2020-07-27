package gui;

import network.XOClient;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends GamePanel {
    private XOClient client;
    private JLabel title, welcomeMsg;
    private JButton soloBtn, multiBtn;


    public MenuPanel(XOClient client) {
        this.client = client;
        createFields();
        render();
    }

    private void createFields() {
        title = new JLabel("XO");
        welcomeMsg = new JLabel("Hi " + client.getPlayerName() + " :)  welcome to ");
        soloBtn = new JButton("     Solo Game     ");
        multiBtn = new JButton("MultiPlayer Game");
        title.setFont(titleFont);
        welcomeMsg.setFont(font3);
        soloBtn.setFont(font1);
        multiBtn.setFont(font1);
        setActionListeners();
    }

    private void setActionListeners() {
        soloBtn.addActionListener(e -> client.playSolo());
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
        add(soloBtn, gc);
        add(multiBtn, gc);
    }
}
