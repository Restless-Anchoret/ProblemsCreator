package com.ran.interaction.components;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class RowsTable extends JPanel {

    public RowsTable() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scrollPane.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

    public JTable getTable() {
        return table;
    }
    
    public String getSelectedIdentifier() {
        if (table.getSelectedRow() == -1) {
            return null;
        } else {
            return table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
        }
    }

    public void setTableContent(Object[][] content, Object[] headers) {
        table.setModel(new NotEditableTableModel(content, headers));
    }

    private static class NotEditableTableModel extends DefaultTableModel {

        public NotEditableTableModel(Object[][] content, Object[] headers) {
            super(content, headers);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

    }

}