package data.db.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class ProductEntity extends ADatabaseEntity implements Iterable<Integer>, Composite {

     public static final String name = "nazev", price = "cena", category = "kategorie";
   
     private Set<Integer> improvements = new TreeSet<>();
     private int categoryId;

     private String nameVal;
     private double priceVal;

     public ProductEntity(int id, String name, double price, int categoryId, boolean availableVal) {
          nameVal = name;
          priceVal = price;
          this.categoryId = categoryId;

          this.availableVal = availableVal;
          this.id = id;
     }
     public ProductEntity() {}
   

     public void setPrice(double price) {
          priceVal = price;
     }

     public double getPrice() {
          return priceVal;
     }

     public void setName(String name) {
          nameVal = name;
     }

     public String getName() {
          return nameVal;
     }
 
     public void setCategories(int categoryId) {
          this.categoryId = categoryId;
     }
 
     public int getCategory() {
          return categoryId;
     }

     public void addImprovement(int improvementId, Set<Integer> improvementsCategoriesId) {
          if (improvementsCategoriesId.contains(categoryId))
               improvements.add(improvementId); 
     }
 
     public boolean removeImprovement(int id) {
        return improvements.remove(id);
     }
     public void removeImprovements() {
          this.improvements = new TreeSet<>();
     }

     @Override
     public String getCreateSQL() {
          return "(" + ids + ", " + name + ", " + price + ", " + category + ", " + available + ") VALUES (?, ?, ?, ?, ?)";
     }

     @Override
     public String getReadSQL() {
          return ids + "," + name + "," + price + "," + category + "," + available;
     }

     @Override
     public String getUpdateSQL() {
          return name + " = ? , " + price + " = ? ," + category + " = ? ," + available + " = ? ";
     }

     @Override
     public String getPrimaryKey() {
          return  ids + " = ? ";
     }
     
     @Override
     public PreparedStatement fillCreateSQL(PreparedStatement ps) throws SQLException {
          ps.setInt(1, id);
          ps.setString(2, nameVal);
          ps.setDouble(3, priceVal);
          ps.setInt(4, categoryId);
          ps.setBoolean(5, availableVal);
          return ps;
     }

     @Override
     public PreparedStatement fillUpdateSQL(PreparedStatement ps) throws SQLException {
          ps.setString(1, nameVal);
          ps.setDouble(2, priceVal);
          ps.setInt(3, categoryId);
          ps.setBoolean(4, availableVal);
          ps.setInt(5, id);
          return ps;
     }

     @Override
     public PreparedStatement fillDeleteSQL(PreparedStatement ps) throws SQLException {
          ps.setInt(1, id);
          return ps;
     }

     @Override
     public String getTable() {
          return "Produkty";
     }
     
     @Override
     public Iterator<Integer> iterator() {
          return new Iterator<Integer>() {
          private Iterator<Integer> keyIterator = improvements.iterator();
          @Override
          public boolean hasNext() {
               return keyIterator.hasNext();
          }

          @Override
          public Integer next() {
               return keyIterator.next();
          }
          };
     }

     public static String[] getPropertyes() {
          return new String[] {"id", "název produktu", "cena", "kategorie", "vypelšení"};
     }

     
     @Override
     public Map<ProductsImprovement, Set<Integer>> getComponents() {
          return Map.of(new ProductsImprovement(), improvements);
     }

}