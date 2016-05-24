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

public class CheckersPanel extends JPanel implements Publisher {

    public static final String CHANGE_CHECKER = "change_checker";
    public static final String VIEW_CODE = "view_checker_code";
    public static final String RECOMPILE = "recompile_checker";
    
    public CheckersPanel() {
        initComponents();
        initCustomComponents();
        initObservers();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelCheckerType = new javax.swing.JLabel();
        comboBoxCheckerType = new com.ran.interaction.components.PresentationComboBox();
        buttonViewCode = new javax.swing.JButton();
        buttonRecompile = new javax.swing.JButton();

        labelCheckerType.setText("Checker type:");

        buttonViewCode.setText("View code");

        buttonRecompile.setText("Recompile");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelCheckerType)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboBoxCheckerType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonViewCode)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonRecompile)))
                .addContainerGap(104, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(comboBoxCheckerType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelCheckerType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonViewCode)
                    .addComponent(buttonRecompile))
                .addContainerGap(230, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonRecompile;
    private javax.swing.JButton buttonViewCode;
    private com.ran.interaction.components.PresentationComboBox comboBoxCheckerType;
    private javax.swing.JLabel labelCheckerType;
    // End of variables declaration//GEN-END:variables

    private Map<String, Observer> observers = new HashMap<>();
    
    private void initCustomComponents() {
        comboBoxCheckerType.getComboBox().addItemListener(event -> getObserver(CHANGE_CHECKER).notify(CHANGE_CHECKER, null));
        buttonViewCode.addActionListener(event -> getObserver(VIEW_CODE).notify(VIEW_CODE, null));
        buttonRecompile.addActionListener(event -> getObserver(RECOMPILE).notify(RECOMPILE, null));
    }
    
    @Override
    public List<String> getAvailableIds() {
        return Arrays.asList(CHANGE_CHECKER, VIEW_CODE, RECOMPILE);
    }
    
    @Override
    public void subscribe(String id, Observer observer) {
        observers.put(id, observer);
    }

    @Override
    public Observer getObserver(String id) {
        return observers.getOrDefault(id, EmptyObserver.getInstanse());
    }
    
    public void setCheckerTypeItems(List<SelectItem> checkerTypeItems) {
        comboBoxCheckerType.setSelectItems(checkerTypeItems);
    }
    
    public String getSelectedCheckerType() {
        return comboBoxCheckerType.getSelectedValue();
    }
    
    public void setSelectedCheckerType(String checkerType) {
        comboBoxCheckerType.setSelectedValue(checkerType);
    }
    
}
