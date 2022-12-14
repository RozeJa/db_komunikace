package data.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import data.Setting;
import data.db.models.ADatabaseEntry;

public class Database {

  private static Database db;
  private Connection conection = null;

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
      try {
          Class.forName("com.mysql.jdbc.Driver");
      } catch (Exception e) {
      }

      try {
          //"jdbc:mysql://localhost:3306/mydb"
          conection = DriverManager.getConnection(setting.getAttribute(Setting.db_url), setting.getAttribute(Setting.usr), setting.getAttribute(Setting.pw));
      } catch (Exception e) {
      }
  }
 
  public ABuider read(ADatabaseEntry type, List<WhereCondition> conditions) throws SQLException {
    StringBuilder sqlRequest = new StringBuilder("SELECT " + type.getReadSQL() + " FROM " + type.getTable()); 
    
    if (!conditions.isEmpty()) {
      sqlRequest.append("WHERE");
    }

    for (WhereCondition whereCondition : conditions) {
      sqlRequest.append(whereCondition.getCondition());
    }

    PreparedStatement ps = conection.prepareStatement(sqlRequest.toString());

    return getBuilder(ps.executeQuery(), type);
  }
 
  public boolean create(String table, ADatabaseEntry entry) {
    String sqlRequest = "INSERT INTO " + table + entry.getCreateSQL();

    try (PreparedStatement ps = conection.prepareStatement(sqlRequest.toString())) {
      
      
      return entry.fillCreateSQL(ps).executeUpdate() == 1;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
 
  public boolean update(String table, ADatabaseEntry entry) {
    StringBuilder sqlRequest = new StringBuilder("UPDATE " + table + " SET" + entry.getUpdateSQL() + "WHERE " + entry.getPrimaryKey() + " LIMIT 1");
    
    try (PreparedStatement ps = conection.prepareStatement(sqlRequest.toString())) {

      return entry.fillUpdateSQL(ps).executeUpdate() == 1;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean delete(String table, ADatabaseEntry entry) {
    String sqlRequest = "DELETE FROM " + table + "WHERE" + entry.getPrimaryKey() + "LIMIT 1";

    try (PreparedStatement ps = conection.prepareStatement(sqlRequest)) {
        ps.executeUpdate();

        return ps.executeUpdate() == 1;
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }
  }
}
