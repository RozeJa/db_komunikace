package data;

import java.util.List;
import java.util.Set;

public interface IDatabaseEntry {
   int getId();
   List<String> getPrimaryKey();
   Set<String> getUpdate();
   List<String> getCread();
}
