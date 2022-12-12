package data.db.models;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class ADatabaseEntry {

   protected int id;
   protected static final String ids = "id";
   protected boolean available;

   protected List<String> createdList = new ArrayList<>();
   protected Map<String, String> properties = new TreeMap<>();

   protected Map<String, String> updatedMap;

   public int getId() {
      return id;
   }
 
   public abstract String getPrimaryKey();
 
   public Map<String, String> getUpdate() {
      return updatedMap;
   }

   public void nullUpdated() {
      updatedMap = new TreeMap<>();
   }
 
   public List<String> getCread() {
      return createdList;
   }

   public void setAvailable(boolean available) {
      this.available = available;
   }

   public boolean isAvailable() {
      return available;
   }

   public static boolean parseBoolean(String string) throws ParseException {
      if ("true".equals(string) || "false".equals(string)) 
        return "true".equals(string);
      else
        throw new ParseException("It is not boolean value", 0);
    }
}
