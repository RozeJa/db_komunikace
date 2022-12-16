package data.db.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Category extends ADatabaseEntry {

   public static final String name = "name";
   private String nameVal;

   public Category(int id, String name, boolean availableVal) {
      nameVal = name;

      this.availableVal = availableVal;
      this.id = id;
   }

   public void setName(String name) {
      nameVal = name;
   }
 
   public String getName() {
      return nameVal;
   }

   @Override
   public String getCreateSQL() {
       return "(" + name + ", " + available + ") VALUES (?, ?)";
   }

   @Override
   public String getReadSQL() {
       return ids + "," + name + ","  + available ;
   }

   @Override
   public String getUpdateSQL() {
       return name + " = ?, " + available + " = ?"; 
   }

   @Override
   public PreparedStatement fillCreateSQL(PreparedStatement ps) throws SQLException {
      return fill(ps);
   }

   @Override
   public PreparedStatement fillUpdateSQL(PreparedStatement ps) throws SQLException {
      return fill(ps);
   }

   private PreparedStatement fill(PreparedStatement ps) throws SQLException {
      ps.setInt(1, id);
      ps.setString(2, nameVal);
      ps.setBoolean(3, availableVal);
      return ps;
   }

   @Override
   public String getPrimaryKey() {
       return  ids + " == " + id;
   }

   @Override
   public String getTable() {
       return "Kategorie";
   }
}
