package data.db.buildesr;

import java.sql.SQLException;

import data.db.models.ProductsImprovement;

public class ProductsImprovementBuilder extends ABuilder {
    @Override
    public ProductsImprovement build() throws SQLException {
        return new ProductsImprovement(rs.getInt(ProductsImprovement.product), rs.getInt(ProductsImprovement.improvement));
    }
}
