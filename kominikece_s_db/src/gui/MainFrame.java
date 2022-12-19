package gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import data.db.MC_Database;
import data.db.models.Category;
import data.db.models.Improvement;
import data.db.models.Product;

import java.awt.event.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class MainFrame extends JFrame {

    private Container container;
    private JList<String> datatypes;
    private JTable dataTable;
    private JScrollPane datatypesSP, dataTableSP;

    private JButton add, update, delete;

    private boolean isClosed = false;

    private int actualDataModel = 0;

    public MainFrame(String title) {
        setTitle(title);

        initComponents();

        initFunctions();

        fillProducts();
    }

    public boolean isClosed() {
        return isClosed;
    }


    private void initFunctions() {
        MouseListener selectDataTyps = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                reprintTable(datatypes.getSelectedIndex());
            }
        };
        datatypes.addMouseListener(selectDataTyps);
        
        add.addActionListener(l -> {
            // TODO:

            // Podle zvolené 
        });

        update.addActionListener(l -> {
            // TODO: 
        });
    
        delete.addActionListener(l -> {
            // TODO: 
        });

        dataTable.getModel().addTableModelListener(l -> {
            // TODO: full stack vyhledávání
        });
    }

    private void reprintTable(int index) {
        switch (index) {
            case 0:
                fillProducts();
                break;
            case 1: 
                fillImprovements();
                break;
            case 2:
                fillCategories();
                break;
        }

        actualDataModel = index;
    }

    private void fillProducts() {
        Map<Integer, Product> data = MC_Database.getDB().getProducts();
        if (data == null) 
            data = new TreeMap<>();

        dataTable.setModel(new DefaultTableModel(formatProductData(data), Product.getPropertyes()));
    }

    private String[][] formatProductData(Map<Integer, Product> products) {
        LinkedList<String[]> data = new LinkedList<>();

        for (Product product : products.values()) {
            String[] row = new String[5]; 
            row[0] = String.valueOf(product.getId());
            row[1] = product.getName();
            row[2] = String.valueOf(product.getPrice());
            row[3] = String.valueOf(product.isAvailable());
            
            data.add(row);

            int j = 0;
            for (Integer improvementID : product) {
                if (j == 0)
                    row[4] = MC_Database.getDB().getImprovement(improvementID).getName(); 
                else 
                    data.add(new String[] {"","","","",MC_Database.getDB().getImprovement(improvementID).getName()});

                j++;
            }
        }

        return tooTwoDArr(data);
    }


    private void fillImprovements() {
        Map<Integer, Improvement> data = MC_Database.getDB().getImprovements();
        if (data == null) 
            data = new TreeMap<>();

        dataTable = new JTable(formatImprovementData(data), Improvement.getPropertyes());

        dataTableSP.setViewportView(dataTable);
    }

    private String[][] formatImprovementData(Map<Integer, Improvement> improvements) {
        LinkedList<String[]> data = new LinkedList<>();

        for (Improvement improvement : improvements.values()) {
            String[] row = new String[5]; 
            row[0] = String.valueOf(improvement.getId());
            row[1] = improvement.getName();
            row[2] = String.valueOf(improvement.getPrice());
            row[3] = String.valueOf(improvement.isAvailable());
            
            data.add(row);

            int j = 0;
            for (Integer categoryID : improvement) {
                if (j == 0)
                    row[4] = MC_Database.getDB().getImprovement(categoryID).getName(); 
                else 
                    data.add(new String[] {"","","","",MC_Database.getDB().getCategory(categoryID).getName()});

                j++;
            }
        }

        return tooTwoDArr(data);
    }

    private void fillCategories() {
        Map<Integer, Category> data = MC_Database.getDB().getCategories();
        if (data == null) 
            data = new TreeMap<>();

        dataTable = new JTable(formatCategoryData(data), Category.getPropertyes());

        dataTableSP.setViewportView(dataTable);
    }

    private String[][] formatCategoryData(Map<Integer, Category> categories) {
        String[][] data = new String[categories.size()][];

        int i = 0;
        for (Category category : categories.values()) {
            String[] row = new String[5]; 
            row[0] = String.valueOf(category.getId());
            row[1] = category.getName();
            row[2] = String.valueOf(category.isAvailable());
            
            data[i++] = row;
        }

        return data;
    }

    private String[][] tooTwoDArr(LinkedList<String[]> data) {
        String[][] arr = new String[data.size()][];
        
        int i = 0;
        for (String[] strings : data) {
            arr[i] = strings;
            i++;
        }
        return arr;
    }

    private void initComponents() {
        // naházení komponent na stránku
        container = this.getContentPane();

        container.setLayout(new GridLayout(1,2));
    
        datatypes = new JList<>(new String[] {"Produkty", "Vypelšení", "Kategorie"});
        datatypesSP = new JScrollPane(datatypes);

        JPanel midPan = new JPanel(new GridLayout(1,3));
        dataTable = new JTable();
        dataTableSP = new JScrollPane(dataTable);
        midPan.add(dataTableSP);


        JPanel right = new JPanel(new GridLayout(4,1));

        add = new JButton("Přidat");
        update = new JButton("Editovat");
        delete = new JButton("Odebrat");
        right.add(add);
        right.add(update);
        right.add(delete);

        midPan.add(datatypesSP);
        midPan.add(new JPanel());
        midPan.add(right);
        midPan.add(new JPanel());
        
        container.add(midPan);
        container.add(dataTableSP);
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