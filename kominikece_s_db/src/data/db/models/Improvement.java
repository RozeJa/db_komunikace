package data.db.models;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Improvement extends ADatabaseEntry implements Iterable<Integer>, Composite {

   public static final String name = "nazev", price = "cena";

   private Set<Integer> availableCategories = new TreeSet<>();

   private String nameVal;
   private double priceVal;

   public Improvement(int id, String name, double price, boolean availableVal) {
      nameVal = name;
      priceVal = price;

      this.availableVal = availableVal;
      this.id = id;
   }
   public Improvement() {}

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

   public void addCategories(int categories) {
      availableCategories.add(categories);
   }
   public Set<Integer> getCategories() {
      return availableCategories;
   }
   public boolean removeCategories(int id) {
      return availableCategories.remove(id);
   }
   public void removeCategories() {
      this.availableCategories = new TreeSet<>();
   }
   @Override
   public String getCreateSQL() {
       return "(" + ids + ", " + name + ", " + price + ", " + available + ") VALUES (?, ?, ?, ?)";
   }

   @Override
   public String getReadSQL() {
       return ids + "," + name + "," + price  + "," + available ;
   }

   @Override
   public String getUpdateSQL() {
       return name + " = ? , " + price  + " = ? ," + available + " = ? ";
   }

   @Override
   public PreparedStatement fillCreateSQL(PreparedStatement ps) throws SQLException {
      ps.setInt(1, id);
      ps.setString(2, nameVal);
      ps.setDouble(3, priceVal);
      ps.setBoolean(4, availableVal);
      return ps;
   }

   @Override
   public PreparedStatement fillUpdateSQL(PreparedStatement ps) throws SQLException {
      ps.setString(1, nameVal);
      ps.setDouble(2, priceVal);
      ps.setBoolean(3, availableVal);
      return ps;
   }

   @Override
   public String getPrimaryKey() {
       return  ids + " = " + id;
   }

   @Override
   public String getTable() {
       return "Vylepseni";
   }

   @Override
   public Iterator<Integer> iterator() {
      return new Iterator<Integer>() {
         private Iterator<Integer> keyIterator = availableCategories.iterator();
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
      return new String[] {"id", "název vylepšení", "cena", "kategorie"};
   }

   @Override
   public Map<ImprovementInCategory, Set<Integer>> getComponents() {
       return Map.of(new ImprovementInCategory(), availableCategories);
   }

}
