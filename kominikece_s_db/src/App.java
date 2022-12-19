import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import data.Setting;
import data.io.txt.ExportToTXT;
import data.io.txt.ImportFromTXT;
import gui.MainFrame;

public class App {
    
    public static void main(String[] args) {

        // načti si nastavení
        loadSetting();

        // pokus se připojit na server 
            // pokud defaultní není dostupný umožní se připojit na jiný 
        
        MainFrame frame = new MainFrame("MC donald");
        if (!frame.isClosed()) 
            setFrame(frame);
         else 
            System.exit(0);
    }


    // umí si načíst nastavení
    private static void loadSetting() {
        Setting setting = Setting.getSetting();

        boolean buildSetting = false;
        File settFile = new File(System.getenv("APPDATA") + File.separator + "mc_db" + File.separator + "setting" + File.separator + "config.conf");

        if (!settFile.exists()) {
            settFile = new File("." + File.separator + "config.conf");
            buildSetting = true;
        }

        try {
            List<String> data = ImportFromTXT.importTXT(settFile.getAbsolutePath());

            for (String string : data) {
                List<String> parsed = ImportFromTXT.parse(string);

                setting.setAttribute(parsed.get(0), parsed.get(1));
            }
        } catch (Exception e) {
            // TODO
        }

        if (buildSetting) {
            saveSetting();
        }
    }

    // umí uložit nastavení
    public static void saveSetting() {
        File dirFile = new File(System.getenv("APPDATA") + File.separator + "mc_db" + File.separator + "setting");

        File settFile = null;
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        settFile = new File(dirFile.getAbsolutePath() + File.separator + "config.conf");

        try {
            List<String> data = new LinkedList<>();

            for (String string : Setting.getSetting()) {
                data.add(string + ImportFromTXT.defSeparator + Setting.getSetting().getAttribute(string));
            }

            ExportToTXT.export(settFile.getAbsolutePath(), data, false);
        } catch (Exception e) {
            // TODO
        }
    }

    // schopnosti
    //  připojit se na server
    //  odpojit se ze servru
    //  poslat zprávu na server
    //  přihlas se na server
    //  odpoj se ze servru

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
