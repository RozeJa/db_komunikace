package data.db.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ProductsImprovement extends ADatabaseEntry implements SubTable {
    private int productId, improvementId;

    public static final String product = "produkt", improvement = "vylepseni";
    
    public ProductsImprovement(int productId, int improvementId) {
        this.productId = productId;
        this.improvementId = improvementId;
        
        availableVal = true;
    }
    public ProductsImprovement() {}

    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public int getImprovementId() {
        return improvementId;
    }
    public void setImprovementId(int improvementId) {
        this.improvementId = improvementId;
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
        return improvement + " = ? , " + product + " = ? ";
    }
    
    @Override
    public PreparedStatement fillCreateSQL(PreparedStatement ps) throws SQLException {
        return fill(ps);
    }
    @Override
    public PreparedStatement fillUpdateSQL(PreparedStatement ps) throws SQLException {
        return fill(ps);
    }
    @Override
    public PreparedStatement fillDeleteSQL(PreparedStatement ps) throws SQLException {
        return fill(ps);
    }
    
    private PreparedStatement fill(PreparedStatement ps) throws SQLException {
        ps.setInt(1, improvementId);
        ps.setInt(2, productId);
        return ps;
    }

    @Override
    public Map<String, ?> getPrimaryKeyFromResultSet(ResultSet rs) throws SQLException {
        Map<String, String> primaryKey = new HashMap<>();

        if (rs.next()) {
            primaryKey.put(product, rs.getString(product));
            primaryKey.put(improvement, rs.getString(improvement));
        }

        return primaryKey;
    }

    @Override
    public String getPrimaryKey() {
        return improvement + " = ?  AND " + product + " = ?"  ;
    }

    @Override
    public String getTable() {
        return "VylepseniProduktu";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new ProductsImprovement(productId, improvementId);
    }
    @Override
    public int getOwnedId() {
        return getImprovementId();
    }
    @Override
    public void setOwnedId(int ownedId) {
        improvementId = ownedId;
    }
    @Override
    public int getOwnerId() {
        return getProductId();
    }
    @Override
    public void setOwnerId(int ownerId) {
        productId = ownerId;
    }
    @Override
    public String getOwnerIdPropertyName() {
        return product;
    }
}