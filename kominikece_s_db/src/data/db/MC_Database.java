package data.db;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import data.Setting;
import data.db.models.IDatabaseEntry;
import data.db.models.ADatabaseEntity;
import data.db.models.CategoryEntity;
import data.db.models.Composite;
import data.db.models.ImprovementEntity;
import data.db.models.ImprovementInCategory;
import data.db.models.ProductEntity;
import data.db.models.ProductsImprovement;
import data.db.models.SubTable;

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

    public Object getNextToken() {
        return requester.getNextToken();
    }

    // ###################################################################//

    private Map<Integer, ProductEntity> products = null;
    private Map<Integer, CategoryEntity> categories = null;
    private Map<Integer, ImprovementEntity> improvements = null;

    private Runnable loadCategory = () -> {
        categories = new TreeMap<>();

        try {
            for (IDatabaseEntry c : read(new CategoryEntity())) {
                if (c.isAvailable()) {
                    synchronized (categories) {
                        categories.put(c.getId(), ((CategoryEntity) c));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
    private Runnable loadImprovement = () -> {
        improvements = new TreeMap<>();

        Thread categoryLoad = null;
        if (categories == null) {
            categoryLoad = new Thread(loadCategory);
            categoryLoad.start();
        }

        try {
            for (IDatabaseEntry i : read(new ImprovementEntity())) {
                if (i.isAvailable()) {
                    synchronized (improvements) {
                        improvements.put(i.getId(), ((ImprovementEntity) i));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (categoryLoad != null) {
            try {
                categoryLoad.join();
            } catch (Exception e) {
            }
        }

        try {
            for (IDatabaseEntry iic : read(new ImprovementInCategory())) {
                synchronized (improvements) {
                    ImprovementEntity i = improvements.get(((ImprovementInCategory) iic).getImprovementId());
                    CategoryEntity c = categories.get(((ImprovementInCategory) iic).getCategoryId());
                    if (c != null && i != null) {
                        if (i.isAvailable() && c.isAvailable())
                            i.addCategories(c.getId());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
    private Runnable loadProduct = () -> {
        products = new TreeMap<>();

        Thread improvementLoad = null;
        if (improvements == null) {
            improvementLoad = new Thread(loadImprovement);
            improvementLoad.start();
        }

        try {
            for (IDatabaseEntry p : read(new ProductEntity())) {
                if (p.isAvailable()) {
                    synchronized (products) {
                        products.put(p.getId(), ((ProductEntity) p));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (improvementLoad != null) {
            try {
                improvementLoad.join();
            } catch (Exception e) {
            }
        }

        try {
            for (IDatabaseEntry pi : read(new ProductsImprovement())) {
                synchronized (products) {
                    ProductEntity p = products.get(((ProductsImprovement) pi).getProductId());
                    ImprovementEntity i = improvements.get(((ProductsImprovement) pi).getImprovementId());
                    if (p != null && i != null) {
                        if (p.isAvailable() && i.isAvailable())
                            p.addImprovement(((ProductsImprovement) pi).getImprovementId(), i.getCategories());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    // Akce s produkty
    public Map<Integer, ProductEntity> getProducts() {
        if (products == null) {
            loadProducts();
        }

        return products;
    }

    public void addProduct(IDatabaseEntry product, Object token) {
        Object requestToken = requester.getNextToken();

        Thread creatThread = new Thread(new CreateThread(products, product, requestToken, token));

        creatThread.start();

        create(product, requestToken, token);
    }

    public ProductEntity removeProduct(ProductEntity product, Object token) {
        if (product == null)
            return null;

        product.setAvailable(false);
        updeteData(product, token);

        synchronized (products) {
            return products.remove(product.getId());
        }
    }

    public ProductEntity getProduct(Integer id) {
        synchronized (products) {
            return products.get(id);
        }
    }

    public void loadProducts() {
        Thread t = new Thread(loadProduct);

        t.start();
    }

    // Akce s kategoriemi
    public Map<Integer, CategoryEntity> getCategories() {
        if (categories == null) {
            loadCategories();
        }

        return categories;
    }

    public void addCategory(IDatabaseEntry category, Object token) {
        Object requestToken = requester.getNextToken();

        Thread creatThread = new Thread(new CreateThread(categories, category, requestToken, token));

        creatThread.start();

        create(category, requestToken, token);
    }

    public CategoryEntity removeCategory(CategoryEntity category, Object token) {
        if (category == null)
            return null;

        category.setAvailable(false);
        updeteData(category, token);

        synchronized (categories) {
            return categories.remove(category.getId());
        }
    }

    public CategoryEntity getCategory(Integer id) {
        synchronized (categories) {
            return categories.get(id);
        }
    }

    public void loadCategories() {
        Thread t = new Thread(loadCategory);

        t.start();
    }

    // Akce s vylepšenímy
    public Map<Integer, ImprovementEntity> getImprovements() {
        if (improvements == null) {
            loadImprevements();
        }

        return improvements;
    }

    public void addImprovement(IDatabaseEntry improvement, Object token) {
        Object requestToken = requester.getNextToken();

        Thread creatThread = new Thread(new CreateThread(improvements, improvement, requestToken, token));

        creatThread.start();

        create(improvement, requestToken, token);
    }

    public ImprovementEntity removeImprovement(ImprovementEntity improvement, Object token) {
        if (improvement == null)
            return null;

        improvement.setAvailable(false);
        updeteData(improvement, token);

        synchronized (improvements) {
            return improvements.remove(improvement.getId());
        }
    }

    public ImprovementEntity getImprovement(Integer id) {
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
    public void updeteData(IDatabaseEntry aDatabaseEntry, Object token) {
        if (aDatabaseEntry instanceof Composite) {

            Thread updateThread = new Thread(() -> {

                Map<SubTable, Set<Integer>> components = (Map<SubTable, Set<Integer>>) ((Composite) aDatabaseEntry)
                        .getComponents();
                for (SubTable subTableObj : components.keySet()) {
                    WhereCondition wc = new WhereCondition(WhereCondition.Operator.NON.toString(),
                            String.valueOf(aDatabaseEntry.getId()), subTableObj.getOwnerIdPropertyName(),
                            WhereCondition.OperationOperator.EQUAL.toString());

                    Set<Integer> dbVals = new TreeSet<>();
                    try {
                        // PRO každou tabulku si získej z db to co si db myslí, že má instance mít
                        for (IDatabaseEntry sto : read(subTableObj, List.of(wc))) {
                            // pokud tu je je to v poha
                            // pokud tu není z db ho smaž
                            if (!components.get(subTableObj).contains(((SubTable) sto).getOwnedId())) {
                                requester.addRequest(new SQLRequest(SQLRequest.delete, sto, null));
                            } else {
                                dbVals.add(((SubTable) sto).getOwnedId());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // projeď to co instance má
                    // pokud to není v db přidej to tam
                    // pokud to v db je je to v pohodě
                    for (Integer subId : components.get(subTableObj)) {
                        if (!dbVals.contains(subId)) {
                            try {
                                // naklanuj si vzorový objekt
                                SubTable subTableObjClon = (SubTable) subTableObj.clone();

                                // dosaď informace
                                subTableObjClon.setOwnedId(subId);
                                subTableObjClon.setOwnerId(aDatabaseEntry.getId());

                                // zavolej uložení
                                requester.addRequest(new SQLRequest(SQLRequest.create, subTableObjClon, null));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

            updateThread.start();
        }
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
    public void removeData(IDatabaseEntry aDatabaseEntry, Object token) {
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
    public void create(IDatabaseEntry aDatabaseEntry, Object requestToken, Object token) {
        requester.addRequest(new SQLRequest(SQLRequest.create, aDatabaseEntry, requestToken));

        // poku se má zapsat do db nová hodnota je nejprve třeba zjistut, zda se mají
        // zapset data i do jiné tabulky
        // příklad produkt má může mít více vylepšení a vylepšení může být u více
        // produktů
        // Rozhranní Composite implementují právě takové Modely, které potřebují
        // podobným způsobem mapovat data
        if (aDatabaseEntry instanceof Composite) {
            Thread t = new Thread(() -> {
                // získej všechny tabulky, které je třeba vytvořit
                Map<SubTable, Set<Integer>> components = (Map<SubTable, Set<Integer>>) ((Composite) aDatabaseEntry)
                        .getComponents();
                // projdi každou tabulku
                for (SubTable subTableObj : components.keySet()) {
                    // a každou hodnotu v tabulce
                    for (Integer subId : components.get(subTableObj)) {
                        try {
                            // naklanuj si vzorový objekt
                            SubTable subTableObjClon = (SubTable) subTableObj.clone();

                            // dosaď informace
                            subTableObjClon.setOwnedId(subId);
                            // počkej dokud nebude mít id
                            synchronized (aDatabaseEntry) {
                                while (aDatabaseEntry.getId() == 0) {
                                    try {
                                        aDatabaseEntry.wait();
                                    } catch (Exception e) {
                                    }
                                }
                            }
                            subTableObjClon.setOwnerId(aDatabaseEntry.getId());

                            // zavolej uložení
                            requester.addRequest(new SQLRequest(SQLRequest.create, subTableObjClon, null));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                synchronized (token) {
                    token.notifyAll();
                }
            });
            t.start();
        }
    }

    private class CreateThread implements Runnable {

        private Object requestToken, token;
        private IDatabaseEntry created;
        private Map<Integer, IDatabaseEntry> coll;

        public CreateThread(Map<Integer, ? extends IDatabaseEntry> coll, IDatabaseEntry created, Object requestToken,
                Object token) {
            this.requestToken = requestToken;
            this.created = created;
            this.coll = (Map<Integer, IDatabaseEntry>) coll;
            this.token = token;
        }

        @Override
        public void run() {
            synchronized (requestToken) {
                try {
                    requestToken.wait();
                } catch (Exception e) {
                }

                SQLResponce responce = requester.getResponce(requestToken);

                created.setId((Integer) responce.getPrimaryKey().get(ADatabaseEntity.ids));
                synchronized (coll) {
                    coll.put((Integer) responce.getPrimaryKey().get(ADatabaseEntity.ids), created);
                }
            }

            if (created instanceof Composite) {
                synchronized (created) {
                    created.notifyAll();
                }
            } else {
                synchronized (token) {
                    token.notifyAll();
                }
            }

        }
    }
}
