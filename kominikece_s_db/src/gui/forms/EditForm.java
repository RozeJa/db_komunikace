package gui.forms;

import javax.swing.JDialog;

import data.db.models.ADatabaseEntry;
import java.awt.*;
import javax.swing.*;

public abstract class EditForm extends JDialog {
    protected ADatabaseEntry editedEntry;
    protected boolean confirmed = false;

    private Container container;
    protected JPanel contentPane = new JPanel(new GridBagLayout());
    protected GridBagConstraints gbc = new GridBagConstraints();    
    
    protected JButton confirm = new JButton("Potvrdit"), exit = new JButton("Zrušit");

    protected EditForm(ADatabaseEntry editedEntry) {
        this.editedEntry = editedEntry;
    }

    protected abstract void initComponents();

    protected abstract void initFunctions();

    protected void buildLayout() {
        container = this.getContentPane();

        container.setLayout(new GridLayout(1,3));
        container.add(new JPanel());

        container.add(new JScrollPane(contentPane));
        
        container.add(new JPanel());
    }

    public ADatabaseEntry getEditedEntry() {
        return editedEntry;
    }
    public boolean isConfirmed() {
        return confirmed;
    }
}
