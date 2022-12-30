package gui.forms;

import data.db.models.Category;

import java.util.Set;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CategoryForm extends EditForm { 
    private JTextField name = new JTextField(12);

    private Set<String> categoriesNames;

    public CategoryForm(Category category, Set<String> categoriesNames) {
        super(category);

        this.categoriesNames = categoriesNames;
        
        initComponents();
        initFunctions();

        setTitle(category != null ? "Editování kategorie" : "Vytváření nové kategorie");
        
        if (category == null) 
            editedEntry = new Category();
    }

    @Override
    protected void initComponents() {
        buildLayout();

        JLabel nameLabel = new JLabel("Název");

        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(nameLabel, gbc);

        gbc.gridx++;
        if (editedEntry != null) 
            name.setText(((Category) editedEntry).getName());
        contentPane.add(name, gbc);

        int gridy = gbc.gridy;
        gbc.gridx = 0;
        gbc.gridy = 2048;
        contentPane.add(confirm, gbc);

        gbc.gridx++;
        contentPane.add(exit, gbc);

        gbc.gridy = gridy;
    }

    @Override
    protected void onConfirm() {           
        if (setData()) {
            this.confirmed = true;
            setVisible(false);
        }
    }

    @Override
    protected void initFunctions() {
        confirm.addActionListener(l -> {
            onConfirm();
        });
        exit.addActionListener(l -> setVisible(false));
        name.addKeyListener(getKeyListener());
    }

    private boolean setData() {
        if (!name.getText().trim().equals("") || !categoriesNames.contains(name.getText().trim())) {
            ((Category) editedEntry).setName(name.getText().trim());
        } else 
            return false;

        editedEntry.setAvailable(true);
        return true;
    }
}
