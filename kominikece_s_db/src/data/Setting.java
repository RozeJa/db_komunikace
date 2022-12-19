package data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
}
