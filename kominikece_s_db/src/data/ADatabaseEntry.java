package data;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class ADatabaseEntry implements IDatabaseEntry {

   protected int id;
   protected boolean available;
   private List<String> primayKeyList;
   protected Set<String> upratedSet;
   private List<String> createdList;
   protected Map<String, String> propertiesMap;

   public int getId() {
      return id;
   }
 
   public List<String> getPrimaryKey() {
      return primayKeyList;
   }
 
   public Set<String> getUpdate() {
      return upratedSet;
   }
 
   public List<String> getCread() {
      return createdList;
   }
}
