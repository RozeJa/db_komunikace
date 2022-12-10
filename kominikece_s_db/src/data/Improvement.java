package data;

import java.util.Iterator;
import java.util.Map;

public class Improvement extends ADatabaseEntry implements Iterable<Categories> {

   private Map<Integer, Categories> availableCategories;
   public void setPrice(double price) {
   }
 
   public double getPrice() {
   }
 
   public void setName(String name) {
   }
 
   public String getName() {
   }

   public boolean addCategories(Categories categories) {
   }
 
   public Categories removeCategories(int id) {
   }
 
   public Categories getCategories(int id) {
   }
 
   public boolean isAvailable() {
   } 

   public Iterator<Categories> iterable() {
      return new Iterator<Categories>() {
         @Override
         public boolean hasNext() {
             // TODO: Auto-generated method stub
             return false;
         }

         @Override
         public Categories next() {
             // TODO: Auto-generated method stub
             return null;
         }
      };
   }
}
