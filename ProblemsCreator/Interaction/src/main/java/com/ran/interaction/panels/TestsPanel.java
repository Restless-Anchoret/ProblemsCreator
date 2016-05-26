package com.ran.interaction.panels;

import com.ran.interaction.components.SelectItem;
import com.ran.interaction.observer.EmptyObserver;
import com.ran.interaction.observer.Observer;
import com.ran.interaction.observer.Publisher;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public class TestsPanel extends JPanel implements Publisher {

    public static final String ADD = "add_test";
    public static final String VIEW_INPUT = "view_input_test";
    public static final String VIEW_ANSWER = "view_answer_test";
    public static final String DELETE = "delete_test";
    public static final String UPDATE = "update_test";
    public static final String SAVE_POINTS = "save_test";
    public static final String CHANGE_TEST_GROUP = "change_test_group";
    
    public static final String INCORRECT_POINTS_MESSAGE = "You should input points for test as number";
    public static final String INCORRECT_POINTS_TITLE = "Incorrect points";
    public static final String DELETING_TEST_MESSAGE = "Are you sure you want to delete this test?";
    public static final String DELETING_TEST_TITLE = "Deleting test";
    
    private static final String NUMBER = "Number";
    private static final String INPUT_FILE = "Input";
    private static final String ANSWER_FILE = "Answer";
    
    private static final Object[] TABLE_HEADERS = {
        NUMBER, INPUT_FILE, ANSWER_FILE
    };
    
    public TestsPanel() {
        initComponents();
        initCustomComponents();
        initObservers();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        comboBoxTestGroup = new com.ran.interaction.components.PresentationComboBox();
        labelTestGroup = new javax.swing.JLabel();
        buttonAdd = new javax.swing.JButton();
        buttonViewInput = new javax.swing.JButton();
        buttonViewAnswer = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        buttonUpdate = new javax.swing.JButton();
        tableTests = new com.ran.interaction.components.RowsTable();
        labelPoints = new javax.swing.JLabel();
        textFieldPoints = new javax.swing.JTextField();
        buttonSavePoints = new javax.swing.JButton();

        labelTestGroup.setText("Test group:");

        buttonAdd.setText("Add");

        buttonViewInput.setText("View input");

        buttonViewAnswer.setText("View answer");

        buttonDelete.setText("Delete");

        buttonUpdate.setText("Update");

        labelPoints.setText("Points for test:");

        buttonSavePoints.setText("Save points");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableTests, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonViewInput)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonViewAnswer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                        .addComponent(buttonUpdate))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelTestGroup)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboBoxTestGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelPoints)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textFieldPoints, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(buttonSavePoints)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(comboBoxTestGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelTestGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonAdd)
                    .addComponent(buttonViewInput)
                    .addComponent(buttonViewAnswer)
                    .addComponent(buttonDelete)
                    .addComponent(buttonUpdate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableTests, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPoints)
                    .addComponent(textFieldPoints, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSavePoints))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonSavePoints;
    private javax.swing.JButton buttonUpdate;
    private javax.swing.JButton buttonViewAnswer;
    private javax.swing.JButton buttonViewInput;
    private com.ran.interaction.components.PresentationComboBox comboBoxTestGroup;
    private javax.swing.JLabel labelPoints;
    private javax.swing.JLabel labelTestGroup;
    private com.ran.interaction.components.RowsTable tableTests;
    private javax.swing.JTextField textFieldPoints;
    // End of variables declaration//GEN-END:variables

    private Map<String, Observer> observers = new HashMap<>();
    
    private void initCustomComponents() {
        buttonAdd.addActionListener(event -> getObserver(ADD).notify(ADD, null));
        buttonViewInput.addActionListener(event -> callObserverIfRowIsSelected(VIEW_INPUT));
        buttonViewAnswer.addActionListener(event -> callObserverIfRowIsSelected(VIEW_ANSWER));
        buttonDelete.addActionListener(event -> callObserverIfRowIsSelected(DELETE));
        buttonUpdate.addActionListener(event -> getObserver(UPDATE).notify(UPDATE, null));
        ActionListener savePointsListener = (event -> getObserver(SAVE_POINTS).notify(SAVE_POINTS, null));
        buttonSavePoints.addActionListener(savePointsListener);
        textFieldPoints.addActionListener(savePointsListener);
        comboBoxTestGroup.getComboBox().addItemListener(event -> getObserver(CHANGE_TEST_GROUP)
                .notify(CHANGE_TEST_GROUP, comboBoxTestGroup.getSelectedValue()));
    }
    
    private void callObserverIfRowIsSelected(String id) {
        if (tableTests.getSelectedIdentifier() != null) {
            getObserver(id).notify(id, tableTests.getSelectedIdentifier());
        }
    }
    
    @Override
    public List<String> getAvailableIds() {
        return Arrays.asList(ADD, VIEW_INPUT, VIEW_ANSWER, DELETE, UPDATE,
                SAVE_POINTS, CHANGE_TEST_GROUP);
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
        tableTests.setTableContent(tableContent, TABLE_HEADERS);
    }
    
    public Integer getPointsForTest() {
        try {
            return Integer.parseInt(textFieldPoints.getText());
        } catch (NumberFormatException exception) {
            return null;
        }
    }
    
    public void setPointsForTest(Integer points) {
        textFieldPoints.setText(points.toString());
    }
    
}
