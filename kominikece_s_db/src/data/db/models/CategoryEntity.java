package data.db.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CategoryEntity extends ADatabaseEntity {

   public static final String name = "nazev";
   private String nameVal;

   public CategoryEntity(int id, String name, boolean availableVal) {
      nameVal = name;

      this.availableVal = availableVal;
      this.id = id;
   }
   public CategoryEntity() {}

   public void setName(String name) {
      nameVal = name;
   }
 
   public String getName() {
      return nameVal;
   }

   @Override
   public String getCreateSQL() {
       return "(" + ids + ", " + name + ", " + available + ") VALUES (?, ?, ?)";
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
      ps.setInt(1, id);
      ps.setString(2, nameVal);
      ps.setBoolean(3, availableVal);
      return ps;
   }

   @Override
   public PreparedStatement fillUpdateSQL(PreparedStatement ps) throws SQLException {
      ps.setString(1, nameVal);
      ps.setBoolean(2, availableVal);
      ps.setInt(3, id);
      return ps;
   }
   
   @Override
   public PreparedStatement fillDeleteSQL(PreparedStatement ps) throws SQLException {
        ps.setInt(1, id);
        return ps;
   }

   @Override
   public String getPrimaryKey() {
        return  ids + " = ? ";
   }

   @Override
   public String getTable() {
       return "Kategorie";
   }

   public static String[] getPropertyes() {
      return new String[] {"id", "název kategorie"};
   }
}
