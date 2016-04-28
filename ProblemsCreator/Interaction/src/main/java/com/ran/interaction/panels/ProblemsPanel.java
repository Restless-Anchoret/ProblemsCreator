package com.ran.interaction.panels;

import com.ran.interaction.observer.EmptyObserver;
import com.ran.interaction.observer.Observer;
import com.ran.interaction.observer.Publisher;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public class ProblemsPanel extends JPanel implements Publisher {
    
    public static final String ADD = "add_problem";
    public static final String EDIT = "edit_problem";
    public static final String DELETE = "delete_problem";
    public static final String UPDATE = "update_problems";
    
    public static final String DELETING_MESSAGE = "Are you sure, you want to delete this problem?";
    public static final String DELETING_TITLE = "Deleting";
    
    private static final String NUMBER = "Number";
    private static final String NAME = "Name";
    private static final String TIME_LIMIT = "Time limit";
    private static final String MEMORY_LIMIT = "Memory limit";
    private static final String CHECKER_TYPE = "Checker type";
    
    private static final Object[] TABLE_HEADERS = {
        NUMBER, NAME, TIME_LIMIT, MEMORY_LIMIT, CHECKER_TYPE
    };
    
    public ProblemsPanel() {
        initComponents();
        initCustomComponents();
        setTableContent(new Object[][] { });
        initObservers();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonAdd = new javax.swing.JButton();
        buttonEdit = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        buttonUpdate = new javax.swing.JButton();
        tableProblems = new com.ran.interaction.components.RowsTable();

        buttonAdd.setText("Add");

        buttonEdit.setText("Edit");

        buttonDelete.setText("Delete");

        buttonUpdate.setText("Update");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableProblems, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 435, Short.MAX_VALUE)
                        .addComponent(buttonUpdate)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonAdd)
                    .addComponent(buttonEdit)
                    .addComponent(buttonDelete)
                    .addComponent(buttonUpdate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableProblems, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonEdit;
    private javax.swing.JButton buttonUpdate;
    private com.ran.interaction.components.RowsTable tableProblems;
    // End of variables declaration//GEN-END:variables

    private Map<String, Observer> observers = new HashMap<>();
    
    private void initCustomComponents() {
        buttonAdd.addActionListener(event -> getObserver(ADD).notify(ADD, null));
        buttonEdit.addActionListener(event -> callObserverIfRowIsSelected(EDIT));
        buttonDelete.addActionListener(event -> callObserverIfRowIsSelected(DELETE));
        buttonUpdate.addActionListener(event -> getObserver(UPDATE).notify(UPDATE, null));
    }
    
    private void callObserverIfRowIsSelected(String id) {
        if (tableProblems.getSelectedIdentifier() != null) {
            getObserver(id).notify(id, tableProblems.getSelectedIdentifier());
        }
    }
    
    @Override
    public List<String> getAvailableIds() {
        return Arrays.asList(ADD, EDIT, DELETE, UPDATE);
    }
    
    @Override
    public void subscribe(String id, Observer observer) {
        observers.put(id, observer);
    }

    @Override
    public Observer getObserver(String id) {
        return observers.getOrDefault(id, EmptyObserver.getInstanse());
    }
    
    public final void setTableContent(Object[][] content) {
        tableProblems.setTableContent(content, TABLE_HEADERS);
    }
    
}
