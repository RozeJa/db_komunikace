package data.db;

import java.util.Map;

import data.Setting;
import data.db.models.Category;
import data.db.models.Improvement;
import data.db.models.Product;

public class MC_Database extends Database {

  //###################################################################//
  // Část pro singleton

    private static MC_Database db;

    /**
     * Metoda slouží pro inicializaci databáze
     * @param setting nastavení
     * @throws NullPointerException pokud je {@param setting} {@value null} 
     * @throws UnsupportedOperationException pokud již instance existuje
     */
    public static void init(Setting setting) throws NullPointerException {
        if (setting == null) 
            throw new NullPointerException("Setting was not inicialiced.");

        if (db != null) 
            throw new UnsupportedOperationException();

        db = new MC_Database(setting);
    }
    
    /**
     * @return instaci MC_Database
     * @throws NullPointerException pokud databáze nebyla inicializovaná
     */
    public static MC_Database getDB() throws NullPointerException {
        if (db == null)
            throw new NullPointerException("Database was not inicialiced.");

        return db;
    }

    private MC_Database(Setting setting) {
        super(setting);
    }


    //###################################################################//

    private Map<Integer, Product> products = null;
    private Map<Integer, Category> categories = null;
    private Map<Integer, Improvement> improvements = null;

    
}
