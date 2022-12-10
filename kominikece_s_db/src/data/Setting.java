package data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Setting implements Iterable<String> {
    private static Setting instance;

    // TODO:
    public static final String db_url = "db_url"; 

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
