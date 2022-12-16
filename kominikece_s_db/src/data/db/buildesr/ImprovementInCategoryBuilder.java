package data.db.buildesr;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import data.db.models.ImprovementInCategory;

public class ImprovementInCategoryBuilder extends ABuilder {
    @Override
    public ImprovementInCategory build() throws SQLException {
        return new ImprovementInCategory(rs.getInt(ImprovementInCategory.improvement), rs.getInt(ImprovementInCategory.category));
    }
}
