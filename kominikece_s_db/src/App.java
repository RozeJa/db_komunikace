import javax.swing.JFrame;

import data.Setting;
import data.db.MC_Database;

public class App {
    
    public static void main(String[] args) {

        Setting.loadSetting();

        MC_Database.init(Setting.getSetting());

        MC_Database.getDB().loadProducts();
    }

}
