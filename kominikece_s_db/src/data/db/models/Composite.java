package data.db.models;

import java.util.Map;

public interface Composite extends IDatabaseEntry {
    Map<SubTable, Iterable<Integer>> getComponents();
}
