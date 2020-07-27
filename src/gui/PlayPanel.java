package gui;

import javax.swing.*;

public class PlayPanel extends GamePanel {
    private GameBoard board;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem menuItem;

    public PlayPanel() {
        createFields();
        render();
    }

    private void createFields() {
        board = new GameBoard();

    }

    private void render() {
        add(board);
    }
}
