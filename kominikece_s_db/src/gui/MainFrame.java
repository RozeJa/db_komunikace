package gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import data.db.MC_Database;
import data.db.models.ADatabaseEntry;
import data.db.models.Category;
import data.db.models.Improvement;
import data.db.models.Product;
import gui.forms.CategoryForm;
import gui.forms.EditForm;
import gui.forms.ImprovementForm;
import gui.forms.ProductForm;

import java.awt.event.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

// TODO: mapuj jména k objektům, abys mohl hlídat zda uživatel nezadal, takoví produkt, který existuje
public class MainFrame extends JFrame {

    private Container container;
    private JList<String> datatypes;
    private JTable dataTable;
    private JScrollPane datatypesSP, dataTableSP;

    private JButton add, update, delete;

    private int actualDataModel = 0;

    private MC_Database mc_db = MC_Database.getDB();

    public MainFrame(String title) {
        setTitle(title);

        initComponents();

        initFunctions();

        fillProducts();
    }

    private void initFunctions() {
        MouseListener selectDataTyps = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                reprintTable(datatypes.getSelectedIndex());
            }
        };
        datatypes.addMouseListener(selectDataTyps);
        
        add.addActionListener(l -> {
            // TODO: provést synchronizaci a změnit value v tabulce
            EditForm ef = null;
            ADatabaseEntry ade = null;

            switch (actualDataModel) {
                case 0:                    
                    ef = new ProductForm(null, mc_db.getImprovements(), mc_db.getCategories());
                    break;
                case 1:
                    ef = new ImprovementForm(null, mc_db.getCategories());
                    break;
                case 2:
                    ef = new CategoryForm(null);
                    break;
            }

            if (ef != null) {
                setJDialog(ef);
                if (ef.isConfirmed())
                    mc_db.create(ade, null);   
            }
        });

        update.addActionListener(l -> {
            int selectedIndex = dataTable.getSelectedRow();
            
            if (selectedIndex >= 0) {
                // TODO: provést synchronizaci a změnit value v tabulce
                EditForm ef = null;
                ADatabaseEntry ade = null;

                switch (actualDataModel) {
                    case 0:
                        ade =  mc_db.getProduct(Integer.parseInt((String) dataTable.getValueAt(selectedIndex, 0)));
                        
                        ef = new ProductForm((Product) ade, mc_db.getImprovements(), mc_db.getCategories());
                        break;
                    case 1:
                        ade = mc_db.getImprovement(Integer.parseInt((String) dataTable.getValueAt(selectedIndex, 0)));

                        ef = new ImprovementForm((Improvement) ade, mc_db.getCategories());
                        break;
                    case 2:
                        ade = mc_db.getCategory(Integer.parseInt((String) dataTable.getValueAt(selectedIndex, 0)));

                        ef = new CategoryForm((Category) ade);
                        break;
                }
                if (ef != null) {
                    setJDialog(ef);
                    if (ef.isConfirmed())
                        mc_db.updeteData(ade, null);   
                }
            } 
        });
    
        delete.addActionListener(l -> {
            int selectedIndex = dataTable.getSelectedRow();
            
            if (selectedIndex >= 0) {
                // TODO: provést synchronizaci a změnit value v tabulce
                switch (actualDataModel) {
                    case 0:
                        mc_db.removeProduct(mc_db.getProduct(Integer.parseInt((String) dataTable.getValueAt(selectedIndex, 0))), null);
                        break;
                    case 1:
                        mc_db.removeImprovement(mc_db.getImprovement(Integer.parseInt((String) dataTable.getValueAt(selectedIndex, 0))), null);
                        break;
                    case 2:
                        mc_db.removeCategory(mc_db.getCategory(Integer.parseInt((String) dataTable.getValueAt(selectedIndex, 0))), null);
                        break;
                }
            }
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
}