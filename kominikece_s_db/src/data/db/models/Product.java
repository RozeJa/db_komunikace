package data.db.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Product extends ADatabaseEntry implements Iterable<Improvement> {

    private static final String name = "name", price = "price";
   
    private Map<Integer, Improvement> improvements = new TreeMap<>();
    private Category categories;

    private String nameVal;
    private double priceVal;
 

     public Product(int id, String name, double price, boolean availableVal) {
          nameVal = name;
          priceVal = price;

          this.availableVal = availableVal;
          this.id = id;
     }
   

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
 
     public void setCategories(Category categories) {
          this.categories = categories;
     }
 
     public Category getCategories() {
          return categories;
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
       return "(" + name + ", " + price + ", " + available + ") VALUES (?, ?, ?)";
   }

   @Override
   public String getReadSQL() {
       return name + "," + price  + "," + available ;
   }

   @Override
   public String getUpdateSQL() {
       return name + " = ?, " + price  + " = ?," + available + " = ?";
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
      ps.setString(1, nameVal);
      ps.setDouble(2, priceVal);
      ps.setBoolean(3, availableVal);
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
