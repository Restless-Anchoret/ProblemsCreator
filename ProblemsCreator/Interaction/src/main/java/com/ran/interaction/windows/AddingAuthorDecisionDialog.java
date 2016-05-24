package com.ran.interaction.windows;

import com.ran.interaction.components.SelectItem;
import com.ran.interaction.observer.EmptyObserver;
import com.ran.interaction.observer.Observer;
import com.ran.interaction.observer.Publisher;
import javax.swing.JDialog;
import java.awt.Frame;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddingAuthorDecisionDialog extends JDialog implements Publisher {

    public static final String FILE_PATH_MESSAGE = "You should choose file path";
    public static final String FILE_PATH_TITLE = "File path";
    public static final String FILE_LOADING_ERROR_MESSAGE = "Error while loading file";
    public static final String FILE_LOADING_ERROR_TITLE = "Loading error";
    
    public static final String ADD = "add";
    public static final String CANCEL = "cancel";
    
    public AddingAuthorDecisionDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initCustomComponents();
        initObservers();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelTitle = new javax.swing.JLabel();
        textFieldTitle = new javax.swing.JTextField();
        labelCompilator = new javax.swing.JLabel();
        labelFile = new javax.swing.JLabel();
        comboBoxCompilator = new com.ran.interaction.components.PresentationComboBox();
        fileBrowser = new com.ran.interaction.components.FileBrowser();
        buttonCancel = new javax.swing.JButton();
        buttonAdd = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        labelTitle.setText("Decision title:");

        labelCompilator.setText("Compilator:");

        labelFile.setText("File:");

        buttonCancel.setText("Cancel");

        buttonAdd.setText("Add");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonCancel))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(labelFile)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(fileBrowser, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelTitle)
                                .addComponent(labelCompilator))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(comboBoxCompilator, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(textFieldTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTitle)
                    .addComponent(textFieldTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboBoxCompilator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCompilator, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fileBrowser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonCancel)
                    .addComponent(buttonAdd))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonCancel;
    private com.ran.interaction.components.PresentationComboBox comboBoxCompilator;
    private com.ran.interaction.components.FileBrowser fileBrowser;
    private javax.swing.JLabel labelCompilator;
    private javax.swing.JLabel labelFile;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JTextField textFieldTitle;
    // End of variables declaration//GEN-END:variables

    private Map<String, Observer> observers = new HashMap<>();
    
    private void initCustomComponents() {
        buttonAdd.addActionListener(event -> getObserver(ADD).notify(ADD, null));
        buttonCancel.addActionListener(event -> getObserver(CANCEL).notify(CANCEL, null));
    }
    
    @Override
    public List<String> getAvailableIds() {
        return Arrays.asList(ADD, CANCEL);
    }
    
    @Override
    public void subscribe(String id, Observer observer) {
        observers.put(id, observer);
    }

    @Override
    public Observer getObserver(String id) {
        return observers.getOrDefault(id, EmptyObserver.getInstanse());
    }
    
    public String getDecisionTitle() {
        return textFieldTitle.getText();
    }
    
    public void setCompilatorSelectItems(List<SelectItem> compilatorNames) {
        comboBoxCompilator.setSelectItems(compilatorNames);
        comboBoxCompilator.setSelectedIndex(0);
    }
    
    public String getCompilator() {
        return comboBoxCompilator.getSelectedValue();
    }
    
    public Path getFilePath() {
        return fileBrowser.getFilePath();
    }
    
}
