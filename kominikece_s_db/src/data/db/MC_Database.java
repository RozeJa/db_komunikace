package data.db;

import java.util.Map;
import java.util.TreeMap;

import data.Setting;
import data.db.buildesr.ABuilder;
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

    
    public Map<Integer, Product> getProducts() {
        if (products == null) {
            loadProducts();
        }

        return products;
    }
    public void addProduct(Product product) {
        
    }
    public void updeteProduct(Product product) {

    }
    public void removeProduct(Product product) {
        
    }
    public Product getProduct(Integer id) {
        return null;
    }


    public Map<Integer, Category> getCategories() {
        if (categories == null) {
            loadCategories();
        }

        return categories;
    }
    public void addCategory(Category product) {
        
    }
    public void updeteCategory(Category product) {

    }
    public void removeCategory(Category product) {
        
    }
    public Category getCategory(Integer id) {
        return null;
    }


    public Map<Integer, Improvement> getImprovements() {
        if (improvements == null) {
            loadImprevements();
        }

        return improvements;
    }
    public void addImprovement(Improvement product) {
        
    }
    public void updeteImprovement(Improvement product) {

    }
    public void removeImprovement(Improvement product) {
        
    }
    public Product getImprovement(Integer id) {
        return null;
    }

}
