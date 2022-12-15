package data.db.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;

// Třída je předken všech modelů, které reprezentují data v databázi
public abstract class ADatabaseEntry {
   // definuje vlastnosti používané u velkého množství modelů
   protected int id = 0;
   protected boolean availableVal;
   protected static final String ids = "id", available = "availablev";

   public int getId() {
      return id;
   }
 
   public void setAvailable(boolean availableVal) {
      this.availableVal = availableVal;
   }

   public boolean isAvailable() {
      return availableVal;
   }

   /**
    * 
    * @return vrací string, který se dá dosadit do SQL dotazu, jako primární klíč
    */
   public abstract String getPrimaryKey();
   /**
    * 
    * @return vrací název tabulky, kterou model reprezentuje
    */
   public abstract String getTable();

   /**
    * 
    * @return vrací string, který se dá dosadit do SQL dotazu, za hodnoty, které budou aktualizovány
    */
   public abstract String getUpdateSQL();
   /**
    * 
    * @return vrací string, který se dá dosadit do SQL dotazu, za hodnoty, které jsou třeba k vytvoření
    */
   public abstract String getCreateSQL();
   /**
    * 
    * @return vrací string, který se dá dosadit do SQL dotazu, za názvy sloupečků, které se mají získat
    */
   public abstract String getReadSQL();

   /**
    * Metoda doplní do {@param ps} hodnoty, které budou aktualizovány
    * @param ps připravený dotaz
    * @return vyplněný připravený dotaz
    * @throws SQLException pokud nastane chyba při doplňování
    */
   public abstract PreparedStatement fillUpdateSQL(PreparedStatement ps) throws SQLException;
   /**
    * Metoda doplní do {@param ps} hodnoty, které budou vytvářeny
    * @param ps připravený dotaz
    * @return vyplněný připravený dotaz
    * @throws SQLException pokud nastane chyba při doplňování
    */
   public abstract PreparedStatement fillCreateSQL(PreparedStatement ps) throws SQLException;
}
