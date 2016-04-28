package com.ran.interaction.panels;

import com.ran.interaction.observer.EmptyObserver;
import com.ran.interaction.observer.Observer;
import com.ran.interaction.observer.Publisher;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public class SubmissionsPanel extends JPanel implements Publisher {

    public static final String ADD = "add_submission";
    public static final String RESUBMIT = "resubmit_submission";
    public static final String DELETE = "delete_submission";
    public static final String UPDATE = "update_submissions";
    public static final String VIEW_CODE = "view_submission_code";
    
    public static final String DELETING_MESSAGE = "Are you sure, you want to delete this submission?";
    public static final String DELETING_TITLE = "Deleting";
    
    private static final String NUMBER = "Number";
    private static final String PROBLEM = "Problem";
    private static final String EVALUATION = "Evaluation type";
    private static final String COMPILATOR = "Compilator";
    private static final String VERDICT = "Verdict";
    private static final String DECISION_TIME = "Decision time";
    
    private static final Object[] TABLE_HEADERS = {
        NUMBER, PROBLEM, EVALUATION, COMPILATOR, VERDICT, DECISION_TIME
    };
    
    public SubmissionsPanel() {
        initComponents();
        initCustomComponents();
        setTableContent(new Object[][] { });
        initObservers();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonAdd = new javax.swing.JButton();
        buttonResubmit = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        buttonUpdate = new javax.swing.JButton();
        buttonViewCode = new javax.swing.JButton();
        tableSubmissions = new com.ran.interaction.components.RowsTable();

        buttonAdd.setText("Add");

        buttonResubmit.setText("Resubmit");

        buttonDelete.setText("Delete");

        buttonUpdate.setText("Update");

        buttonViewCode.setText("View code");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableSubmissions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonResubmit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonViewCode)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 280, Short.MAX_VALUE)
                        .addComponent(buttonUpdate)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonAdd)
                    .addComponent(buttonResubmit)
                    .addComponent(buttonDelete)
                    .addComponent(buttonUpdate)
                    .addComponent(buttonViewCode))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableSubmissions, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonResubmit;
    private javax.swing.JButton buttonUpdate;
    private javax.swing.JButton buttonViewCode;
    private com.ran.interaction.components.RowsTable tableSubmissions;
    // End of variables declaration//GEN-END:variables

    private Map<String, Observer> observers = new HashMap<>();
    
    private void initCustomComponents() {
        buttonAdd.addActionListener(event -> getObserver(ADD).notify(ADD, null));
        buttonResubmit.addActionListener(event -> callObserverIfRowIsSelected(RESUBMIT));
        buttonDelete.addActionListener(event -> callObserverIfRowIsSelected(DELETE));
        buttonViewCode.addActionListener(event -> callObserverIfRowIsSelected(VIEW_CODE));
        buttonUpdate.addActionListener(event -> getObserver(UPDATE).notify(UPDATE, null));
    }
    
    private void callObserverIfRowIsSelected(String id) {
        if (tableSubmissions.getSelectedIdentifier() != null) {
            getObserver(id).notify(id, tableSubmissions.getSelectedIdentifier());
        }
    }
    
    @Override
    public List<String> getAvailableIds() {
        return Arrays.asList(ADD, RESUBMIT, DELETE, VIEW_CODE, UPDATE);
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
        tableSubmissions.setTableContent(content, TABLE_HEADERS);
    }
    
}
