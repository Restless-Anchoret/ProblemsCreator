package com.ran.interaction.panels;

import com.ran.interaction.components.SelectItem;
import com.ran.interaction.observer.EmptyObserver;
import com.ran.interaction.observer.Observer;
import com.ran.interaction.observer.Publisher;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public class AuthorDecisionsPanel extends JPanel implements Publisher {

    public static final String ADD = "add_author_decision";
    public static final String VIEW_CODE = "view_author_decision_code";
    public static final String SUBMIT = "submit_author_decision";
    public static final String DELETE = "delete_author_decision";
    public static final String UPDATE = "update_author_decisions";
    public static final String CREATE_ANSWERS = "create_answers";
    
    private static final String NUMBER = "Number";
    private static final String DECISION = "Decision";
    private static final String TITLE = "Title";
    private static final String COMPILATOR = "Compilator";
    
    private static final Object[] TABLE_HEADERS = {
        NUMBER, DECISION, TITLE, COMPILATOR
    };
    
    public AuthorDecisionsPanel() {
        initComponents();
        initCustomComponents();
        initObservers();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonAdd = new javax.swing.JButton();
        buttonViewCode = new javax.swing.JButton();
        buttonSubmit = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        buttonUpdate = new javax.swing.JButton();
        labelTestGroup = new javax.swing.JLabel();
        comboBoxTestGroup = new com.ran.interaction.components.PresentationComboBox();
        buttonCreateAnswers = new javax.swing.JButton();
        tableAuthorDecisions = new com.ran.interaction.components.RowsTable();

        buttonAdd.setText("Add");

        buttonViewCode.setText("View code");

        buttonSubmit.setText("Submit");

        buttonDelete.setText("Delete");

        buttonUpdate.setText("Update");

        labelTestGroup.setText("Test group:");

        buttonCreateAnswers.setText("Create answers");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableAuthorDecisions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonViewCode)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonSubmit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonUpdate))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelTestGroup)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboBoxTestGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonCreateAnswers)
                        .addGap(0, 165, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonAdd)
                    .addComponent(buttonViewCode)
                    .addComponent(buttonSubmit)
                    .addComponent(buttonDelete)
                    .addComponent(buttonUpdate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(comboBoxTestGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelTestGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(buttonCreateAnswers))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableAuthorDecisions, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonCreateAnswers;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonSubmit;
    private javax.swing.JButton buttonUpdate;
    private javax.swing.JButton buttonViewCode;
    private com.ran.interaction.components.PresentationComboBox comboBoxTestGroup;
    private javax.swing.JLabel labelTestGroup;
    private com.ran.interaction.components.RowsTable tableAuthorDecisions;
    // End of variables declaration//GEN-END:variables

    private Map<String, Observer> observers = new HashMap<>();
    
    private void initCustomComponents() {
        buttonAdd.addActionListener(event -> getObserver(ADD).notify(ADD, null));
        buttonViewCode.addActionListener(event -> callObserverIfRowIsSelected(VIEW_CODE));
        buttonSubmit.addActionListener(event -> callObserverIfRowIsSelected(SUBMIT));
        buttonDelete.addActionListener(event -> callObserverIfRowIsSelected(DELETE));
        buttonUpdate.addActionListener(event -> getObserver(UPDATE).notify(UPDATE, null));
        buttonCreateAnswers.addActionListener(event -> callObserverIfRowIsSelected(CREATE_ANSWERS));
    }
    
    private void callObserverIfRowIsSelected(String id) {
        if (tableAuthorDecisions.getSelectedIdentifier() != null) {
            getObserver(id).notify(id, tableAuthorDecisions.getSelectedIdentifier());
        }
    }
    
    @Override
    public List<String> getAvailableIds() {
        return Arrays.asList(ADD, VIEW_CODE, SUBMIT, DELETE, UPDATE, CREATE_ANSWERS);
    }
    
    @Override
    public void subscribe(String id, Observer observer) {
        observers.put(id, observer);
    }

    @Override
    public Observer getObserver(String id) {
        return observers.getOrDefault(id, EmptyObserver.getInstanse());
    }
    
    public String getSelectedTestGroupType() {
        return comboBoxTestGroup.getSelectedValue();
    }
    
    public void setTestGroupItems(List<SelectItem> items) {
        comboBoxTestGroup.setSelectItems(items);
    }
    
    public void setTableContent(Object[][] tableContent) {
        tableAuthorDecisions.setTableContent(tableContent, TABLE_HEADERS);
    }
    
}
