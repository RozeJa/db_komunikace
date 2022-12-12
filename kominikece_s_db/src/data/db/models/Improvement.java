package data.db.models;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Improvement extends ADatabaseEntry implements Iterable<Category> {

   private static final String name = "name", price = "price";

   private Map<Integer, Category> availableCategories = new TreeMap<>();

   public Improvement(int id, String name, double price, boolean available) {
      createdList.add(Improvement.name);
      createdList.add(Improvement.price);

      properties.put(Improvement.name, name);
      properties.put(Improvement.price, Double.toString(price));

      this.available = available;
      this.id = id;
   }

   public void setPrice(double price) {
      properties.put(Improvement.price, Double.toString(price));
      updatedMap.put(Improvement.price, Double.toString(price));
   }
 
   public double getPrice() {
      return Double.parseDouble(properties.get(Improvement.price));
   }
 
   public void setName(String name) {
      properties.put(Improvement.name, name);
      updatedMap.put(Improvement.name, name);
   }
 
   public String getName() {
      return properties.get(Improvement.name);
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
   public String getPrimaryKey() {
       return  ids + " == " + id;
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
