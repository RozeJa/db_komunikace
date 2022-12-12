package data.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
 
  public ResultSet read(String table, List<WhereCondition> conditions) throws SQLException {
    // TODO: odebrat "*" z sql dotazu a nahradit ji
    StringBuilder sqlRequest = new StringBuilder("SELECT * FROM " + table); 
    
    if (!conditions.isEmpty()) {
      sqlRequest.append("WHERE");
    }

    for (WhereCondition whereCondition : conditions) {
      sqlRequest.append(whereCondition.getCondition());
    }

    PreparedStatement ps = conection.prepareStatement(sqlRequest.toString());

    return ps.executeQuery();
  }
 
  public boolean create(String table, ADatabaseEntry entry) {
    StringBuilder sqlRequest = new StringBuilder("INSERT INTO " + table);

    sqlRequest.append("(");
    int index = 1;
    for (String string : entry.getCread()) {
      sqlRequest.append(string);

      if (index < entry.getCread().size())
      sqlRequest.append(", ");
      index++;
    }

    sqlRequest.append(") VALUES (");
    for (int i = 0; i < entry.getCread().size(); i++) {
      sqlRequest.append("?");

      if (index < entry.getCread().size())
        sqlRequest.append(", ");
    }

    sqlRequest.append(");");

    try (PreparedStatement ps = conection.prepareStatement(sqlRequest.toString())) {
      
      index = 1;
      for (String key : entry.getUpdate().keySet()) {
        setPropety(ps, entry.getUpdate(), key, index);
        index++;
      }
      
      return ps.executeUpdate() == 1;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
 
  public boolean update(String table, ADatabaseEntry entry) {
    StringBuilder sqlRequest = new StringBuilder("UPDATE " + table + " SET");
    
    int index = 1;
    for (String key : entry.getUpdate().keySet()) {
      sqlRequest.append(key);
      sqlRequest.append("= ? ");

      if (index < entry.getUpdate().size())
        sqlRequest.append(", ");
      index++;
    }

    sqlRequest.append(" WHERE ");
    sqlRequest.append(entry.getPrimaryKey());
    sqlRequest.append(" LIMIT = 1;");
  
    //(id, uziv_jmeno, heslo, zegistrovan) VALUES (?, ?, ?, ?)
    
    try (PreparedStatement ps = conection.prepareStatement(sqlRequest.toString())) {

      index = 1;
      for (String key : entry.getUpdate().keySet()) {

        setPropety(ps, entry.getUpdate(), key, index);
        index++;
      }

      return ps.executeUpdate() == 1;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean delete(String table, ADatabaseEntry entry) {
    String sqlRequest = "DELETE FROM " + table + "WHERE" + entry.getPrimaryKey() + "LIMIT = 1";

    try (PreparedStatement ps = conection.prepareStatement(sqlRequest)) {
        ps.executeUpdate();

        return ps.executeUpdate() == 1;
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }
  }

  private void setPropety(PreparedStatement ps, Map<String, String> data, String key, int index) throws SQLException {
    try {
      ps.setDouble(index, Double.parseDouble(data.get(key)));
    } catch (Exception e) {
      try {
        ps.setBoolean(index, ADatabaseEntry.parseBoolean(data.get(key)));
      } catch (Exception ex) {
        ps.setString(index, data.get(key));
      }
    } 
  }
}
