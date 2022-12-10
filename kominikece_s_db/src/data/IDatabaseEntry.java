package data;

import java.util.List;

public interface IDatabaseEntry {
   int getId();
   List<String> getPrimaryKey();
   List<String> getUpdate();
   List<String> getCread();
}
