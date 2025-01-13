package gui;

import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public MyTableModel(Object[][] rowsData, String[] colunms) {
        super(rowsData, colunms);
    }
    
}
