package data.db.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public interface IDatabaseEntry {
    
    int getId();
    void setId(int id);

    void setAvailable(boolean availableVal);

    boolean isAvailable();

    /**
     * 
    * @return vrací string, který se dá dosadit do SQL dotazu, jako primární klíč
    */
    String getPrimaryKey();
    /**
     * 
    * @return vrací název tabulky, kterou model reprezentuje
    */
    String getTable();

    /**
     * 
    * @return vrací string, který se dá dosadit do SQL dotazu, za hodnoty, které budou aktualizovány
    */
    String getUpdateSQL();
    /**
     * 
    * @return vrací string, který se dá dosadit do SQL dotazu, za hodnoty, které jsou třeba k vytvoření
    */
    String getCreateSQL();
    /**
     * 
    * @return vrací string, který se dá dosadit do SQL dotazu, za názvy sloupečků, které se mají získat
    */
    String getReadSQL();

    /**
     * Metoda doplní do {@param ps} hodnoty, které budou aktualizovány
    * @param ps připravený dotaz
    * @return vyplněný připravený dotaz
    * @throws SQLException pokud nastane chyba při doplňování
    */
    PreparedStatement fillUpdateSQL(PreparedStatement ps) throws SQLException;
    /**
     * Metoda doplní do {@param ps} hodnoty, které budou vytvářeny
    * @param ps připravený dotaz
    * @return vyplněný připravený dotaz
    * @throws SQLException pokud nastane chyba při doplňování
    */
    PreparedStatement fillCreateSQL(PreparedStatement ps) throws SQLException;
    /**
     * Metoda extrahuje z {@param rs} hodnoty klíčů
    * @param rs ResultSet obsahující primární klíč
    * @return vrací mapu<název vlastnosti, hodnota> 
    * @throws SQLException pokud nastane chyba při čtení
    */
    Map<String, ?> getPrimaryKeyFromResultSet(ResultSet rs) throws SQLException;
}
