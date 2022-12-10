package data;

import java.sql.ResultSet;

public class Database {
  private static Database db;

  public static void init(Setting setting) throws NullPointerException {
    if (setting == null) 
      throw new NullPointerException("Setting was not inicialiced.");

    db = new Database(setting);
  }
 
  public static Database getDB() throws NullPointerException {
    if (db == null)
      throw new NullPointerException("Database was not inicialiced.");

    return db;
  }
 
  private Database(Setting setting) {
    // TODO: 
  }
 
  public ResultSet read(String table, String sqlCondition) {
    // TODO: 

    return null;
  }
 
  public boolean create(String table, IDatabaseEntry entry) {
    // TODO:

    return false;
  }
 
  public boolean update(String table, IDatabaseEntry entry) {
    // TODO:

    return false;
  }
 
  public boolean delete(String table, IDatabaseEntry entry) {
    // TODO:

    return false;
  }
}
