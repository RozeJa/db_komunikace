package data.db.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ImprovementInCategory extends ADatabaseEntity implements SubTable, Comparable<ImprovementInCategory> {
    private int improvementId, categoryId;

    public static final String improvement = "vylepseni", category = "kategorie";
    
    public ImprovementInCategory(int improvementId, int categoryId) {
        this.improvementId = improvementId;
        this.categoryId = categoryId;

        availableVal = true;
    }
    public ImprovementInCategory() {}

    public int getImprovementId() {
        return improvementId;
    }
    public void setImprovementId(int improvementId) {
        this.improvementId = improvementId;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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
        return improvement + " = ? , " + category + " = ? ";
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
        ps.setInt(2, categoryId);
        return ps;
    }

    @Override
    public Map<String, ?> getPrimaryKeyFromResultSet(ResultSet rs) throws SQLException {
        Map<String, String> primaryKey = new HashMap<>();

        if (rs.next()) {
            primaryKey.put(improvement, rs.getString(improvement));
            primaryKey.put(category, rs.getString(category));
        }

        return primaryKey;
    }

    @Override
    public String getPrimaryKey() {
        return improvement + " = ?  AND " + category + " = ?";
    }

    @Override
    public String getTable() {
        return "VylepseniProKetegorie";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new ImprovementInCategory(improvementId, categoryId);
    }
    @Override
    public int getOwnedId() {
        return getCategoryId();
    }
    @Override
    public void setOwnedId(int ownedId) {
        categoryId = ownedId;
    }
    @Override
    public int getOwnerId() {
        return getImprovementId();
    }
    @Override
    public void setOwnerId(int ownerId) {
        improvementId = ownerId;
    }
    @Override
    public String getOwnerIdPropertyName() {
        return improvement;
    }

    @Override
    public int compareTo(ImprovementInCategory o) {
        return this.toString().compareTo(o.toString());
    }
}
