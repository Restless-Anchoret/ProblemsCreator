package com.ran.interaction.windows;

import com.ran.interaction.observer.EmptyObserver;
import com.ran.interaction.observer.Observer;
import com.ran.interaction.observer.Publisher;
import javax.swing.JDialog;
import java.awt.Frame;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmissionResultDialog extends JDialog implements Publisher {

    private static final String SUBMISSION_PREFIX = "Submission: ";
    private static final String PROBLEM_PREFIX = "Problem: ";
    private static final String EVALUATION_SYSTEM_PREFIX = "Evaluation system: ";
    private static final String COMPILATOR_PREFIX = "Compilator: ";
    private static final String VERDICT_PREFIX = "Verdict: ";
    public static final String VERDICT_WAITING = "Waiting (click Update to check results)";
    
    public static final String UPDATE = "update";
    public static final String CLOSE = "close";
    
    private static final String NUMBER = "Number";
    private static final String TEST_GROUP = "Test group";
    private static final String VERDICT = "Verdict";
    private static final String DECISION_TIME = "Decision time";
    
    private static final Object[] TABLE_HEADERS = {
        NUMBER, TEST_GROUP, VERDICT, DECISION_TIME
    };
    
    public SubmissionResultDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initCustomComponents();
        initObservers();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelVerdict = new javax.swing.JLabel();
        tableVerdicts = new com.ran.interaction.components.RowsTable();
        buttonClose = new javax.swing.JButton();
        buttonUpdate = new javax.swing.JButton();
        labelSubmission = new javax.swing.JLabel();
        labelProblem = new javax.swing.JLabel();
        labelEvaluationSystem = new javax.swing.JLabel();
        labelCompilator = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Submission results");

        labelVerdict.setText("Verdict: ");

        buttonClose.setText("Close");

        buttonUpdate.setText("Update");

        labelSubmission.setText("Submission: ");

        labelProblem.setText("Problem: ");

        labelEvaluationSystem.setText("Evaluation system: ");

        labelCompilator.setText("Compilator: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableVerdicts, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(buttonClose))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelSubmission)
                            .addComponent(labelProblem))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelCompilator)
                            .addComponent(labelEvaluationSystem))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelVerdict)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonUpdate)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelSubmission)
                    .addComponent(labelEvaluationSystem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelProblem)
                    .addComponent(labelCompilator))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelVerdict)
                    .addComponent(buttonUpdate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableVerdicts, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonClose)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonClose;
    private javax.swing.JButton buttonUpdate;
    private javax.swing.JLabel labelCompilator;
    private javax.swing.JLabel labelEvaluationSystem;
    private javax.swing.JLabel labelProblem;
    private javax.swing.JLabel labelSubmission;
    private javax.swing.JLabel labelVerdict;
    private com.ran.interaction.components.RowsTable tableVerdicts;
    // End of variables declaration//GEN-END:variables

    private Map<String, Observer> observers = new HashMap<>();
    
    private void initCustomComponents() {
        buttonClose.addActionListener(event -> getObserver(CLOSE).notify(CLOSE, null));
        buttonUpdate.addActionListener(event -> getObserver(UPDATE).notify(UPDATE, null));
    }
    
    @Override
    public List<String> getAvailableIds() {
        return Arrays.asList(UPDATE, CLOSE);
    }
    
    @Override
    public void subscribe(String id, Observer observer) {
        observers.put(id, observer);
    }

    @Override
    public Observer getObserver(String id) {
        return observers.getOrDefault(id, EmptyObserver.getInstanse());
    }
    
    public void setSubmission(String submission) {
        labelSubmission.setText(SUBMISSION_PREFIX + submission);
    }
    
    public void setProblem(String problem) {
        labelProblem.setText(PROBLEM_PREFIX + problem);
    }
    
    public void setEvaluationSystem(String evaluationSystem) {
        labelEvaluationSystem.setText(EVALUATION_SYSTEM_PREFIX + evaluationSystem);
    }
    
    public void setCompilator(String compilator) {
        labelCompilator.setText(COMPILATOR_PREFIX + compilator);
    }
    
    public void setVerdict(String verdict) {
        labelVerdict.setText(VERDICT_PREFIX + verdict);
    }
    
    public void setTableContent(Object[][] content) {
        tableVerdicts.setTableContent(content, TABLE_HEADERS);
    }
    
}
