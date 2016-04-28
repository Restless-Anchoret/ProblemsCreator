package com.ran.interaction.support;

import java.awt.Component;
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
    
    private SwingUtil() { }
    
}