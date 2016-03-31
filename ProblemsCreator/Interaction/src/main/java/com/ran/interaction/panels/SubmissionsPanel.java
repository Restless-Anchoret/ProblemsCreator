package com.ran.interaction.panels;

import com.ran.interaction.observer.EmptyObserver;
import com.ran.interaction.observer.Observer;
import com.ran.interaction.observer.Publisher;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class SubmissionsPanel extends JPanel implements Publisher {

    public static final String ADD = "add_submission";
    public static final String RESUBMIT = "resubmit_submission";
    public static final String DELETE = "delete_submission";
    public static final String UPDATE = "update_submissions";
    
    private static final String NUMBER = "Number";
    private static final String PROBLEM = "Problem";
    private static final String EVALUATION = "Evaluation type";
    private static final String COMPILATOR = "Compilator";
    private static final String VERDICT = "Verdict";
    private static final String DECISION_TIME = "Decision time";
    
    private static final Object[] TABLE_HEADERS = new Object[] {
        NUMBER, PROBLEM, EVALUATION, COMPILATOR, VERDICT, DECISION_TIME
    };
//    private static final int[] TABLE_COLUMN_WIDTHS = new int[] {
//        50, 150, 70, 60, 150, 60
//    };
    
    public SubmissionsPanel() {
        initComponents();
        setTableContent(new Object[][] { });
        initObservers();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        tableSubmissions = new javax.swing.JTable();
        buttonAdd = new javax.swing.JButton();
        buttonResubmit = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        buttonUpdate = new javax.swing.JButton();

        tableSubmissions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Number", "Problem", "Evaluation type", "Compilator", "Verdict", "Decision time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPane.setViewportView(tableSubmissions);
        if (tableSubmissions.getColumnModel().getColumnCount() > 0) {
            tableSubmissions.getColumnModel().getColumn(0).setResizable(false);
            tableSubmissions.getColumnModel().getColumn(0).setPreferredWidth(30);
            tableSubmissions.getColumnModel().getColumn(1).setMinWidth(100);
            tableSubmissions.getColumnModel().getColumn(1).setPreferredWidth(150);
            tableSubmissions.getColumnModel().getColumn(2).setResizable(false);
            tableSubmissions.getColumnModel().getColumn(2).setPreferredWidth(70);
            tableSubmissions.getColumnModel().getColumn(3).setResizable(false);
            tableSubmissions.getColumnModel().getColumn(3).setPreferredWidth(50);
            tableSubmissions.getColumnModel().getColumn(4).setMinWidth(100);
            tableSubmissions.getColumnModel().getColumn(4).setPreferredWidth(150);
            tableSubmissions.getColumnModel().getColumn(5).setResizable(false);
            tableSubmissions.getColumnModel().getColumn(5).setPreferredWidth(60);
        }

        buttonAdd.setText("Add");

        buttonResubmit.setText("Resubmit");

        buttonDelete.setText("Delete");

        buttonUpdate.setText("Update");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonResubmit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonUpdate)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonAdd)
                    .addComponent(buttonResubmit)
                    .addComponent(buttonDelete)
                    .addComponent(buttonUpdate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonResubmit;
    private javax.swing.JButton buttonUpdate;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable tableSubmissions;
    // End of variables declaration//GEN-END:variables

    private Map<String, Observer> observers = new HashMap<>();

    @Override
    public List<String> getAvailableIds() {
        return Arrays.asList(ADD, RESUBMIT, DELETE, UPDATE);
    }
    
    @Override
    public void subscribe(String id, Observer observer) {
        observers.put(id, observer);
    }

    @Override
    public Observer getObserver(String id) {
        return observers.getOrDefault(id, EmptyObserver.getInstanse());
    }
    
    public final void setTableContent(Object[][] content) {
        tableSubmissions.setModel(new DefaultTableModel(content, TABLE_HEADERS));
//        TableColumnModel columnModel = new DefaultTableColumnModel();
//        for (int width: TABLE_COLUMN_WIDTHS) {
//            columnModel.addColumn(new TableColumn(0, width));
//        }
//        tableSubmissions.setColumnModel(columnModel);
    }
    
}
