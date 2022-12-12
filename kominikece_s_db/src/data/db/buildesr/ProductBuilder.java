package data.db.buildesr;

import java.sql.SQLException;

import data.db.models.Product;

public class ProductBuilder extends ABuilder {
    @Override
    public Product build() throws SQLException {
        return new Product(rs.getInt("id"), rs.getString("nazev"), rs.getDouble("cena"), rs.getBoolean("dostupne"));
    }
}
