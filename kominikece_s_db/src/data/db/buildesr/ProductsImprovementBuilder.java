package data.db.buildesr;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import data.db.models.ADatabaseEntry;

public class ProductsImprovementBuilder extends ABuilder {
    @Override
    public ADatabaseEntry build() throws SQLException {
        return new ProductsImprovement(rs.getInt(ProductsImprovement.improvement), rs.getInt(ProductsImprovement.product));
    }

    public class ProductsImprovement extends ADatabaseEntry {
        private int productId, improvementId;

        private static final String product = "produkt", improvement = "vylepseni";
        
        public ProductsImprovement(int productId, int improvementId) {
            this.productId = productId;
            this.improvementId = improvementId;
            
            availableVal = true;
        }

        @Override
        public String getCreateSQL() {
            return "(" + improvement + ", " + product + ") VALUES (?, ?)";
        }
        @Override
        public String getReadSQL() {
            return improvement + ","  + product;
        }
        @Override
        public String getUpdateSQL() {
            return improvement + " = ?, " + product + " = ?";
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
            ps.setInt(2, productId);
            return ps;
        }

        @Override
        public String getPrimaryKey() {
            return product + " == " + productId + "AND " + improvement + " == " + improvementId;
        }

        @Override
        public String getTable() {
            return "VylepseniProduktu";
        }
    }
}
