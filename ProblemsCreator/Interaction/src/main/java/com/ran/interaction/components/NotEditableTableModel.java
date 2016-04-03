package com.ran.interaction.components;

import javax.swing.table.DefaultTableModel;

public class NotEditableTableModel extends DefaultTableModel {

    public NotEditableTableModel(Object[][] content, Object[] headers) {
        super(content, headers);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
    
    
}