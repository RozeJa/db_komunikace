package data.db.buildesr;

import java.sql.SQLException;

import data.db.models.ADatabaseEntry;

public class ImprovementInCategoryBuilder extends ABuilder {
    @Override
    public ADatabaseEntry build() throws SQLException {
        return new ImprovementInCategory(rs.getInt("vylepseni"), rs.getInt("kategorie"));
    }

    public class ImprovementInCategory extends ADatabaseEntry {
        private int improvementId, categoryId;

        private static final String improvement = "vylepseni", category = "kategorie";
        
        public ImprovementInCategory(int improvementId, int categoryId) {
            this.improvementId = improvementId;
            this.categoryId = categoryId;

            createdList.add(category);
            createdList.add(improvement);

            available = true;
        }

        @Override
        public String getPrimaryKey() {
            return category + " == " + categoryId + "AND " + improvement + " == " + improvementId;
        }

    }
}
