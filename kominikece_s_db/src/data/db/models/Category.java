package data.db.models;

public class Category extends ADatabaseEntry {

   private static final String name = "name";

   public Category(int id, String name, boolean available) {
      createdList.add(Category.name);
      properties.put(Category.name, name);

      this.available = available;
      this.id = id;
   }

   public void setName(String name) {
      properties.put(Category.name, name);
      updatedMap.put(Category.name, name);
   }
 
   public String getName() {
      return properties.get(Category.name);
   }

   @Override
   public String getPrimaryKey() {
       return  ids + " == " + id;
   }
}
