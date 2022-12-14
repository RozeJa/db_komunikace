package data.db.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class ADatabaseEntry {

   protected int id;
   protected static final String ids = "id", available = "availablev";
   protected boolean availableVal;

   public int getId() {
      return id;
   }
 
   public void setAvailable(boolean availableVal) {
      this.availableVal = availableVal;
   }

   public boolean isAvailable() {
      return availableVal;
   }

   public abstract String getPrimaryKey();
   public abstract String getTable();

   public abstract String getUpdateSQL();
   public abstract String getCreateSQL();
   public abstract String getReadSQL();

   public abstract PreparedStatement fillUpdateSQL(PreparedStatement ps) throws SQLException;
   public abstract PreparedStatement fillCreateSQL(PreparedStatement ps) throws SQLException;
}
