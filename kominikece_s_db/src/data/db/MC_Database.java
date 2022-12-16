package data.db;

import java.util.Map;
import java.util.TreeMap;

import data.Setting;
import data.db.buildesr.ABuilder;
import data.db.models.ADatabaseEntry;
import data.db.models.Category;
import data.db.models.Improvement;
import data.db.models.Product;

public class MC_Database extends Database {

    // ###################################################################//
    // Část pro singleton

    private static MC_Database db;

    /**
     * Metoda slouží pro inicializaci databáze
     * 
     * @param setting nastavení
     * @throws NullPointerException          pokud je {@param setting} {@value null}
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

    // ###################################################################//

    private Map<Integer, Product> products = null;
    private Map<Integer, Category> categories = null;
    private Map<Integer, Improvement> improvements = null;

    // Akce s produkty
    public Map<Integer, Product> getProducts() {
        if (products == null) {
            loadProducts();
        }

        return products;
    }
    /**
     * Metada přidá do db objekt a z db ho načte
     * @param product 
     */
    public void addProduct(Product product) {
        // TODO: 
    }
    public void removeProduct(Product product, Object token) {
        product.setAvailable(false);
        updeteData(product, token);
    }
    public Product getProduct(Integer id) {
        synchronized (products) {
            return products.get(id);
        }
    }
    public void loadProducts() {
        // TODO: 
    }

    // Akce s kategoriemi
    public Map<Integer, Category> getCategories() {
        if (categories == null) {
            loadCategories();
        }

        return categories;
    }
    public void addCategory(Category category) {
        // TODO: 
    }
    public void removeCategory(Category category, Object token) {
        category.setAvailable(false);
        updeteData(category, token);
    }
    public Category getCategory(Integer id) {
        synchronized (categories) {
            return categories.get(id);
        }
    }
    public void loadCategories() {
        // TODO: 
    }

    // Akce s vylepšenímy
    public Map<Integer, Improvement> getImprovements() {
        if (improvements == null) {
            loadImprevements();
        }

        return improvements;
    }
    public void addImprovement(Improvement improvement) {
        // TODO: 
    }
    public void removeImprovement(Improvement improvement, Object token) {
        improvement.setAvailable(false);
        updeteData(improvement, token);
    }
    public Improvement getImprovement(Integer id) {
        synchronized (improvements) {
            return improvements.get(id);
        }
    }
    public void loadImprevements() {
        // TODO: 
    }

    /**
     * Metoda update je obecná pro všechny. Metoda nevrací žádná data ani nijak neinteraguje s okolím
     * @param aDatabaseEntry data, která mají být aktualizována
     */
    public void updeteData(ADatabaseEntry aDatabaseEntry, Object token) {
        requester.addRequest(new SQLRequest(SQLRequest.update, aDatabaseEntry, token));
    }
    
    /**
     * 
     * @param aDatabaseEntry
     * @param token
     */
    public void removeData(ADatabaseEntry aDatabaseEntry, Object token) {
        requester.addRequest(new SQLRequest(SQLRequest.delete, aDatabaseEntry, token));
    }
}
