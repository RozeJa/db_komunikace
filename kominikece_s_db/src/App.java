import javax.swing.JFrame;

import data.Setting;
import data.db.MC_Database;
import data.db.buildesr.ProductBuilder;
import data.db.models.Product;
import gui.MainFrame;

public class App {
    
    public static void main(String[] args) {

        System.out.println(new Product().getClass().getName());
        System.out.println(new ProductBuilder().getClass().getName());

        // načti si nastavení
        Setting.getSetting().loadSetting();

        MC_Database.init(Setting.getSetting());
        // pokus se připojit na server 
            // pokud defaultní není dostupný umožní se připojit na jiný 
        
        MainFrame frame = new MainFrame("MC donald");
        if (!frame.isClosed()) 
            setFrame(frame);
         else 
            System.exit(0);
    }

    // zobrazení okna
    private static void setFrame(JFrame frame) {
        Setting s = Setting.getSetting();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(Integer.parseInt(s.getAttribute(Setting.width)), Integer.parseInt(s.getAttribute(Setting.height)));
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        if (Boolean.valueOf(s.getAttribute(Setting.fullscrean)))
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

}
