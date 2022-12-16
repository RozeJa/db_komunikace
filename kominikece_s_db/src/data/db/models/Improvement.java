package data.db.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Improvement extends ADatabaseEntry implements Iterable<Category> {

   private static final String name = "name", price = "price";

   private Map<Integer, Category> availableCategories = new TreeMap<>();

   private String nameVal;
   private double priceVal;


   public Improvement(int id, String name, double price, boolean availableVal) {
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

   public void addCategories(int key, Category categories) {
      availableCategories.put(key, categories);
   }

   public void addCategories(Category categories) {
      addCategories(categories.getId(), categories);
   }
 
   public Category removeCategories(int id) {
      return availableCategories.remove(id);
   }
 
   public Category getCategories(int id) {
      return availableCategories.get(id);
   }
   
   @Override
   public String getCreateSQL() {
       return "(" + name + ", " + price + ", " + available + ") VALUES (?, ?, ?)";
   }

   @Override
   public String getReadSQL() {
       return ids + "," + name + "," + price  + "," + available ;
   }

   @Override
   public String getUpdateSQL() {
       return name + " = ?, " + price  + " = ?," + available + " = ?";
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
      ps.setBoolean(4, availableVal);
      return ps;
   }

   @Override
   public String getPrimaryKey() {
       return  ids + " == " + id;
   }

   @Override
   public String getTable() {
       return "Vylepseni";
   }

   @Override
   public Iterator<Category> iterator() {
      return new Iterator<Category>() {
         private Iterator<Integer> keyIterator = availableCategories.keySet().iterator();
         @Override
         public boolean hasNext() {
             return keyIterator.hasNext();
         }

         @Override
         public Category next() {
             return availableCategories.get(keyIterator.next());
         }
      };
   }
}
