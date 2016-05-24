package com.ran.interaction.controllers;

import com.ran.filesystem.descriptor.AuthorDecisionDescriptor;
import com.ran.filesystem.supplier.FileSupplier;
import com.ran.interaction.components.SelectItem;
import com.ran.interaction.support.PresentationSupport;
import com.ran.interaction.support.SwingUtil;
import com.ran.interaction.windows.AddingAuthorDecisionDialog;
import com.ran.interaction.windows.SubmissionCreationDialog;
import com.ran.testing.language.LanguageToolkitRegistry;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class AddingAuthorDecisionController {

    private AddingAuthorDecisionDialog dialog = null;
    private FileSupplier fileSupplier = null;
    private String problemFolder = null;
    private String authorDecisionDialog = null;

    public void setFileSupplier(FileSupplier fileSupplier) {
        this.fileSupplier = fileSupplier;
    }

    public void setProblemFolder(String problemFolder) {
        this.problemFolder = problemFolder;
    }

    public String getAuthorDecisionFolder() {
        return authorDecisionDialog;
    }
    
    public void showDialog() {
        dialog = new AddingAuthorDecisionDialog(null, true);
        configureDialog(dialog);
        dialog.setVisible(true);
    }
    
    private void configureDialog(AddingAuthorDecisionDialog dialog) {
        dialog.subscribe(AddingAuthorDecisionDialog.ADD, this::addAuthorDecision);
        dialog.subscribe(AddingAuthorDecisionDialog.CANCEL, this::cancelAdding);
        Properties presentationProperties = PresentationSupport.getPresentationProperties();
        Collection<String> availableCompilators = LanguageToolkitRegistry.registry().getAvailableIds();
        List<SelectItem> compilatorSelectItems = new ArrayList<>(availableCompilators.size());
        for (String compilatorName: availableCompilators) {
            compilatorSelectItems.add(new SelectItem(compilatorName,
                    presentationProperties.getProperty(compilatorName)));
        }
        dialog.setCompilatorSelectItems(compilatorSelectItems);
    }
    
    private void addAuthorDecision(String id, Object parameter) {
        if (dialog.getFilePath() == null) {
            SwingUtil.showMessageDialog(dialog, SubmissionCreationDialog.FILE_PATH_MESSAGE,
                    SubmissionCreationDialog.FILE_PATH_TITLE);
            return;
        }
        authorDecisionDialog = fileSupplier.addAuthorDecisionFolder(problemFolder);
        Path loadedSourceFile = fileSupplier.getAuthorDecisionCodeSupplier(problemFolder, authorDecisionDialog)
                .putSourceFile(dialog.getFilePath());
        if (loadedSourceFile == null) {
            SwingUtil.showErrorDialog(dialog, SubmissionCreationDialog.FILE_LOADING_ERROR_MESSAGE,
                    SubmissionCreationDialog.FILE_LOADING_ERROR_TITLE);
            fileSupplier.deleteAuthorDecisionFolder(problemFolder, authorDecisionDialog);
            authorDecisionDialog = null;
            return;
        }
        AuthorDecisionDescriptor descriptor = fileSupplier.getAuthorDecisionDescriptor(problemFolder, authorDecisionDialog);
        descriptor.setTitle(dialog.getDecisionTitle());
        descriptor.setCompilatorName(dialog.getCompilator());
        descriptor.persist();
        dialog.dispose();
    }
    
    private void cancelAdding(String id, Object parameter) {
        dialog.dispose();
    }
    
}