package data.db.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Product extends ADatabaseEntry implements Iterable<Improvement> {

     public static final String name = "name", price = "price", category = "category";
   
    private Map<Integer, Improvement> improvements = new TreeMap<>();
    private int categoryId;

    private String nameVal;
    private double priceVal;
 

     public Product(int id, String name, double price, int categoryId, boolean availableVal) {
          nameVal = name;
          priceVal = price;
          this.categoryId = categoryId;

          this.availableVal = availableVal;
          this.id = id;
     }
     public Product() {}
   

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
 
     public int getCategories() {
          return categoryId;
     }

     public void addImprovement(int key, Improvement improvement) {
          improvements.put(key, improvement); 
     }

   public void addImprovement(Improvement improvement) {
        addImprovement(improvement.getId() ,improvement);
   }
 
   public Improvement removeImprovement(int id) {
        return improvements.remove(id);
   }
 
   public Improvement getImprovement(int id) {
        return improvements.get(id);
   }

   @Override
   public String getCreateSQL() {
       return "(" + name + ", " + price + ", " + category + ", " + available + ") VALUES (?, ?, ?, ?)";
   }

   @Override
   public String getReadSQL() {
       return ids + "," + name + "," + price + "," + category + "," + available;
   }

   @Override
   public String getUpdateSQL() {
       return name + " = ?, " + price + " = ?," + category + " = ?," + available + " = ?";
   }

   @Override
   public String getPrimaryKey() {
       return  ids + " == " + id;
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
      ps.setInt(1, id);
      ps.setString(2, nameVal);
      ps.setDouble(3, priceVal);
      ps.setInt(4, categoryId);
      ps.setBoolean(5, availableVal);
      return ps;
   }

   @Override
   public String getTable() {
       return "Produkty";
   }
   
   @Override
   public Iterator<Improvement> iterator() {
       return new Iterator<Improvement>() {
        private Iterator<Integer> keyIterator = improvements.keySet().iterator();
         @Override
         public boolean hasNext() {
             return keyIterator.hasNext();
         }

         @Override
         public Improvement next() {
             return improvements.get(keyIterator.next());
         }
       };
   }
}
