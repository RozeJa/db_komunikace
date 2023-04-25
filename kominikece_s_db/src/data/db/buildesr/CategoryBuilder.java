package data.db.buildesr;

import java.sql.SQLException;

import data.db.models.ADatabaseEntity;
import data.db.models.CategoryEntity;

public class CategoryBuilder extends ABuilder {
    @Override
    public CategoryEntity build() throws SQLException {
        return new CategoryEntity(rs.getInt(ADatabaseEntity.ids), rs.getString(CategoryEntity.name),
                rs.getBoolean(ADatabaseEntity.available));
    }
}
