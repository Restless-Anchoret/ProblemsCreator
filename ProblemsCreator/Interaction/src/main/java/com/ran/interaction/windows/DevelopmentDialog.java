package com.ran.interaction.windows;

import com.ran.interaction.observer.EmptyObserver;
import com.ran.interaction.observer.Observer;
import com.ran.interaction.observer.Publisher;
import javax.swing.JDialog;
import java.awt.Frame;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTextArea;

public class DevelopmentDialog extends JDialog implements Publisher {

    public static final String SAVE = "save_results";
    public static final String INTERRUPT = "interrupt_process";
    public static final String CLOSE = "close_dialog";
    
    private static final String EXECUTING_FILE_PREFIX = "Executing file: ";
    
    public DevelopmentDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initCustomComponents();
        initObservers();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelTitle = new javax.swing.JLabel();
        buttonClose = new javax.swing.JButton();
        buttonInterrupt = new javax.swing.JButton();
        buttonSave = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaOutput = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        labelTitle.setText("Executing file:");

        buttonClose.setText("Close");

        buttonInterrupt.setText("Interrupt");

        buttonSave.setText("Save");

        textAreaOutput.setEditable(false);
        textAreaOutput.setColumns(20);
        textAreaOutput.setRows(5);
        jScrollPane1.setViewportView(textAreaOutput);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(buttonSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonInterrupt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonClose))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelTitle)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonClose)
                    .addComponent(buttonInterrupt)
                    .addComponent(buttonSave))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonClose;
    private javax.swing.JButton buttonInterrupt;
    private javax.swing.JButton buttonSave;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JTextArea textAreaOutput;
    // End of variables declaration//GEN-END:variables

    private Map<String, Observer> observers = new HashMap<>();
    
    private void initCustomComponents() {
        buttonSave.addActionListener(event -> getObserver(SAVE).notify(SAVE, null));
        buttonInterrupt.addActionListener(event -> getObserver(INTERRUPT).notify(INTERRUPT, null));
        buttonClose.addActionListener(event -> getObserver(CLOSE).notify(CLOSE, null));
    }
    
    @Override
    public List<String> getAvailableIds() {
        return Arrays.asList(SAVE, INTERRUPT, CLOSE);
    }
    
    @Override
    public void subscribe(String id, Observer observer) {
        observers.put(id, observer);
    }

    @Override
    public Observer getObserver(String id) {
        return observers.getOrDefault(id, EmptyObserver.getInstanse());
    }

    public JTextArea getTextAreaOutput() {
        return textAreaOutput;
    }
    
    public void setSaveAbility(boolean saveAbility) {
        buttonSave.setVisible(saveAbility);
    }
    
    public void setExecutingFileName(String fileName) {
        labelTitle.setText(EXECUTING_FILE_PREFIX + fileName);
    }
    
}
