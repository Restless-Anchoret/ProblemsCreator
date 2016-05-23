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

public class GeneratorsPanel extends JPanel implements Publisher {

    public static final String ADD = "add_generator";
    public static final String VIEW_CODE = "view_generator_code";
    public static final String DELETE = "delete_generator";
    public static final String UPDATE = "update_generators";
    public static final String GENERATE_TESTS = "generate_tests";
    
    public static final String DELETING_GENERATOR_MESSAGE = "Are you sure you want to delete this generator?";
    public static final String DELETING_GENERATOR_TITLE = "Deleting generator";
    
    private static final String NUMBER = "Number";
    private static final String GENERATOR = "Generator";
    
    private static final Object[] TABLE_HEADERS = {
        NUMBER, GENERATOR
    };
    
    public GeneratorsPanel() {
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
        tableGenerators = new com.ran.interaction.components.RowsTable();
        labelTestGroup = new javax.swing.JLabel();
        comboBoxTestGroup = new com.ran.interaction.components.PresentationComboBox();
        labelQuantity = new javax.swing.JLabel();
        textFieldQuantity = new javax.swing.JTextField();
        textFieldArguments = new javax.swing.JTextField();
        textFieldRandomSeed = new javax.swing.JTextField();
        labelArguments = new javax.swing.JLabel();
        labelRandomSeed = new javax.swing.JLabel();
        buttonGenerate = new javax.swing.JButton();

        buttonAdd.setText("Add");

        buttonViewCode.setText("View code");

        buttonDelete.setText("Delete");

        buttonUpdate.setText("Update");

        labelTestGroup.setText("Test group:");

        labelQuantity.setText("Tests quantity to generate:");

        textFieldQuantity.setText("1");

        textFieldRandomSeed.setText("0");

        labelArguments.setText("Generator arguments:");

        labelRandomSeed.setText("Random seed:");

        buttonGenerate.setText("Generate tests");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonViewCode)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 246, Short.MAX_VALUE)
                        .addComponent(buttonUpdate))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tableGenerators, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelTestGroup)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboBoxTestGroup, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelQuantity)
                                    .addComponent(labelArguments)
                                    .addComponent(labelRandomSeed))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textFieldArguments)
                                    .addComponent(textFieldQuantity)
                                    .addComponent(textFieldRandomSeed)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(buttonGenerate)
                                .addGap(0, 0, Short.MAX_VALUE)))))
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
                    .addComponent(tableGenerators, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(labelTestGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboBoxTestGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textFieldQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelQuantity))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textFieldArguments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelArguments))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textFieldRandomSeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelRandomSeed))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonGenerate)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonGenerate;
    private javax.swing.JButton buttonUpdate;
    private javax.swing.JButton buttonViewCode;
    private com.ran.interaction.components.PresentationComboBox comboBoxTestGroup;
    private javax.swing.JLabel labelArguments;
    private javax.swing.JLabel labelQuantity;
    private javax.swing.JLabel labelRandomSeed;
    private javax.swing.JLabel labelTestGroup;
    private com.ran.interaction.components.RowsTable tableGenerators;
    private javax.swing.JTextField textFieldArguments;
    private javax.swing.JTextField textFieldQuantity;
    private javax.swing.JTextField textFieldRandomSeed;
    // End of variables declaration//GEN-END:variables

    private Map<String, Observer> observers = new HashMap<>();
    
    private void initCustomComponents() {
        buttonAdd.addActionListener(event -> getObserver(ADD).notify(ADD, null));
        buttonViewCode.addActionListener(event -> callObserverIfRowIsSelected(VIEW_CODE));
        buttonDelete.addActionListener(event -> callObserverIfRowIsSelected(DELETE));
        buttonUpdate.addActionListener(event -> getObserver(UPDATE).notify(UPDATE, null));
        buttonGenerate.addActionListener(event -> callObserverIfRowIsSelected(GENERATE_TESTS));
    }
    
    private void callObserverIfRowIsSelected(String id) {
        if (tableGenerators.getSelectedIdentifier() != null) {
            getObserver(id).notify(id, tableGenerators.getSelectedIdentifier());
        }
    }
    
    @Override
    public List<String> getAvailableIds() {
        return Arrays.asList(ADD, VIEW_CODE, DELETE, UPDATE, GENERATE_TESTS);
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
        tableGenerators.setTableContent(tableContent, TABLE_HEADERS);
    }
    
    public void setTestGroupItems(List<SelectItem> items) {
        comboBoxTestGroup.setSelectItems(items);
    }
    
}
