import javax.swing.JFrame;

import data.Setting;
import data.db.MC_Database;
import gui.MainFrame;

public class App {
    
    public static void main(String[] args) {

        Setting.getSetting().loadSetting();

        MC_Database.init(Setting.getSetting());
        
        setFrame(new MainFrame("MC donald"));
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
