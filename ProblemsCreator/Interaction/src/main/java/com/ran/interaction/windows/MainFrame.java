package com.ran.interaction.windows;

import com.ran.interaction.panels.ProblemsPanel;
import com.ran.interaction.panels.SubmissionsPanel;
import javax.swing.JFrame;

public class MainFrame extends JFrame {

    private static final String SUBMISSIONS = "Submissions";
    private static final String PROBLEMS = "Problems";
    
    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        initCustomComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Problems Creator");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

    private SubmissionsPanel submissionsPanel;
    private ProblemsPanel problemsPanel;

    private void initCustomComponents() {
        submissionsPanel = new SubmissionsPanel();
        tabbedPane.add(submissionsPanel);
        tabbedPane.setTitleAt(0, SUBMISSIONS);
        problemsPanel = new ProblemsPanel();
        tabbedPane.add(problemsPanel);
        tabbedPane.setTitleAt(1, PROBLEMS);
    }
    
    public SubmissionsPanel getSubmissionsPanel() {
        return submissionsPanel;
    }

    public ProblemsPanel getProblemsPanel() {
        return problemsPanel;
    }
    
}
