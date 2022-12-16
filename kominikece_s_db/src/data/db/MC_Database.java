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
     * Metoda update je obecná pro všechny.
     * @param aDatabaseEntry data, která mají být aktualizována
     * @param token pokud bude null, tak se metoda provede bez jaké koliv odpovědi.
     * pokud bude {@param token} nějaké hodnoty, vlákno DatabaseRequester probudí všechna vlákna, která čekají na tomto monitoru a pod tímto objektem ho přidá do mapy odpovědí. Obpověď jde získat zavoláním metody get{@code getResponce} na objektu databáze. 
     */
    public void updeteData(ADatabaseEntry aDatabaseEntry, Object token) {
        if (token != null) {
            requester.addRequest(new SQLRequest(SQLRequest.update, aDatabaseEntry, token)); 
        } else {
            requester.addRequest(new SQLRequest(SQLRequest.update, aDatabaseEntry)); 
        }
    }
    
    /**
     * Metoda slouží pro odebrání dat z databáze.
     * @param aDatabaseEntry data, která mají být odebrána
     * @param token pokud bude null, tak se metoda provede bez jaké koliv odpovědi.
     * pokud bude {@param token} nějaké hodnoty, vlákno DatabaseRequester probudí všechna vlákna, která čekají na tomto monitoru a pod tímto objektem ho přidá do mapy odpovědí. Obpověď jde získat zavoláním metody get{@code getResponce} na objektu databáze. 
     */
    public void removeData(ADatabaseEntry aDatabaseEntry, Object token) {
        if (token != null) {
            requester.addRequest(new SQLRequest(SQLRequest.delete, aDatabaseEntry, token));
        } else {
            requester.addRequest(new SQLRequest(SQLRequest.delete, aDatabaseEntry));
        }
    }

    /**
     * Metoda slouží pro přidání dat do databáze.
     * @param aDatabaseEntry data, která mají být přidána
     * @param token pokud bude null, tak se metoda provede bez jaké koliv odpovědi.
     * pokud bude {@param token} nějaké hodnoty, vlákno DatabaseRequester probudí všechna vlákna, která čekají na tomto monitoru a pod tímto objektem ho přidá do mapy odpovědí. Obpověď jde získat zavoláním metody get{@code getResponce} na objektu databáze. 
     */
    public void create(ADatabaseEntry aDatabaseEntry, Object token) {
        if (token != null) {
            requester.addRequest(new SQLRequest(SQLRequest.create, aDatabaseEntry, token));
        } else {
            requester.addRequest(new SQLRequest(SQLRequest.create, aDatabaseEntry));
        }
    }
}
