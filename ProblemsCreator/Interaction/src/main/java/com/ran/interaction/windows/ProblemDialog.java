package com.ran.interaction.windows;

import com.ran.interaction.panels.AuthorDecisionsPanel;
import com.ran.interaction.panels.CheckersPanel;
import com.ran.interaction.panels.GeneralPanel;
import com.ran.interaction.panels.GeneratorsPanel;
import com.ran.interaction.panels.TestsPanel;
import com.ran.interaction.panels.ValidatorsPanel;
import javax.swing.JDialog;
import java.awt.Frame;

public class ProblemDialog extends JDialog {

    private static final String GENERAL = "General";
    private static final String TESTS = "Tests";
    private static final String GENERATORS = "Generators";
    private static final String VALIDATORS = "Validators";
    private static final String CHECKERS = "Checkers";
    private static final String AUTHOR_DECISIONS = "AuthorDecisions";
    
    public ProblemDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initCustomComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Problem settings");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

    private GeneralPanel generalPanel = new GeneralPanel();
    private TestsPanel testsPanel = new TestsPanel();
    private GeneratorsPanel generatorsPanel = new GeneratorsPanel();
    private ValidatorsPanel validatorsPanel = new ValidatorsPanel();
    private CheckersPanel checkersPanel = new CheckersPanel();
    private AuthorDecisionsPanel authorDecisionsPanel = new AuthorDecisionsPanel();
    
    public void initCustomComponents() {
        tabbedPane.add(generalPanel);
        tabbedPane.setTitleAt(0, GENERAL);
        tabbedPane.add(testsPanel);
        tabbedPane.setTitleAt(1, TESTS);
        tabbedPane.add(generatorsPanel);
        tabbedPane.setTitleAt(2, GENERATORS);
        tabbedPane.add(validatorsPanel);
        tabbedPane.setTitleAt(3, VALIDATORS);
        tabbedPane.add(checkersPanel);
        tabbedPane.setTitleAt(4, CHECKERS);
        tabbedPane.add(authorDecisionsPanel);
        tabbedPane.setTitleAt(5, AUTHOR_DECISIONS);
    }

    public GeneralPanel getGeneralPanel() {
        return generalPanel;
    }

    public TestsPanel getTestsPanel() {
        return testsPanel;
    }

    public GeneratorsPanel getGeneratorsPanel() {
        return generatorsPanel;
    }

    public ValidatorsPanel getValidatorsPanel() {
        return validatorsPanel;
    }

    public CheckersPanel getCheckersPanel() {
        return checkersPanel;
    }

    public AuthorDecisionsPanel getAuthorDecisionsPanel() {
        return authorDecisionsPanel;
    }
    
}
