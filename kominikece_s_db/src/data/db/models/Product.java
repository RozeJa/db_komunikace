package data.db.models;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Product extends ADatabaseEntry implements Iterable<Improvement> {

    private static final String name = "name", price = "price";
   
    private Map<Integer, Improvement> improvements = new TreeMap<>();
    private Category categories;

    public Product(int id, String name, double price, boolean available) {
     createdList.add(Product.name);
     createdList.add(Product.price);

     properties.put(Product.name, name);
     properties.put(Product.price, Double.toString(price));

     this.available = available;
     this.id = id;
    }
   

     public void setPrice(double price) {
          properties.put(Product.price, Double.toString(price));
          updatedMap.put(Product.price, Double.toString(price));
     }

     public double getPrice() {
          return Double.parseDouble(properties.get(Product.price));
     }

     public void setName(String name) {
          properties.put(Product.name, name);
          updatedMap.put(Product.name, name);
     }

     public String getName() {
          return properties.get(Product.name);
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
   public String getPrimaryKey() {
       return  ids + " == " + id;
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
