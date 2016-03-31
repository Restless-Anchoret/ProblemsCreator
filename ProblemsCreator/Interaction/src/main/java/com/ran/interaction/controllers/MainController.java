package com.ran.interaction.controllers;

import com.ran.interaction.application.Application;
import com.ran.interaction.frame.MainFrame;
import com.ran.interaction.logging.InteractionLogging;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainController {

    private Application application;
    private MainFrame mainFrame;

    public void showFrame() {
        application = new Application();
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
            public void windowClosed(WindowEvent event) {
                application.stop();
            }
        });
        // TODO! Add Observers to MainFrame panels;
    }

}
