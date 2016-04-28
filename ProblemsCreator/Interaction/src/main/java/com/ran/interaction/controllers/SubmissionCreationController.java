package com.ran.interaction.controllers;

import com.ran.filesystem.descriptor.SubmissionDescriptor;
import com.ran.filesystem.supplier.FileSupplier;
import com.ran.interaction.components.SelectItem;
import com.ran.interaction.support.PresentationSupport;
import com.ran.interaction.support.SwingUtil;
import com.ran.interaction.windows.SubmissionCreationDialog;
import com.ran.testing.evaluation.EvaluationSystemRegistry;
import com.ran.testing.language.LanguageToolkitRegistry;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class SubmissionCreationController {
    
    private SubmissionCreationDialog dialog = null;
    private FileSupplier fileSupplier = null;
    private String submissionFolder = null;

    public void setFileSupplier(FileSupplier fileSupplier) {
        this.fileSupplier = fileSupplier;
    }

    public String getSubmissionFolder() {
        return submissionFolder;
    }
    
    public void showDialog() {
        dialog = new SubmissionCreationDialog(null, true);
        configureDialog(dialog);
        dialog.setVisible(true);
    }
    
    private void configureDialog(SubmissionCreationDialog dialog) {
        dialog.subscribe(SubmissionCreationDialog.SUBMIT, this::submitSubmission);
        dialog.subscribe(SubmissionCreationDialog.CANCEL, this::cancelSubmission);
        List<String> problemFolderNames = fileSupplier.getProblemsFolderNames();
        List<SelectItem> problemSelectItems = new ArrayList<>(problemFolderNames.size());
        for (String problemFolderName: problemFolderNames) {
            problemSelectItems.add(new SelectItem(problemFolderName,
                    fileSupplier.getProblemDescriptor(problemFolderName).getProblemName()));
        }
        dialog.setProblemNamesSelectItems(problemSelectItems);
        Properties presentationProperties = PresentationSupport.getPresentationProperties();
        Collection<String> availableEvaluationSystems = EvaluationSystemRegistry.registry().getAvailableIds();
        List<SelectItem> evaluationSystemSelectItems = new ArrayList<>(availableEvaluationSystems.size());
        for (String evaluationSystemName: availableEvaluationSystems) {
            evaluationSystemSelectItems.add(new SelectItem(evaluationSystemName,
                    presentationProperties.getProperty(evaluationSystemName)));
        }
        dialog.setEvaluationSystemsSelectItems(evaluationSystemSelectItems);
        Collection<String> availableCompilators = LanguageToolkitRegistry.registry().getAvailableIds();
        List<SelectItem> compilatorSelectItems = new ArrayList<>(availableCompilators.size());
        for (String compilatorName: availableCompilators) {
            compilatorSelectItems.add(new SelectItem(compilatorName,
                    presentationProperties.getProperty(compilatorName)));
        }
        dialog.setCompilatorSelectItems(compilatorSelectItems);
    }
    
    private void submitSubmission(String id, Object parameter) {
        if (dialog.getFilePath() == null) {
            SwingUtil.showMessageDialog(dialog, SubmissionCreationDialog.FILE_PATH_MESSAGE,
                    SubmissionCreationDialog.FILE_PATH_TITLE);
            return;
        }
        submissionFolder = fileSupplier.addSubmissionFolder();
        Path loadedSourceFile = fileSupplier.getSubmissionCodeSupplier(submissionFolder)
                .putSourceFile(dialog.getFilePath());
        if (loadedSourceFile == null) {
            SwingUtil.showErrorDialog(dialog, SubmissionCreationDialog.FILE_LOADING_ERROR_MESSAGE,
                    SubmissionCreationDialog.FILE_LOADING_ERROR_TITLE);
            fileSupplier.deleteSubmissionFolder(submissionFolder);
            submissionFolder = null;
            return;
        }
        SubmissionDescriptor descriptor = fileSupplier.getSubmissionDescriptor(submissionFolder);
        descriptor.setProblemName(dialog.getProblemFolder());
        descriptor.setEvaluationType(dialog.getEvaluationSystem());
        descriptor.setCompilatorName(dialog.getCompilator());
        descriptor.persist();
        dialog.dispose();
    }
    
    private void cancelSubmission(String id, Object parameter) {
        dialog.dispose();
    }
    
}