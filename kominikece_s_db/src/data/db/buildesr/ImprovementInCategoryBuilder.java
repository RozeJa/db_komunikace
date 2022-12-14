package data.db.buildesr;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import data.db.models.ADatabaseEntry;

public class ImprovementInCategoryBuilder extends ABuilder {
    @Override
    public ADatabaseEntry build() throws SQLException {
        return new ImprovementInCategory(rs.getInt(ImprovementInCategory.improvement), rs.getInt(ImprovementInCategory.category));
    }

    public class ImprovementInCategory extends ADatabaseEntry {
        private int improvementId, categoryId;

        private static final String improvement = "vylepseni", category = "kategorie";
        
        public ImprovementInCategory(int improvementId, int categoryId) {
            this.improvementId = improvementId;
            this.categoryId = categoryId;

            availableVal = true;
        }

        @Override
        public String getCreateSQL() {
            return "(" + improvement + ", " + category + ") VALUES (?, ?)";
        }
        @Override
        public String getReadSQL() {
            return improvement + ","  + category;
        }
        @Override
        public String getUpdateSQL() {
            return improvement + " = ?, " + category + " = ?";
        }
        
        @Override
        public PreparedStatement fillCreateSQL(PreparedStatement ps) throws SQLException {
            return fill(ps);
        }
        @Override
        public PreparedStatement fillUpdateSQL(PreparedStatement ps) throws SQLException {
            return fill(ps);
        }
        
        private PreparedStatement fill(PreparedStatement ps) throws SQLException {
            ps.setInt(1, improvementId);
            ps.setInt(2, categoryId);
            return ps;
        }

        @Override
        public String getPrimaryKey() {
            return category + " == " + categoryId + "AND " + improvement + " == " + improvementId;
        }

        @Override
        public String getTable() {
            return "VylepseniProKategorie";
        }

    }
}
