package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ScoreBoard extends JTable {
    //https://www.youtube.com/watch?v=iMBfneE2Ztg&t=61s
    private static final String[] columns = new String[]{"userName", "status", "score"};
    private String[][] data;
    private final int WIDTH = 250;
    private final int HEIGHT = 300;

    ScoreBoard(String[][] boardStats) {
        super(boardStats, columns);
        data = boardStats;
        setPreferredScrollableViewportSize(new Dimension(WIDTH, HEIGHT));
        updateSize();
        getTableHeader().setReorderingAllowed(false); //https://stackoverflow.com/questions/17641123/jtable-disable-user-column-dragging
    }

    private void updateSize() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        if (getModel().getRowCount() * 25 > HEIGHT)
            setPreferredSize(new Dimension(WIDTH, getModel().getRowCount() * 25));
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
//https://stackoverflow.com/questions/34174107/java-error-javax-swing-jtable1-cannot-be-cast-to-javax-swing-table-defaulttab
        System.out.println("upgrading board");

        data = boardStats;
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columns);
        for (String[] stat : boardStats) tableModel.addRow(stat);
        setModel(tableModel);

        updateSize();
    }
}
