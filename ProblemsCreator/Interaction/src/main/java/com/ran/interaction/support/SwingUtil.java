package com.ran.interaction.support;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class SwingUtil {

    private static JFileChooser fileChooser = null;
    
    public static JFileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser(System.getProperty("user.home"));
        }
        return fileChooser;
    }
    
    public static int showYesNoDialog(Component parent, String message, String title) {
        return JOptionPane.showConfirmDialog(parent, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
    
    public static void showMessageDialog(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void showErrorDialog(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public static <T> Object[][] prepareTableContent(List<T> identifiers,
            BiConsumer<T, List<Object>> rowCreator) {
        List<List<Object>> tableContentList = new ArrayList<>(identifiers.size());
        int columns = 0;
        for (T producerObject: identifiers) {
            List<Object> creatingList = new ArrayList<>();
            rowCreator.accept(producerObject, creatingList);
            tableContentList.add(creatingList);
            columns = Math.max(columns, creatingList.size());
        }
        Object[][] tableContent = new Object[tableContentList.size()][columns];
        for (int i = 0; i < tableContentList.size(); i++) {
            for (int j = 0; j < tableContentList.get(i).size(); j++) {
                tableContent[i][j] = tableContentList.get(i).get(j);
            }
        }
        return tableContent;
    }
    
    private SwingUtil() { }
    
}