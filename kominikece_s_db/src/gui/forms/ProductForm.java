package gui.forms;

import data.db.models.Category;
import data.db.models.Improvement;
import data.db.models.Product;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedList;

public class ProductForm extends EditForm { 
    private JLabel improvementLabel = new JLabel("Vylepšení");
    private List<JComboBox<String>> listOfComboboxs = new LinkedList<>();

    private Map<Integer, Improvement> improvements;
    private Map<Integer, Category> categories;

    private List<Improvement> improvmentAsIndexis = new ArrayList<>();
    private List<Category> categoriesAsIndexis = new ArrayList<>();
    private String[] arrImprovement;

    private JTextField name = new JTextField(12);
    private JTextField price = new JTextField(12);
    private JComboBox<String> category;

    public ProductForm(Product product, Map<Integer, Improvement> improvements, Map<Integer, Category> categories) {
        super(product);

        this.improvements = improvements;
        this.categories = categories;

        initComponents();
        initFunctions();

        setTitle(product != null ? "Editování produktu" : "Vytváření nového produktu");
        
        if (product == null) 
            editedEntry = new Product();
    }

    @Override
    protected void initComponents() {
        loadImprevements();
        category = new JComboBox<>(parseCategories());
        buildLayout();

        JLabel nameLabel = new JLabel("Název");
        JLabel priceLabel = new JLabel("Cena");
        JLabel categoryLabel = new JLabel("Kategorie");


        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(nameLabel, gbc);

        gbc.gridx++;
        if (editedEntry != null) 
            name.setText(((Product) editedEntry).getName());
        contentPane.add(name, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        contentPane.add(priceLabel, gbc);

        gbc.gridx++;
        if (editedEntry != null) 
            price.setText(((Product) editedEntry).getPrice() + "");
        contentPane.add(price, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        contentPane.add(categoryLabel, gbc);

        gbc.gridx++;
        if (editedEntry != null) 
            category.setSelectedItem(categories.get(((Product) editedEntry).getCategory()).getName());
        contentPane.add(category, gbc);

        int gridy = gbc.gridy;
        gbc.gridx = 0;
        gbc.gridy = 2048;
        contentPane.add(confirm, gbc);

        gbc.gridx++;
        contentPane.add(exit, gbc);

        gbc.gridy = gridy;

        if (editedEntry == null) {
            addComboBox(null);
        } else {
            for (Integer i : (Product) editedEntry) {
                addComboBox(improvements.get(i));
            }
        }
    }

    private String[] parseCategories() {
        String[] arr = new String[categories.size() + 1];

        arr[0] = "";
        categoriesAsIndexis.add(null);

        int j = 1;
        for (Category c : categories.values()) {
            arr[j++] = c.getName();
            categoriesAsIndexis.add(c);
        }

        return arr;
    }

    private void addComboBox(Improvement imp) {
        gbc.gridx = 0;
        gbc.gridy++;
        contentPane.add(improvementLabel, gbc);

        gbc.gridx++;

        JComboBox<String> jcb = new JComboBox<>(arrImprovement);
        listOfComboboxs.add(jcb);
        if (imp != null) 
            jcb.setSelectedItem(imp.getName());
        contentPane.add(jcb, gbc);

        jcb.addActionListener(l -> {
            if (listOfComboboxs.get(listOfComboboxs.size()-1).getSelectedIndex() != 0) {
                addComboBox(null);
            }
        });
    }

    private void loadImprevements() {
        arrImprovement = new String[improvements.size() + 1];

        arrImprovement[0] = "\t";
        improvmentAsIndexis.add(null);

        int j = 1;
        for (Improvement i : improvements.values()) {
            arrImprovement[j++] = i.getName();
            improvmentAsIndexis.add(i);
        }
    }

    @Override
    protected void initFunctions() {
        confirm.addActionListener(l -> {
            
            if (setData()) {
                this.confirmed = true;
                setVisible(false);
            }
        });
        exit.addActionListener(l -> setVisible(false));
    }

    private boolean setData() {
        if (!name.getText().trim().equals("")) {
            ((Product) editedEntry).setName(name.getText().trim());
        } else 
            return false;

        if (!price.getText().trim().equals("")) {
            ((Product) editedEntry).setPrice(Double.parseDouble(price.getText().trim()));
        } else 
            return false;

        if (categoriesAsIndexis.get(category.getSelectedIndex()) != null) {
            ((Product) editedEntry).setCategories(categoriesAsIndexis.get(category.getSelectedIndex()).getId());
        } else 
            return false;

        for (JComboBox<String> jcob : listOfComboboxs) {            
            Improvement i = improvmentAsIndexis.get(jcob.getSelectedIndex());
            if (i != null)
                ((Product) editedEntry).addImprovement(i.getId());
        }

        editedEntry.setAvailable(true);
        return true;
    }
}
