package data;

import java.util.Iterator;
import java.util.Map;

public class Product extends ADatabaseEntry implements Iterable<Improvement> {
   
   private Map<Integer, Improvement> Improvements;
   private Categories categories;

   public void setPrice(double price) {
   } 
 
   public double getPrice() {
   }
 
   public void setName(String name) {
   }
 
   public String getName() {
   }
 
   public void setCategories(Categories categories) {
   }
 
   public Categories getCategories() {
   }

   public boolean addImprovement(Improvement improvement) {
   }
 
   public Improvement removeImprovement(int id) {
   }
 
   public Improvement getImprovement(int id) {
   }
 
   public boolean isAvailable() {
   }
  
   @Override
   public Iterator<Improvement> iterator() {
       return new Iterator<Improvement>() {
         @Override
         public boolean hasNext() {
             // TODO: 
             return false;
         }

         @Override
         public Improvement next() {
             // TODO: 
             return null;
         }
       };
   }
}
