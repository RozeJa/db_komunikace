package data.db.buildesr;

import java.sql.SQLException;

import data.db.models.ADatabaseEntry;
import data.db.models.Category;

public class CategoryBuilder extends ABuilder {
    @Override
    public Category build() throws SQLException {
        return new Category(rs.getInt(ADatabaseEntry.ids), rs.getString(Category.name), rs.getBoolean(ADatabaseEntry.available));
    }
}
