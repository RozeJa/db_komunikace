package data.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import data.Setting;
import data.db.buildesr.ABuilder;
import data.db.models.IDatabaseEntry;

public class Database {

  // Třída je návrhového typu singleton
  private static Database db;
  
  // Spojení s DB
  private Connection conection = null;
  // Vlákno, které asynchroně komunikuje s DB
  protected DatabaseRequester requester = null;

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

    db = new Database(setting);
  }
 
  /**
   * @return instaci Database
   * @throws NullPointerException pokud databáze nebyla inicializovaná
   */
  public static Database getDB() throws NullPointerException {
    if (db == null)
      throw new NullPointerException("Database was not inicialiced.");

    return db;
  }

  /**
   * Dochází zde k vytvoření spojení a spuštění vlákna pro asynchronní komunikaci
   * @param setting
   */
  protected Database(Setting setting) {
      try {
          Class.forName("com.mysql.jdbc.Driver");
      } catch (Exception e) {
      }

      try {
          //"jdbc:mysql://localhost:3306/mydb"
          conection = DriverManager.getConnection(setting.getAttribute(Setting.db_url), setting.getAttribute(Setting.usr), setting.getAttribute(Setting.pw));
      } catch (Exception e) {
        e.printStackTrace();
      }

      requester = new DatabaseRequester(Boolean.parseBoolean(setting.getAttribute(Setting.enable_asyn_db_thread)));
      requester.start();
  }
 
  /**
   * Metoda umožňuje načtení dat z db
   * @param type instance modelu, může být úplně prázdná, musí rozšiřovat ADatabaseEntry
   * @param conditions list podmínek k výběru
   * @return vrací instanci Buildru pro konkrétní model. Jen nutné dodržet jmenou konvenci: model = Model, builder = ModelBuilder
   * @throws Exception pokud se nepodaří vytvořit Builder
   */
  protected ABuilder read(IDatabaseEntry type, List<WhereCondition> conditions) throws Exception {
    StringBuilder sqlRequest = new StringBuilder("SELECT " + type.getReadSQL() + " FROM " + type.getTable()); 
    
    if (!conditions.isEmpty()) {
      sqlRequest.append(" WHERE");
    }

    for (WhereCondition whereCondition : conditions) {
      sqlRequest.append(whereCondition.getCondition());
    }

    PreparedStatement ps = conection.prepareStatement(sqlRequest.toString());

    // TODO: odstraň sys.out
    System.out.println(sqlRequest.toString());
    return getBuilder(ps.executeQuery(), type);
  }

    /**
   * Metoda umožňuje načtení dat z db
   * @param type instance modelu, může být úplně prázdná, musí rozšiřovat ADatabaseEntry
   * @return vrací instanci Buildru pro konkrétní model. Jen nutné dodržet jmenou konvenci: model = Model, builder = ModelBuilder
   * @throws Exception pokud se nepodaří vytvořit Builder
   */
  protected ABuilder read(IDatabaseEntry type) throws Exception {
    return read(type, new LinkedList<>());
  }
 
  /**
   * Metoda vytvoří instanci Buildru a nastavý mu vlastnost typu ResultSet
   * @param rs ResultSet 
   * @param type instance třídy modelu, musí rozšiřovat ADatabaseEntry
   * @return vrací instanci Buildru pro konkrétní model. Jen nutné dodržet jmenou konvenci: model = Model, builder = ModelBuilder
   * @throws Exception pokud se nepodaří vytvořit Builder
   */
  private ABuilder getBuilder(ResultSet rs, IDatabaseEntry type) throws Exception {
    ABuilder builder = (ABuilder) Class.forName(type.getClass().getName().replace("models", "buildesr") + "Builder").getConstructor().newInstance();

    builder.setResultSet(rs);

    return builder;
  }

  /**
   * Metoda pro zapsání instance modelu do databáze
   * @param entry instance, musí rozšiřovat ADatabaseEntry
   * @return vrací booleanovou hodnotu, zda se zapsání podařilo
   */
  protected Map<String, ?> create(IDatabaseEntry entry) {
    String sqlRequest = "INSERT INTO " + entry.getTable() + entry.getCreateSQL();

    try (PreparedStatement ps = conection.prepareStatement(sqlRequest.toString(), Statement.RETURN_GENERATED_KEYS)) {

      entry.fillCreateSQL(ps).executeUpdate();

      return entry.getPrimaryKeyFromResultSet(ps.getGeneratedKeys());
    } catch (Exception e) {
      e.printStackTrace();
      return new TreeMap<>();
    }
  }

  /**
   * Metoda slouží pro editování hodnot v databázi
   * @param entry instance modelu, která obsahuje data
   * @return vrací booleanovou hodnotu, zda se zapsání podařilo
   */
  protected boolean update(IDatabaseEntry entry) {
    StringBuilder sqlRequest = new StringBuilder("UPDATE " + entry.getTable() + " SET " + entry.getUpdateSQL() + " WHERE " + entry.getPrimaryKey() + " LIMIT 1");
    
    try (PreparedStatement ps = conection.prepareStatement(sqlRequest.toString())) {

      return entry.fillUpdateSQL(ps).executeUpdate() == 1;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Metado slouží pro odebrání dat z databáze 
   * @param entry data, která se mají odebrat
   * @return vrací booleanovou hodnotu, zda se smazání podařilo
   */
  protected boolean delete(IDatabaseEntry entry) {
    String sqlRequest = "DELETE FROM " + entry.getTable() + " WHERE " + entry.getPrimaryKey() + " LIMIT 1";

    // TODO: odebrat sys.out
    System.out.println(sqlRequest);

    try (PreparedStatement ps = conection.prepareStatement(sqlRequest)) {
        return ps.executeUpdate() == 1;
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }
  }

  /**
   * Metoda poskytuje přístup k vypracovaným odpovědím, při získávání odpovědi dojde k jejímu odebrání z kolekce. To znamená, že je přístupná pouze jednou.
   * @param token , slouží k synchronizaci a k mapování odpovědí
   * @return pokud byl request prováděn pro read, tak vrací instanci Buildru, pokud se načítání podařilo. Vrací null, když se podařily další 3 operace. Vyvolává vyjímku, když se dotaz neprovedl správně.
   * @throws Exception když se dotaz neprovedl správně.
   */
  public ABuilder getResponce(Object token) throws Exception {
    SQLResponce responce = requester.getResponce(token);

    if (responce.isSucces()) {
      return responce.getBuilder();
    }

    throw new Exception();
  }

  //###################################################################//
  // Vnořené třídy, důležité puze pro Databázi
  //###################################################################//
  
  // Třída zajišťuje asynchronní komunikaci s databází
  protected class DatabaseRequester extends Thread {
    // list dotazů
    private List<SQLRequest> requests = new LinkedList<>();
    // mapa odpovědí
    private Map<Object, SQLResponce> responcies = new HashMap<>();
    // nastavení běhu
    private boolean run = true;
    // počítadlo requestů7
    private Integer counter = 0;

    public DatabaseRequester(boolean run) {
      setDaemon(true);
      this.run = run;
    }

    // přidání dotazu
    public void addRequest(SQLRequest request) {
      synchronized(requests) {
        requests.add(request);
        requests.notifyAll();
      }
    }

    // získání odpovědi
    public SQLResponce getResponce(Object token) {
      synchronized(responcies) {
        return responcies.remove(token);
      }
    }

    @Override
    public void run() {
      // dokud máš běžet dělej rutinu
      while (run) {
        // připrav si místo pro dotaz
        SQLRequest request = null;

        // synchronizovaně si vem dotaz
        synchronized(requests) {
          // pokud dotazy došly, čekej
          while (requests.isEmpty()) {
            try {
              requests.wait();
            } catch (Exception e) {
            }
          }

          request = requests.remove(0);
        }
        
        // vyhodnoť dotaz
        completeRequest(request);
      }
    }
    /**
     * Matoda vyhodnocuje dotaz
     * @param request dotaz
     */
    private void completeRequest(SQLRequest request) {
      // připrav si místo pro odpověď 
      SQLResponce responce = null;

      // vyber metodu a vytvoř odpověď
      switch (request.getMethod()) {
        case SQLRequest.create:
          responce = new SQLResponce(create(request.getData()));
          break;
        case SQLRequest.read:
          try {
            responce = new SQLResponce(read(request.getData(), request.getConditions()), true);
          } catch (Exception e) {
            responce = new SQLResponce(null, false);
          }
          break;
        case SQLRequest.update:
          responce = new SQLResponce(null, update(request.getData()));
          break;                
        case SQLRequest.delete:
          responce = new SQLResponce(null, delete(request.getData()));
          break;
      }

      if (request.isResponceNeaded()) {
        // přidej odpověď k dalším
        synchronized(responcies) {
          responcies.put(request.getToken(), responce);
        }
        // upozorni vlákno, které dotaz vzneslo, že má připravenou odpověď
        synchronized(request.getToken()) {
          request.getToken().notifyAll();
        }        
      }
    }

    public int getNextToken() {
      synchronized(counter) {
        return ++counter;
      }
    }

    /**
     * Metoda umožňuje ukončit vlákno
     */
    public void breakThred() {
      run = false;
    }
  }

  // Třída slouží jako nosič dat o dotazu
  protected class SQLRequest {
    public static final String create = "create", read = "read", update = "updete", delete = "delete";
    private Object token = null;
    private String method;
    private IDatabaseEntry data;
    private List<WhereCondition> whereConditions;
    private boolean responceNeaded = false;

    public SQLRequest(String method, IDatabaseEntry data, Object token) {
      this.data = data;
      this.method = method;
      if (token != null) {
        this.token = token;
        this.responceNeaded = true;
      } 
    }

    public SQLRequest(String method, IDatabaseEntry data, List<WhereCondition> whereConditions, Object token) {
      this.data = data;
      this.method = method;
      if (token != null) {
        this.token = token;
        this.whereConditions = whereConditions;
        this.responceNeaded = true;
      } 
    }
    
    public String getMethod() {
        return method;
    }
    public IDatabaseEntry getData() {
        return data;
    }
    public Object getToken() {
        return token;
    }
    public List<WhereCondition> getConditions() {
      return whereConditions;
    }
    public boolean isResponceNeaded() {
      return responceNeaded;
    }
  }

  // Třída slouží jako nosič dat o odpovědi
  protected class SQLResponce {
    private ABuilder builder;
    private boolean succes;
    private Map<String, ?> primaryKey;

    public SQLResponce(ABuilder builder, boolean succes) {
      this.builder = builder;
      this.succes = succes;
    }

    public SQLResponce(Map<String, ?> primaryKey) {
      this.succes = primaryKey == null;
      this.primaryKey = primaryKey;
    }

    public ABuilder getBuilder() {
        return builder;
    }
    
    public boolean isSucces() {
        return succes;
    }

    public Map<String, ?> getPrimaryKey() {
      return primaryKey;
    }
  }
}
