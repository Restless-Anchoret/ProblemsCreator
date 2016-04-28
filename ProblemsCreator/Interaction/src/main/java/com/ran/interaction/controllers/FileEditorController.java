package com.ran.interaction.controllers;

import com.ran.interaction.logging.InteractionLogging;
import com.ran.interaction.support.SwingUtil;
import com.ran.interaction.windows.FileEditorDialog;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileEditorController {

    private FileEditorDialog dialog;
    private Path filePath;
    
    public void showDialog(Path filePath, boolean readOnly) {
        dialog = new FileEditorDialog(null, true);
        this.filePath = filePath;
        configureDialog(dialog, filePath, readOnly);
        dialog.setVisible(true);
    }
    
    private void configureDialog(FileEditorDialog dialog, Path filePath, boolean readOnly) {
        dialog.subscribe(FileEditorDialog.SAVE, this::saveFileText);
        dialog.subscribe(FileEditorDialog.CANCEL, this::cancelChanges);
        dialog.setFilePath(filePath);
        dialog.setReadOnly(readOnly);
        try {
            List<String> fileLines = Files.readAllLines(filePath);
            StringBuilder builder = new StringBuilder();
            for (String line: fileLines) {
                if (builder.length() > 0) {
                    builder.append('\n');
                }
                builder.append(line);
            }
            dialog.setText(builder.toString());
        } catch (IOException exception) {
            InteractionLogging.logger.log(Level.FINE, "IOException while reading file content", exception);
            dialog.setText(FileEditorDialog.CANNOT_READ_TEXT);
            dialog.setReadOnly(true);
        }
    }
    
    private void saveFileText(String id, Object parameter) {
        try (PrintWriter writer = new PrintWriter(filePath.toFile())) {
            writer.print(dialog.getText());
            writer.flush();
            dialog.dispose();
        } catch (FileNotFoundException exception) {
            InteractionLogging.logger.log(Level.FINE, "Unable to save file", exception);
            SwingUtil.showErrorDialog(dialog, FileEditorDialog.UNABLE_SAVE_MESSAGE,
                    FileEditorDialog.UNABLE_SAVE_TITLE);
        }
    }
    
    private void cancelChanges(String id, Object parameter) {
        dialog.dispose();
    }
    
}