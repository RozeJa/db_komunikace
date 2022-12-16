package data.db.buildesr;

import java.sql.SQLException;

import data.db.models.Product;

public class ProductBuilder extends ABuilder {
    @Override
    public Product build() throws SQLException {
        return new Product(rs.getInt(Product.ids), rs.getString(Product.name), rs.getDouble(Product.price), rs.getBoolean(Product.available));
    }
}
