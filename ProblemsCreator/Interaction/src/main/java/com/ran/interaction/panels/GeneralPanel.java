package com.ran.interaction.panels;

import com.ran.interaction.observer.EmptyObserver;
import com.ran.interaction.observer.Observer;
import com.ran.interaction.observer.Publisher;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public class GeneralPanel extends JPanel implements Publisher {
    
    public static final String FILE_PATH_MESSAGE = "You should choose file path";
    public static final String FILE_PATH_TITLE = "File path";
    public static final String FILE_LOADING_ERROR_MESSAGE = "Error while loading file";
    public static final String FILE_LOADING_ERROR_TITLE = "Loading error";
    
    public static final String SAVE = "save";
    public static final String LOAD_STATEMENT = "load_statement";
    
    private static final String STATEMENT_FILE_PREFIX = "Statement file: ";
    private static final String NONE_STATEMENT = "(None)";
    
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
        labelStatement = new javax.swing.JLabel();
        fileBrowser = new com.ran.interaction.components.FileBrowser();
        labelLoad = new javax.swing.JLabel();
        buttonLoad = new javax.swing.JButton();

        labelProblemName.setText("Problem name:");

        labelTimeLimit.setText("Time limit (ms):");

        labelMemoryLimit.setText("Memory limit (MB):");

        buttonSave.setText("Save");

        labelStatement.setText("Statement file:");

        labelLoad.setText("Load statement:");

        buttonLoad.setText("Load");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelMemoryLimit)
                            .addComponent(labelTimeLimit)
                            .addComponent(labelProblemName)
                            .addComponent(labelLoad))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldProblemName)
                            .addComponent(textFieldTimeLimit)
                            .addComponent(textFieldMemoryLimit)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(buttonSave))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(fileBrowser, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonLoad))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelStatement)
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelStatement)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttonLoad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(labelLoad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(fileBrowser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonLoad;
    private javax.swing.JButton buttonSave;
    private com.ran.interaction.components.FileBrowser fileBrowser;
    private javax.swing.JLabel labelLoad;
    private javax.swing.JLabel labelMemoryLimit;
    private javax.swing.JLabel labelProblemName;
    private javax.swing.JLabel labelStatement;
    private javax.swing.JLabel labelTimeLimit;
    private javax.swing.JTextField textFieldMemoryLimit;
    private javax.swing.JTextField textFieldProblemName;
    private javax.swing.JTextField textFieldTimeLimit;
    // End of variables declaration//GEN-END:variables

    private Map<String, Observer> observers = new HashMap<>();
    
    private void initCustomComponents() {
        buttonSave.addActionListener(event -> getObserver(SAVE).notify(SAVE, null));
        buttonLoad.addActionListener(event -> getObserver(LOAD_STATEMENT).notify(LOAD_STATEMENT, null));
    }
    
    @Override
    public List<String> getAvailableIds() {
        return Arrays.asList(SAVE, LOAD_STATEMENT);
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
    
    public void setStatementFileDescription(String description) {
        if (description == null || description.isEmpty()) {
            description = NONE_STATEMENT;
        }
        labelStatement.setText(STATEMENT_FILE_PREFIX + description);
    }
    
    public Path getNewStatementPath() {
        return fileBrowser.getFilePath();
    }
    
    private Integer tryConvertToInt(String line) {
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
    
}
