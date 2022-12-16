package data.db.buildesr;

import java.sql.SQLException;

import data.db.models.Improvement;

public class ImprovementBuilder extends ABuilder {
    @Override
    public Improvement build() throws SQLException {
        return new Improvement(rs.getInt(Improvement.ids), rs.getString(Improvement.name), rs.getDouble(Improvement.price), rs.getBoolean(Improvement.available));
    }
}
