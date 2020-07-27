package gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ScoreBoard extends JTable {
    //https://www.youtube.com/watch?v=iMBfneE2Ztg&t=61s
    private static final String[] columns = new String[]{"userName", "status", "score"};
    private String[][] data;

    ScoreBoard(String[][] boardStats) {
        super(boardStats, columns);
        data = boardStats;
        setPreferredScrollableViewportSize(new Dimension(200, 300));//todo check size being proper later
        setPreferredSize(new Dimension(250, 400));
        getTableHeader().setReorderingAllowed(false); //https://stackoverflow.com/questions/17641123/jtable-disable-user-column-dragging
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        if (row % 2 == 0) c.setBackground(Color.WHITE);
        else c.setBackground(Color.cyan);
        return c;
    }

    void update(String[][] boardStats) {
//https://stackoverflow.com/questions/3179136/jtable-how-to-refresh-table-model-after-insert-delete-or-update-the-data
        data = boardStats;
        repaint();
        System.out.println("updating board");
    }
}
