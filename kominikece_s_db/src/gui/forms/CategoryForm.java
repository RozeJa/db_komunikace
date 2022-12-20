package gui.forms;

import data.db.models.Category;
import javax.swing.*;

public class CategoryForm extends EditForm { 
    private JTextField name = new JTextField(12);

    public CategoryForm(Category category) {
        super(category);
        
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
            ((Category) editedEntry).setName(name.getText().trim());
        } else 
            return false;

        editedEntry.setAvailable(true);
        return true;
    }
}
