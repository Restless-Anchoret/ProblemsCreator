package com.ran.interaction.windows;

import com.ran.interaction.components.SelectItem;
import com.ran.interaction.observer.EmptyObserver;
import com.ran.interaction.observer.Observer;
import com.ran.interaction.observer.Publisher;
import javax.swing.JDialog;
import java.awt.Frame;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmissionCreationDialog extends JDialog implements Publisher {

    public static final String FILE_PATH_MESSAGE = "You should choose file path";
    public static final String FILE_PATH_TITLE = "File path";
    public static final String FILE_LOADING_ERROR_MESSAGE = "Error while loading file";
    public static final String FILE_LOADING_ERROR_TITLE = "Loading error";
    
    public static final String SUBMIT = "submit";
    public static final String CANCEL = "cancel";
    
    public SubmissionCreationDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initCustomComponents();
        initObservers();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelProblem = new javax.swing.JLabel();
        labelEvaluationSystem = new javax.swing.JLabel();
        labelCompilator = new javax.swing.JLabel();
        labelFile = new javax.swing.JLabel();
        buttonSubmit = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        comboBoxProblem = new com.ran.interaction.components.PresentationComboBox();
        comboBoxEvaluationSystem = new com.ran.interaction.components.PresentationComboBox();
        comboBoxCompilator = new com.ran.interaction.components.PresentationComboBox();
        fileBrowser = new com.ran.interaction.components.FileBrowser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New submission");

        labelProblem.setText("Problem:");

        labelEvaluationSystem.setText("Evaluation system:");

        labelCompilator.setText("Compilator:");

        labelFile.setText("File:");

        buttonSubmit.setText("Submit");

        buttonCancel.setText("Cancel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelCompilator, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelEvaluationSystem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelProblem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboBoxProblem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboBoxEvaluationSystem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboBoxCompilator, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(buttonSubmit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonCancel))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(labelFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fileBrowser, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelProblem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboBoxProblem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelEvaluationSystem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboBoxEvaluationSystem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelCompilator, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboBoxCompilator, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fileBrowser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonCancel)
                    .addComponent(buttonSubmit))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonSubmit;
    private com.ran.interaction.components.PresentationComboBox comboBoxCompilator;
    private com.ran.interaction.components.PresentationComboBox comboBoxEvaluationSystem;
    private com.ran.interaction.components.PresentationComboBox comboBoxProblem;
    private com.ran.interaction.components.FileBrowser fileBrowser;
    private javax.swing.JLabel labelCompilator;
    private javax.swing.JLabel labelEvaluationSystem;
    private javax.swing.JLabel labelFile;
    private javax.swing.JLabel labelProblem;
    // End of variables declaration//GEN-END:variables

    private Map<String, Observer> observers = new HashMap<>();
    
    private void initCustomComponents() {
        buttonSubmit.addActionListener(event -> getObserver(SUBMIT).notify(SUBMIT, null));
        buttonCancel.addActionListener(event -> getObserver(CANCEL).notify(CANCEL, null));
    }
    
    @Override
    public List<String> getAvailableIds() {
        return Arrays.asList(SUBMIT, CANCEL);
    }
    
    @Override
    public void subscribe(String id, Observer observer) {
        observers.put(id, observer);
    }

    @Override
    public Observer getObserver(String id) {
        return observers.getOrDefault(id, EmptyObserver.getInstanse());
    }
    
    public void setProblemNamesSelectItems(List<SelectItem> problemNames) {
        comboBoxProblem.setSelectItems(problemNames);
        comboBoxProblem.setSelectedIndex(0);
    }
    
    public void setEvaluationSystemsSelectItems(List<SelectItem> evaluationSystemNames) {
        comboBoxEvaluationSystem.setSelectItems(evaluationSystemNames);
        comboBoxEvaluationSystem.setSelectedIndex(0);
    }
    
    public void setCompilatorSelectItems(List<SelectItem> compilatorNames) {
        comboBoxCompilator.setSelectItems(compilatorNames);
        comboBoxCompilator.setSelectedIndex(0);
    }
    
    public String getProblemFolder() {
        return comboBoxProblem.getSelectedValue();
    }
    
    public String getEvaluationSystem() {
        return comboBoxEvaluationSystem.getSelectedValue();
    }
    
    public String getCompilator() {
        return comboBoxCompilator.getSelectedValue();
    }
    
    public Path getFilePath() {
        return fileBrowser.getFilePath();
    }
    
}
