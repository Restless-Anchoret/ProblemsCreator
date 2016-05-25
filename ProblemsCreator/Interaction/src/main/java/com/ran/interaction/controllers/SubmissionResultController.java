package com.ran.interaction.controllers;

import com.ran.filesystem.descriptor.AuthorDecisionDescriptor;
import com.ran.filesystem.descriptor.SubmissionDescriptor;
import com.ran.filesystem.supplier.FileSupplier;
import com.ran.interaction.support.PresentationSupport;
import com.ran.interaction.support.SwingUtil;
import com.ran.interaction.support.TestTableWrapper;
import com.ran.interaction.support.TestingUtil;
import com.ran.interaction.windows.SubmissionResultDialog;
import com.ran.testing.system.TestResultHandler;
import com.ran.testing.system.TestingInfo;
import com.ran.testing.system.TestingSystem;
import com.ran.testing.system.VerdictInfo;
import java.awt.Toolkit;
import java.util.Properties;

public class SubmissionResultController {

    private SubmissionResultDialog dialog = null;
    private FileSupplier fileSupplier = null;
    private TestingSystem testingSystem = null;
    private String authorDecisionFolder = null;
    private String problemFolder = null;
    private String submissionFolder = null;
    private TestingInfo resultTestingInfo = null;

    public void setFileSupplier(FileSupplier fileSupplier) {
        this.fileSupplier = fileSupplier;
    }

    public void setTestingSystem(TestingSystem testingSystem) {
        this.testingSystem = testingSystem;
    }

    public void setSubmissionFolder(String submissionFolder) {
        this.submissionFolder = submissionFolder;
    }

    public void setAuthorDecisionFolder(String authorDecisionFolder) {
        this.authorDecisionFolder = authorDecisionFolder;
    }

    public void setProblemFolder(String problemFolder) {
        this.problemFolder = problemFolder;
    }
    
    private synchronized TestingInfo getResultTestingInfo() {
        return resultTestingInfo;
    }
    
    private synchronized void setResultTestingInfo(TestingInfo testingInfo) {
        resultTestingInfo = testingInfo;
    }
    
    public void showDialog() {
        dialog = new SubmissionResultDialog(null, true);
        configureDialog(dialog);
        TestingInfo testingInfo = null;
        if (submissionFolder != null) {
            testingInfo = TestingUtil.prepareTestingInfoForSubmission(fileSupplier,
                    submissionFolder, new DialogResultHandler());
        } else {
            testingInfo = TestingUtil.prepareTestingInfoForAuthorDecision(fileSupplier,
                    problemFolder, authorDecisionFolder, new DialogResultHandler());
        }
        testingSystem.addSubmission(testingInfo);
        dialog.setVisible(true);
    }
    
    private void configureDialog(SubmissionResultDialog dialog) {
        dialog.subscribe(SubmissionResultDialog.UPDATE, this::updateDialog);
        dialog.subscribe(SubmissionResultDialog.CLOSE, this::closeDialog);
        Properties properties = PresentationSupport.getPresentationProperties();
        if (submissionFolder != null) {
            dialog.setSubmission(submissionFolder);
            SubmissionDescriptor submissionDescriptor = fileSupplier.getSubmissionDescriptor(submissionFolder);
            dialog.setProblem(fileSupplier.getProblemDescriptor(submissionDescriptor.getProblemName()).getProblemName());
            dialog.setEvaluationSystem(properties.getProperty(submissionDescriptor.getEvaluationType()));
            dialog.setCompilator(properties.getProperty(submissionDescriptor.getCompilatorName()));
        } else {
            AuthorDecisionDescriptor authorDecisionDescriptor = fileSupplier
                    .getAuthorDecisionDescriptor(problemFolder, authorDecisionFolder);
            dialog.setAuthorDecision(authorDecisionDescriptor.getTitle());
            dialog.setProblem(fileSupplier.getProblemDescriptor(problemFolder).getProblemName());
            dialog.setEvaluationSystem(properties.getProperty(TestingUtil.EVALUATION_SYSTEM_ID_FOR_AUTHOR_DECISIONS));
            dialog.setCompilator(properties.getProperty(authorDecisionDescriptor.getCompilatorName()));
        }
        dialog.setVerdict(SubmissionResultDialog.VERDICT_WAITING);
    }
    
    private void updateDialog(String id, Object parameter) {
        TestingInfo testingInfo = getResultTestingInfo();
        if (testingInfo == null) {
            return;
        }
        VerdictInfo verdictInfo = testingInfo.getVerdictInfo();
        dialog.setVerdict(TestingUtil.getVerdictDescription(verdictInfo.getVerdict().toString(),
                TestingUtil.safeConvertToInt(verdictInfo.getPoints()), verdictInfo.getWrongTestNumber()));
        TestTableWrapper wrapper = new TestTableWrapper(testingInfo.getTestTable());
        Properties presentationProperties = PresentationSupport.getPresentationProperties();
        Object[][] tableContent = SwingUtil.prepareTableContent(wrapper.getCommonTestNumbers(), (number, row) -> {
            row.add(number);
            row.add(presentationProperties.getProperty(wrapper.getTestGroupType(number).toString()));
            VerdictInfo testVerdictInfo = wrapper.getVerdictInfo(number);
            String verdictDescription = TestingUtil.getVerdictDescription(
                    testVerdictInfo.getVerdict().toString(),
                    TestingUtil.safeConvertToInt(testVerdictInfo.getPoints()),
                    testVerdictInfo.getWrongTestNumber());
            row.add(verdictDescription);
            row.add(TestingUtil.getTimeDescription(testVerdictInfo.getDecisionTime()));
        });
        dialog.setTableContent(tableContent);
    }
    
    private void closeDialog(String id, Object parameter) {
        dialog.dispose();
        TestingInfo testingInfo = getResultTestingInfo();
        if (testingInfo == null || submissionFolder == null) {
            return;
        }
        VerdictInfo verdictInfo = testingInfo.getVerdictInfo();
        SubmissionDescriptor descriptor = fileSupplier.getSubmissionDescriptor(submissionFolder);
        descriptor.setVerdict(verdictInfo.getVerdict().toString());
        descriptor.setDecisionMemory(TestingUtil.safeConvertToInt(verdictInfo.getDecisionMemory()));
        descriptor.setDecisionTime(verdictInfo.getDecisionTime());
        descriptor.setDecisionPoints(TestingUtil.safeConvertToInt(verdictInfo.getPoints()));
        descriptor.setWrongTestNumber(verdictInfo.getWrongTestNumber());
        descriptor.persist();
    }
    
    private class DialogResultHandler implements TestResultHandler {
        @Override
        public void process(TestingInfo info) {
            setResultTestingInfo(info);
            Toolkit.getDefaultToolkit().beep();
        }
    }
    
}