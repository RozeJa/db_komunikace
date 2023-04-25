package data.db.buildesr;

import java.sql.SQLException;

import data.db.models.ADatabaseEntity;
import data.db.models.ProductEntity;

public class ProductBuilder extends ABuilder {
    @Override
    public ProductEntity build() throws SQLException {
        return new ProductEntity(rs.getInt(ADatabaseEntity.ids), rs.getString(ProductEntity.name), rs.getDouble(ProductEntity.price), rs.getInt(ProductEntity.category), rs.getBoolean(ADatabaseEntity.available));
    }
}
