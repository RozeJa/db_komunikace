package data;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import data.io.txt.ExportToTXT;
import data.io.txt.ImportFromTXT;

public class Setting implements Iterable<String> {
    private static Setting instance;

    public static final String db_url = "db_url", usr = "usr", pw = "pw", enable_asyn_db_thread = "enable_asyn_db_thread"; 
    public static final String width = "width", height = "height", fullscrean = "fullscrean"; 

    private Setting() {}

    public static Setting getSetting() {
        if (instance == null) 
            instance = new Setting();
        return instance;
    }

    private Map<String, String> settingMap = new HashMap<>();

    public String getAttribute(String key) {
        return settingMap.get(key);
    }

    public void setAttribute(String key, String value) {
        settingMap.put(key, value);
    }

    @Override
    public Iterator<String> iterator() {
        return settingMap.keySet().iterator();
    }

    
    // umí si načíst nastavení
    public static void loadSetting() {
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
        }
    }
}
