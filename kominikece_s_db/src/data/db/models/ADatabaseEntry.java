package data.db.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

// Třída je předken všech modelů, které reprezentují data v databázi
public abstract class ADatabaseEntry implements IDatabaseEntry {
   // definuje vlastnosti používané u velkého množství modelů
   protected int id = 0;
   protected boolean availableVal;
   public static final String ids = "id", available = "dostupne";

   public int getId() {
      return id;
   }
   public void setId(int id) {
      if (this.id == 0) 
         this.id = id;
   }
 
   public void setAvailable(boolean availableVal) {
      this.availableVal = availableVal;
   }

   public boolean isAvailable() {
      return availableVal;
   }

   /**
    * Metoda extrahuje z {@param rs} hodnoty klíčů
    * @param rs ResultSet obsahující primární klíč
    * @return vrací mapu<název vlastnosti, hodnota> 
    * @throws SQLException pokud nastane chyba při čtení
    */
   public Map<String, ?> getPrimaryKeyFromResultSet(ResultSet rs) throws SQLException {
        Map<String, Integer> primaryKey = new HashMap<>();
 
        if (rs.next()) {
           primaryKey.put(ids, rs.getInt(1));
        }
 
        return primaryKey;
   }
}
