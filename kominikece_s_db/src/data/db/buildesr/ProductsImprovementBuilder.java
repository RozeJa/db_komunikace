package data.db.buildesr;

import java.sql.SQLException;

import data.db.models.ADatabaseEntry;

public class ProductsImprovementBuilder extends ABuilder {
    @Override
    public ADatabaseEntry build() throws SQLException {
        return new ProductsImprovement(rs.getInt("vylepseni"), rs.getInt("produkt"));
    }

    public class ProductsImprovement extends ADatabaseEntry {
        private int productId, improvementId;

        private static final String product = "produkt", improvement = "vylepseni";
        
        public ProductsImprovement(int productId, int improvementId) {
            this.productId = productId;
            this.improvementId = improvementId;
            
            createdList.add(product);
            createdList.add(improvement);
            
            available = true;
        }

        @Override
        public String getPrimaryKey() {
            return product + " == " + productId + "AND " + improvement + " == " + improvementId;
        }
    }
}
