package data.db;

import java.util.Map;
import java.util.TreeMap;

import data.Setting;
import data.db.models.ADatabaseEntry;
import data.db.models.Category;
import data.db.models.Improvement;
import data.db.models.ImprovementInCategory;
import data.db.models.Product;
import data.db.models.ProductsImprovement;

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

    private Runnable loadCategory = () -> {
        categories = new TreeMap<>();

        try {
            for (ADatabaseEntry c : read(new Category())) {
                synchronized(categories) {
                    categories.put(c.getId(), ((Category) c));
                }
            }                
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
    private Runnable loadImprovement = () -> {
        improvements = new TreeMap<>();

        try {
            for (ADatabaseEntry i : read(new Improvement())) {
                synchronized(improvements) {
                    improvements.put(i.getId(), ((Improvement) i));
                }
            }       
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (categories == null) {
            Thread tr = new Thread(loadCategory);

            tr.start();
            try {
                tr.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            for (ADatabaseEntry iic : read(new ImprovementInCategory())) {
                synchronized(improvements) {
                    improvements.get(((ImprovementInCategory) iic).getImprovementId()).addCategories(categories.get(((ImprovementInCategory) iic).getCategoryId()));
                }
            }                
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
    private Runnable loadProduct = () -> {
        products = new TreeMap<>();

        try {
            for (ADatabaseEntry i : read(new Product())) {
                synchronized(products) {
                    products.put(i.getId(), ((Product) i));
                }
            }       
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (improvements == null) {
            Thread tr = new Thread(loadImprovement);

            tr.start();
            try {
                tr.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            for (ADatabaseEntry pi : read(new ProductsImprovement())) {
                synchronized(products) {
                    products.get(((ProductsImprovement) pi).getProductId()).addImprovement(improvements.get(((ProductsImprovement) pi).getImprovementId()));
                }
            }                
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

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
        Thread t = new Thread(loadProduct);
        
        t.start();
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
        Thread t = new Thread(loadCategory);

        t.start();
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
        Thread t = new Thread(loadImprovement);

        t.start();
    }

    /**
     * Metoda update je obecná pro všechny.
     * @param aDatabaseEntry data, která mají být aktualizována
     * @param token pokud bude null, tak se metoda provede bez jaké koliv odpovědi.
     * pokud bude {@param token} nějaké hodnoty, vlákno DatabaseRequester probudí všechna vlákna, která čekají na tomto monitoru a pod tímto objektem ho přidá do mapy odpovědí. Obpověď jde získat zavoláním metody get{@code getResponce} na objektu databáze. 
     */
    public void updeteData(ADatabaseEntry aDatabaseEntry, Object token) {
        requester.addRequest(new SQLRequest(SQLRequest.update, aDatabaseEntry, token));
    }
    
    /**
     * Metoda slouží pro odebrání dat z databáze.
     * @param aDatabaseEntry data, která mají být odebrána
     * @param token pokud bude null, tak se metoda provede bez jaké koliv odpovědi.
     * pokud bude {@param token} nějaké hodnoty, vlákno DatabaseRequester probudí všechna vlákna, která čekají na tomto monitoru a pod tímto objektem ho přidá do mapy odpovědí. Obpověď jde získat zavoláním metody get{@code getResponce} na objektu databáze. 
     */
    public void removeData(ADatabaseEntry aDatabaseEntry, Object token) {
        requester.addRequest(new SQLRequest(SQLRequest.delete, aDatabaseEntry, token));
    }

    /**
     * Metoda slouží pro přidání dat do databáze.
     * @param aDatabaseEntry data, která mají být přidána
     * @param token pokud bude null, tak se metoda provede bez jaké koliv odpovědi.
     * pokud bude {@param token} nějaké hodnoty, vlákno DatabaseRequester probudí všechna vlákna, která čekají na tomto monitoru a pod tímto objektem ho přidá do mapy odpovědí. Obpověď jde získat zavoláním metody get{@code getResponce} na objektu databáze. 
     */
    public void create(ADatabaseEntry aDatabaseEntry, Object token) {
        requester.addRequest(new SQLRequest(SQLRequest.create, aDatabaseEntry, token));
    }
}
