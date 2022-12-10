package data;

import java.util.List;
import java.util.Map;

public abstract class ADatabaseEntry implements IDatabaseEntry {

   protected int id;
   private List<String> primayKeyList;
   protected List<String> upratedList;
   private List<String> createdList;
   protected Map<String, String> propertiesMap;

   public int getId() {
      return id;
   }
 
   public List<String> getPrimaryKey() {
      return primayKeyList;
   }
 
   public List<String> getUpdate() {
      return upratedList;
   }
 
   public List<String> getCread() {
      return createdList;
   }
}
