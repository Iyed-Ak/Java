package com.bibliotheque.ui.components;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomTable extends JTable {

    private static final Color HEADER_BACKGROUND = new Color(45, 45, 45);
    private static final Color HEADER_FOREGROUND = Color.BLACK;
    private static final Color ROW_ALTERNATE = new Color(245, 245, 245);
    private static final Color SELECTION_BACKGROUND = new Color(30, 144, 255);
    private static final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public CustomTable(String[] columnNames) {
        this(new DefaultTableModel(columnNames, 0));
    }

    public CustomTable(DefaultTableModel model) {
        super(model);
        setupTableAppearance();
    }

    public void clearTable() {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.setRowCount(0);
    }

    public void addRow(Object[] rowData) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(rowData);
    }

    public int getSelectedRowIndex() {
        return getSelectedRow();
    }

    public void setFilter(String text, int[] columns) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) getModel());
        setRowSorter(sorter);

        if (text == null || text.trim().isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }

        RowFilter<DefaultTableModel, Object> rf = new RowFilter<>() {
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                for (int col : columns) {
                    Object value = entry.getValue(col);
                    if (value != null && value.toString().toLowerCase().contains(text.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            }
        };
        sorter.setRowFilter(rf);
    }

    private void setupTableAppearance() {
        setFont(TABLE_FONT);
        setRowHeight(30);
        setIntercellSpacing(new Dimension(10, 5));
        setFillsViewportHeight(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setShowGrid(false);
        setShowHorizontalLines(true);
        setGridColor(new Color(230, 230, 230));

        JTableHeader header = getTableHeader();
        header.setBackground(HEADER_BACKGROUND);
        header.setForeground(HEADER_FOREGROUND);
        header.setFont(HEADER_FONT);
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);

        setSelectionBackground(SELECTION_BACKGROUND);
        setSelectionForeground(Color.WHITE);

        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : ROW_ALTERNATE);
                    c.setForeground(Color.BLACK);
                } else {
                    c.setForeground(Color.WHITE);
                }
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c; 
            }
        });
    }
}
