package com.ran.interaction.panels;

import com.ran.interaction.observer.EmptyObserver;
import com.ran.interaction.observer.Observer;
import com.ran.interaction.observer.Publisher;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public class GeneralPanel extends JPanel implements Publisher {

    public static final String SAVE = "save";
    
    public GeneralPanel() {
        initComponents();
        initCustomComponents();
        initObservers();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelProblemName = new javax.swing.JLabel();
        labelTimeLimit = new javax.swing.JLabel();
        labelMemoryLimit = new javax.swing.JLabel();
        textFieldMemoryLimit = new javax.swing.JTextField();
        textFieldTimeLimit = new javax.swing.JTextField();
        textFieldProblemName = new javax.swing.JTextField();
        buttonSave = new javax.swing.JButton();

        labelProblemName.setText("Problem name:");

        labelTimeLimit.setText("Time limit (ms):");

        labelMemoryLimit.setText("Memory limit (MB):");

        buttonSave.setText("Save");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelMemoryLimit)
                    .addComponent(labelTimeLimit)
                    .addComponent(labelProblemName))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textFieldProblemName)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 142, Short.MAX_VALUE)
                        .addComponent(buttonSave))
                    .addComponent(textFieldTimeLimit)
                    .addComponent(textFieldMemoryLimit))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelProblemName)
                    .addComponent(textFieldProblemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTimeLimit)
                    .addComponent(textFieldTimeLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelMemoryLimit)
                    .addComponent(textFieldMemoryLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonSave)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonSave;
    private javax.swing.JLabel labelMemoryLimit;
    private javax.swing.JLabel labelProblemName;
    private javax.swing.JLabel labelTimeLimit;
    private javax.swing.JTextField textFieldMemoryLimit;
    private javax.swing.JTextField textFieldProblemName;
    private javax.swing.JTextField textFieldTimeLimit;
    // End of variables declaration//GEN-END:variables

    private Map<String, Observer> observers = new HashMap<>();
    
    private void initCustomComponents() {
        buttonSave.addActionListener(event -> getObserver(SAVE).notify(SAVE, null));
    }
    
    @Override
    public List<String> getAvailableIds() {
        return Arrays.asList(SAVE);
    }
    
    @Override
    public void subscribe(String id, Observer observer) {
        observers.put(id, observer);
    }

    @Override
    public Observer getObserver(String id) {
        return observers.getOrDefault(id, EmptyObserver.getInstanse());
    }
    
    public String getProblemName() {
        return textFieldProblemName.getText();
    }
    
    public void setProblemName(String problemName) {
        textFieldProblemName.setText(problemName);
    }
    
    public Integer getTimeLimit() {
        return tryConvertToInt(textFieldTimeLimit.getText());
    }
    
    public void setTimeLimit(Integer timeLimit) {
        textFieldTimeLimit.setText(timeLimit.toString());
    }
    
    public Integer getMemoryLimit() {
        return tryConvertToInt(textFieldMemoryLimit.getText());
    }
    
    public void setMemoryLimit(Integer memoryLimit) {
        textFieldMemoryLimit.setText(memoryLimit.toString());
    }
    
    private Integer tryConvertToInt(String line) {
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
    
}
