package com.ran.interaction.main;

import com.ran.interaction.frame.MainFrame;
import com.ran.interaction.logging.InteractionLogging;
import java.awt.EventQueue;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

    public static void main(String[] args) {
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
            JFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

}
