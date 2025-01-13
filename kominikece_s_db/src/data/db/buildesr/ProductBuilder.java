package data.db.buildesr;

import java.sql.SQLException;

import data.db.models.ADatabaseEntry;
import data.db.models.Product;

public class ProductBuilder extends ABuilder {
    @Override
    public Product build() throws SQLException {
        return new Product(rs.getInt(ADatabaseEntry.ids), rs.getString(Product.name), rs.getDouble(Product.price), rs.getInt(Product.category), rs.getBoolean(ADatabaseEntry.available));
    }
}
