package data;

import java.util.Iterator;
import java.util.Map;

public class Product extends ADatabaseEntry implements Iterable<Improvement> {

    private static final String name = "name", price = "price";
   
   private Map<Integer, Improvement> improvements;
   private Categories categories;

   public void setPrice(double price) {
    propertiesMap.put(Product.price, Double.toString(price));
    upratedSet.add(Product.price);
 }

 public double getPrice() {
    String priceString = propertiesMap.get(Product.price);

    if (priceString == null) 
       return -1;

    return Double.parseDouble(priceString);
 }

 public void setName(String name) {
    propertiesMap.put(Product.name, name);
    upratedSet.add(Product.name);
 }

 public String getName() {
    return propertiesMap.get(name);
 }
 
   public void setCategories(Categories categories) {
        this.categories = categories;
   }
 
   public Categories getCategories() {
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

   public void setAvailable(boolean available) {
        this.available = available;
   }

   public boolean isAvailable() {
        return available;
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
