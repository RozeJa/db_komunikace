package data.db.buildesr;

import java.sql.SQLException;

import data.db.models.ADatabaseEntry;
import data.db.models.Improvement;

public class ImprovementBuilder extends ABuilder {
    @Override
    public ADatabaseEntry build() throws SQLException {
        return new Improvement(rs.getInt("id"), rs.getString("nazev"), rs.getDouble("cena"), rs.getBoolean("dostupne"));
    }
}
