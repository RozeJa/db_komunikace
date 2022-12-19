package data.db.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ImprovementInCategory extends ADatabaseEntry {
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
        return category + " == " + categoryId + "AND " + improvement + " == " + improvementId;
    }

    @Override
    public String getTable() {
        return "VylepseniProKetegorie";
    }
}
