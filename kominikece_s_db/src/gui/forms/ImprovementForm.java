package gui.forms;

import data.db.models.Category;
import data.db.models.Improvement;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedList;

public class ImprovementForm extends EditForm { 
    private JLabel categoryLabel = new JLabel("Kategorie");
    private List<JComboBox<String>> listOfComboboxs = new LinkedList<>();

    private Map<Integer, Category> categories;

    private List<Category> categoriesAsIndexis = new ArrayList<>();
    private String[] arrCategories;

    private JTextField name = new JTextField(12);
    private JTextField price = new JTextField(12);

    public ImprovementForm(Improvement improvement, Map<Integer, Category> categories) {
        super(improvement);

        this.categories = categories;
        
        initComponents();
        initFunctions();

        setTitle(improvement != null ? "Editování vylepšení" : "Vytváření nového vylepšení");
        
        if (improvement == null) 
            editedEntry = new Improvement();
    }

    @Override
    protected void initComponents() {
        loadCategories();
        buildLayout();

        JLabel nameLabel = new JLabel("Název");
        JLabel priceLabel = new JLabel("Cena");

        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(nameLabel, gbc);

        gbc.gridx++;
        if (editedEntry != null)
            name.setText(((Improvement) editedEntry).getName() + "");
        contentPane.add(name, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        contentPane.add(priceLabel, gbc);

        gbc.gridx++;
        if (editedEntry != null)
            price.setText(((Improvement) editedEntry).getPrice() + "");
        contentPane.add(price, gbc);
        
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
            for (Integer i : (Improvement) editedEntry) {
                addComboBox(categories.get(i));
            }
            addComboBox(null);
        }
    }

    private void addComboBox(Category cat) {
        gbc.gridx = 0;
        gbc.gridy++;
        contentPane.add(new JLabel(categoryLabel.getText()), gbc);

        gbc.gridx++;

        JComboBox<String> jcb = new JComboBox<>(arrCategories);
        listOfComboboxs.add(jcb);
        if (cat != null) 
            jcb.setSelectedItem(cat.getName());
        contentPane.add(jcb, gbc);

        jcb.addActionListener(l -> {
            if (listOfComboboxs.get(listOfComboboxs.size()-1).getSelectedIndex() != 0) {
                addComboBox(null);
            }
        });
    }

    private void loadCategories() {
        arrCategories = new String[categories.size() + 1];

        arrCategories[0] = "\t";
        categoriesAsIndexis.add(null);

        int j = 1;
        for (Category c : categories.values()) {
            arrCategories[j++] = c.getName();
            categoriesAsIndexis.add(c);
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
            ((Improvement) editedEntry).setName(name.getText().trim());
        } else 
            return false;

        if (!price.getText().trim().equals("")) {
            ((Improvement) editedEntry).setPrice(Double.parseDouble(price.getText().trim()));
        } else 
            return false;

        ((Improvement) editedEntry).removeCategories();
        for (JComboBox<String> jcob : listOfComboboxs) {
            Category c = categoriesAsIndexis.get(jcob.getSelectedIndex());
            if (c != null)
                ((Improvement) editedEntry).addCategories(c.getId());
        }

        editedEntry.setAvailable(true);
        return true;
    }
}
