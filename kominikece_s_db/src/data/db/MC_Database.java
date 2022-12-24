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

    public Integer getNextToken() {
        return requester.getNextToken();
    }

    // ###################################################################//

    private Map<Integer, Product> products = null;
    private Map<Integer, Category> categories = null;
    private Map<Integer, Improvement> improvements = null;

    private Runnable loadCategory = () -> {
        categories = new TreeMap<>();

        try {
            for (ADatabaseEntry c : read(new Category())) {
                if (c.isAvailable()) {
                    synchronized (categories) {
                        categories.put(c.getId(), ((Category) c));
                    }                    
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
                if (i.isAvailable()) {
                    synchronized (improvements) {
                        improvements.put(i.getId(), ((Improvement) i));
                    }                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (ADatabaseEntry iic : read(new ImprovementInCategory())) {
                synchronized (improvements) {
                    improvements.get(((ImprovementInCategory) iic).getImprovementId()).addCategories(((ImprovementInCategory) iic).getCategoryId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
    private Runnable loadProduct = () -> {
        products = new TreeMap<>();

        try {
            for (ADatabaseEntry p : read(new Product())) {
                if (p.isAvailable()) {
                    synchronized (products) {
                        products.put(p.getId(), ((Product) p));
                    }                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (ADatabaseEntry pi : read(new ProductsImprovement())) {
                synchronized (products) {
                    products.get(((ProductsImprovement) pi).getProductId()).addImprovement(((ProductsImprovement) pi).getImprovementId());
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

    public void addProduct(ADatabaseEntry product, Object token) {
        Object requestToken = requester.getNextToken();

        Thread creatThread = new Thread(new CreateThread(products, product, requestToken, token));

        creatThread.start();

        create(product, requestToken);
    }

    public Product removeProduct(Product product, Object token) {
        if (product == null) 
            return null;

        product.setAvailable(false);
        updeteData(product, token);

        synchronized(products) {
            return products.remove(product.getId());
        }
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

    public void addCategory(ADatabaseEntry category, Object token) {
        Object requestToken = requester.getNextToken();

        Thread creatThread = new Thread(new CreateThread(categories, category, requestToken, token));

        creatThread.start();

        create(category, requestToken);
    }

    public Category removeCategory(Category category, Object token) {
        if (category == null)
            return null;

        category.setAvailable(false);
        updeteData(category, token);

        synchronized(categories) {
            return categories.remove(category.getId());
        }
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

    public void addImprovement(ADatabaseEntry improvement, Object token) {
        Object requestToken = requester.getNextToken();

        Thread creatThread = new Thread(new CreateThread(improvements, improvement, requestToken, token));

        creatThread.start();

        create(improvement, requestToken);
    }

    public Improvement removeImprovement(Improvement improvement, Object token) {
        if (improvement == null)
            return null;

        improvement.setAvailable(false);
        updeteData(improvement, token);

        synchronized(improvements) {
            return improvements.remove(improvement.getId());
        }
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
     * 
     * @param aDatabaseEntry data, která mají být aktualizována
     * @param token          pokud bude null, tak se metoda provede bez jaké koliv
     *                       odpovědi.
     *                       pokud bude {@param token} nějaké hodnoty, vlákno
     *                       DatabaseRequester probudí všechna vlákna, která čekají
     *                       na tomto monitoru a pod tímto objektem ho přidá do mapy
     *                       odpovědí. Obpověď jde získat zavoláním metody
     *                       get{@code getResponce} na objektu databáze.
     */
    public void updeteData(ADatabaseEntry aDatabaseEntry, Object token) {
        requester.addRequest(new SQLRequest(SQLRequest.update, aDatabaseEntry, token));
    }

    /**
     * Metoda slouží pro odebrání dat z databáze.
     * 
     * @param aDatabaseEntry data, která mají být odebrána
     * @param token          pokud bude null, tak se metoda provede bez jaké koliv
     *                       odpovědi.
     *                       pokud bude {@param token} nějaké hodnoty, vlákno
     *                       DatabaseRequester probudí všechna vlákna, která čekají
     *                       na tomto monitoru a pod tímto objektem ho přidá do mapy
     *                       odpovědí. Obpověď jde získat zavoláním metody
     *                       get{@code getResponce} na objektu databáze.
     */
    public void removeData(ADatabaseEntry aDatabaseEntry, Object token) {
        requester.addRequest(new SQLRequest(SQLRequest.delete, aDatabaseEntry, token));
    }

    /**
     * Metoda slouží pro přidání dat do databáze.
     * 
     * @param aDatabaseEntry data, která mají být přidána
     * @param token          pokud bude null, tak se metoda provede bez jaké koliv
     *                       odpovědi.
     *                       pokud bude {@param token} nějaké hodnoty, vlákno
     *                       DatabaseRequester probudí všechna vlákna, která čekají
     *                       na tomto monitoru a pod tímto objektem ho přidá do mapy
     *                       odpovědí. Obpověď jde získat zavoláním metody
     *                       get{@code getResponce} na objektu databáze.
     */
    public void create(ADatabaseEntry aDatabaseEntry, Object token) {

        requester.addRequest(new SQLRequest(SQLRequest.create, aDatabaseEntry, token));
    }

    private class CreateThread implements Runnable {

        private Object requestToken, token;
        private ADatabaseEntry created;
        private Map<Integer, ADatabaseEntry> coll;

        public CreateThread(Map<Integer, ? extends ADatabaseEntry> coll, ADatabaseEntry created, Object requestToken, Object token) {
            this.token = token;
            this.requestToken = requestToken;
            this.created = created;
            this.coll = (Map<Integer, ADatabaseEntry>) coll;
        }

        @Override
        public void run() {
            synchronized(requestToken) {
                try {
                    requestToken.wait();
                } catch (Exception e) {
                }

                SQLResponce responce = requester.getResponce(requestToken);

                created.setId((Integer) responce.getPrimaryKey().get(ADatabaseEntry.ids));
                synchronized(coll) {
                    coll.put((Integer) responce.getPrimaryKey().get(ADatabaseEntry.ids), created);
                }
            }

            synchronized(token) {
                token.notifyAll();
            }
        }
    }
}
