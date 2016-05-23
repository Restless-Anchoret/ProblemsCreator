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

public class ValidatorsPanel extends JPanel implements Publisher {

    public static final String ADD = "add_validator";
    public static final String VIEW_CODE = "view_validator_code";
    public static final String DELETE = "delete_validator";
    public static final String UPDATE = "update_validators";
    public static final String VALIDATE_TESTS = "validate_tests";
    
    public static final String DELETING_VALIDATOR_MESSAGE = "Are you sure you want to delete this validator?";
    public static final String DELETING_VALIDATOR_TITLE = "Deleting validator";
    
    private static final String NUMBER = "Number";
    private static final String VALIDATOR = "Validator";
    
    private static final Object[] TABLE_HEADERS = {
        NUMBER, VALIDATOR
    };
    
    public ValidatorsPanel() {
        initComponents();
        initCustomComponents();
        initObservers();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonAdd = new javax.swing.JButton();
        buttonViewCode = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        buttonUpdate = new javax.swing.JButton();
        tableValidators = new com.ran.interaction.components.RowsTable();
        labelTestGroup = new javax.swing.JLabel();
        comboBoxTestGroup = new com.ran.interaction.components.PresentationComboBox();
        textFieldArguments = new javax.swing.JTextField();
        labelArguments = new javax.swing.JLabel();
        buttonValidate = new javax.swing.JButton();

        buttonAdd.setText("Add");

        buttonViewCode.setText("View code");

        buttonDelete.setText("Delete");

        buttonUpdate.setText("Update");

        labelTestGroup.setText("Test group:");

        labelArguments.setText("Validator arguments:");

        buttonValidate.setText("Validate tests");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tableValidators, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonValidate)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelArguments)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textFieldArguments, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelTestGroup)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboBoxTestGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonViewCode)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 283, Short.MAX_VALUE)
                        .addComponent(buttonUpdate)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonAdd)
                    .addComponent(buttonViewCode)
                    .addComponent(buttonDelete)
                    .addComponent(buttonUpdate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableValidators, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(labelTestGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboBoxTestGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textFieldArguments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelArguments))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonValidate)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonUpdate;
    private javax.swing.JButton buttonValidate;
    private javax.swing.JButton buttonViewCode;
    private com.ran.interaction.components.PresentationComboBox comboBoxTestGroup;
    private javax.swing.JLabel labelArguments;
    private javax.swing.JLabel labelTestGroup;
    private com.ran.interaction.components.RowsTable tableValidators;
    private javax.swing.JTextField textFieldArguments;
    // End of variables declaration//GEN-END:variables

    private Map<String, Observer> observers = new HashMap<>();
    
    private void initCustomComponents() {
        buttonAdd.addActionListener(event -> getObserver(ADD).notify(ADD, null));
        buttonViewCode.addActionListener(event -> callObserverIfRowIsSelected(VIEW_CODE));
        buttonDelete.addActionListener(event -> callObserverIfRowIsSelected(DELETE));
        buttonUpdate.addActionListener(event -> getObserver(UPDATE).notify(UPDATE, null));
        buttonValidate.addActionListener(event -> callObserverIfRowIsSelected(VALIDATE_TESTS));
    }
    
    private void callObserverIfRowIsSelected(String id) {
        if (tableValidators.getSelectedIdentifier() != null) {
            getObserver(id).notify(id, tableValidators.getSelectedIdentifier());
        }
    }
    
    @Override
    public List<String> getAvailableIds() {
        return Arrays.asList(ADD, VIEW_CODE, DELETE, UPDATE, VALIDATE_TESTS);
    }
    
    @Override
    public void subscribe(String id, Observer observer) {
        observers.put(id, observer);
    }

    @Override
    public Observer getObserver(String id) {
        return observers.getOrDefault(id, EmptyObserver.getInstanse());
    }
    
    public void setTableContent(Object[][] tableContent) {
        tableValidators.setTableContent(tableContent, TABLE_HEADERS);
    }
    
    public void setTestGroupItems(List<SelectItem> items) {
        comboBoxTestGroup.setSelectItems(items);
    }
    
}
