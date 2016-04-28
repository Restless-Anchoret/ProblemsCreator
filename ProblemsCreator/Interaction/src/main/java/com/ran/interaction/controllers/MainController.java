package com.ran.interaction.controllers;

import com.ran.filesystem.descriptor.ProblemDescriptor;
import com.ran.filesystem.descriptor.SubmissionDescriptor;
import com.ran.filesystem.supplier.FileSupplier;
import com.ran.interaction.windows.MainFrame;
import com.ran.interaction.logging.InteractionLogging;
import com.ran.interaction.panels.ProblemsPanel;
import com.ran.interaction.panels.SubmissionsPanel;
import com.ran.interaction.support.SwingUtil;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainController {

    private ProblemsCreator creator;
    private MainFrame mainFrame;

    public void init() {
        creator = new ProblemsCreator();
        creator.init();
    }
    
    public void showFrame() {
        EventQueue.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException | InstantiationException |
                    IllegalAccessException | UnsupportedLookAndFeelException exception) {
                InteractionLogging.logger.log(Level.FINE, "Cannot set Nimbus Look and Feel", exception);
            }
            mainFrame = new MainFrame();
            configurateMainFrame(mainFrame);
            mainFrame.setVisible(true);
        });
    }

    private void configurateMainFrame(MainFrame mainFrame) {
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                creator.stop();
            }
        });
        SubmissionsPanel submissionsPanel = mainFrame.getSubmissionsPanel();
        updateSubmissions(null, null);
        submissionsPanel.subscribe(SubmissionsPanel.ADD, this::addSubmission);
        submissionsPanel.subscribe(SubmissionsPanel.RESUBMIT, this::submitSubmission);
        submissionsPanel.subscribe(SubmissionsPanel.VIEW_CODE, this::viewSubmissionCode);
        submissionsPanel.subscribe(SubmissionsPanel.DELETE, this::deleteSubmission);
        submissionsPanel.subscribe(SubmissionsPanel.UPDATE, this::updateSubmissions);
        ProblemsPanel problemsPanel = mainFrame.getProblemsPanel();
        updateProblems(null, null);
        problemsPanel.subscribe(ProblemsPanel.ADD, this::addProblem);
        problemsPanel.subscribe(ProblemsPanel.EDIT, this::editProblem);
        problemsPanel.subscribe(ProblemsPanel.DELETE, this::deleteProblem);
        problemsPanel.subscribe(ProblemsPanel.UPDATE, this::updateProblems);
    }
    
    private void addSubmission(String id, Object parameter) {
        SubmissionCreationController creationController = new SubmissionCreationController();
        creationController.setFileSupplier(creator.getFileSupplier());
        creationController.showDialog();
        String submissionFolder = creationController.getSubmissionFolder();
        if (submissionFolder != null) {
            updateSubmissions(null, null);
            submitSubmission(null, submissionFolder);
        }
    }
    
    private void submitSubmission(String id, Object parameter) {
        
    }
    
    private void viewSubmissionCode(String id, Object parameter) {
        
    }
    
    private void deleteSubmission(String id, Object parameter) {
        int answer = SwingUtil.showYesNoDialog(mainFrame,
                SubmissionsPanel.DELETING_MESSAGE, SubmissionsPanel.DELETING_TITLE);
        if (answer == JOptionPane.YES_OPTION) {
            creator.getFileSupplier().deleteSubmissionFolder(parameter.toString());
            updateSubmissions(null, null);
        }
    }
    
    private void updateSubmissions(String id, Object parameter) {
        FileSupplier fileSupplier = creator.getFileSupplier();
        List<String> submissionNumbers = fileSupplier.getSubmissionsFolderNames();
        int submissionsQuantity = submissionNumbers.size();
        Object[][] content = new Object[submissionsQuantity][];
        for (int i = 0; i < submissionsQuantity; i++) {
            String number = submissionNumbers.get(i);
            SubmissionDescriptor descriptor = fileSupplier.getSubmissionDescriptor(number);
            String problemNumber = descriptor.getProblemName();
            String problemName = "";
            if (fileSupplier.getProblemsFolderNames().contains(problemNumber)) {
                problemName = fileSupplier.getProblemDescriptor(problemNumber).getProblemName();
            }
            String evaluationType = descriptor.getEvaluationType();
            String compilator = descriptor.getCompilatorName();
            String verdict = descriptor.getVerdict();
            Integer decisionTime = descriptor.getDecisionTime();
            String decisionTimeLine = (decisionTime == null ? "" : decisionTime + " ms");
            content[i] = new Object[] {
                number, problemName, evaluationType, compilator, verdict, decisionTime
            };
        }
        mainFrame.getSubmissionsPanel().setTableContent(content);
    }
    
    private void addProblem(String id, Object parameter) {
        creator.getFileSupplier().addProblemFolder();
        updateProblems(null, null);
    }
    
    private void editProblem(String id, Object parameter) {
        
    }
    
    private void deleteProblem(String id, Object parameter) {
        int answer = SwingUtil.showYesNoDialog(mainFrame,
                ProblemsPanel.DELETING_MESSAGE, ProblemsPanel.DELETING_TITLE);
        if (answer == JOptionPane.YES_OPTION) {
            creator.getFileSupplier().deleteProblemFolder(parameter.toString());
            updateProblems(null, null);
        }
    }
    
    private void updateProblems(String id, Object parameter) {
        FileSupplier fileSupplier = creator.getFileSupplier();
        List<String> problemNumbers = fileSupplier.getProblemsFolderNames();
        int problemsQuantity = problemNumbers.size();
        Object[][] content = new Object[problemsQuantity][];
        for (int i = 0; i < problemsQuantity; i++) {
            String number = problemNumbers.get(i);
            ProblemDescriptor descriptor = fileSupplier.getProblemDescriptor(number);
            String problemName = descriptor.getProblemName();
            String timeLimit = descriptor.getTimeLimit() + "";
            String memoryLimit = descriptor.getMemoryLimit() + "";
            String checkerType = descriptor.getCheckerType();
            content[i] = new Object[] {
                number, problemName, timeLimit, memoryLimit, checkerType
            };
        }
        mainFrame.getProblemsPanel().setTableContent(content);
    }

}
