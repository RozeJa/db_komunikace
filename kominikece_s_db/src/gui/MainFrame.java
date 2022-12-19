package gui;

import java.awt.*;

import javax.swing.*;

public class MainFrame extends JFrame {

    private Container container;
    private JList<String> dataList;
    private JTable dataTable;
    private JScrollPane dataListSP, dataTableSP;

    private boolean isClosed = false;

    public MainFrame(String title) {
        setTitle(title);

        initComponents();

        initFunctions();

    }

    public boolean isClosed() {
        return isClosed;
    }


    private void initFunctions() {
        
    }

    private void initComponents() {
        
        // naházení komponent na stránku
        container = this.getContentPane();

        container.setLayout(new BorderLayout());
    
        dataTable = new JTable();
        dataTableSP = new JScrollPane();
        dataTableSP.setViewportView(dataTable);
        
        dataList = new JList<>();
        dataListSP = new JScrollPane(dataList);

        container.add(dataTableSP, BorderLayout.CENTER);
        container.add(dataListSP, BorderLayout.LINE_START);
    }


    private void setJDialog(JDialog frame) {
        frame.setModal(true);
        frame.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setVisible(true);
    }
    
    private void setJFrame(JFrame frame) {
        frame.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setVisible(true);
    }
}