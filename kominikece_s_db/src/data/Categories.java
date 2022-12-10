package data;
public class Categories extends ADatabaseEntry {

   private static final String name = "name";

   public void setName(String name) {
      propertiesMap.put(Categories.name, name);
      upratedSet.add(Categories.name);
   }
 
   public String getName() {
      return propertiesMap.get(name);
   }

   public void setAvailable(boolean available) {
      this.available = available;
   }
 
   public boolean isAvailable() {
      return available;
   }
}
