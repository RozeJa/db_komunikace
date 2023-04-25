package data.db.buildesr;

import java.sql.SQLException;

import data.db.models.ADatabaseEntity;
import data.db.models.ImprovementEntity;

public class ImprovementBuilder extends ABuilder {
    @Override
    public ImprovementEntity build() throws SQLException {
        return new ImprovementEntity(rs.getInt(ADatabaseEntity.ids), rs.getString(ImprovementEntity.name), rs.getDouble(ImprovementEntity.price), rs.getBoolean(ADatabaseEntity.available));
    }
}
