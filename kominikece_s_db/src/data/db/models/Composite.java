package data.db.models;

import java.util.Map;
import java.util.Set;

public interface Composite extends IDatabaseEntry {
    Map<? extends SubTable, Set<Integer>> getComponents();
}
