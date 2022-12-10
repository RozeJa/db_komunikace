package data;

import java.util.Iterator;
import java.util.Map;

public class Improvement extends ADatabaseEntry implements Iterable<Categories> {

   private static final String name = "name", price = "price";

   private Map<Integer, Categories> availableCategories;

   public void setPrice(double price) {
      propertiesMap.put(Improvement.price, Double.toString(price));
      upratedSet.add(Improvement.price);
   }
 
   public double getPrice() {
      String priceString = propertiesMap.get(Improvement.price);

      if (priceString == null) 
         return -1;

      return Double.parseDouble(priceString);
   }
 
   public void setName(String name) {
      propertiesMap.put(Improvement.name, name);
      upratedSet.add(Improvement.name);
   }
 
   public String getName() {
      return propertiesMap.get(name);
   }

   public void addCategories(int key, Categories categories) {
      availableCategories.put(key, categories);
   }

   public void addCategories(Categories categories) {
      addCategories(categories.getId(), categories);
   }
 
   public Categories removeCategories(int id) {
      return availableCategories.remove(id);
   }
 
   public Categories getCategories(int id) {
      return availableCategories.get(id);
   }

   public void setAvailable(boolean available) {
      this.available = available;
   }
 
   public boolean isAvailable() {
      return available;
   }

   @Override
   public Iterator<Categories> iterator() {
      return new Iterator<Categories>() {
         private Iterator<Integer> keyIterator = availableCategories.keySet().iterator();
         @Override
         public boolean hasNext() {
             return keyIterator.hasNext();
         }

         @Override
         public Categories next() {
             return availableCategories.get(keyIterator.next());
         }
      };
   }
}
