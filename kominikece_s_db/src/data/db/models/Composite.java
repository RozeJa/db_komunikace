package data.db.models;

import java.util.Iterator;
import java.util.Map;

public interface Composite extends IDatabaseEntry {
    Map<? extends SubTable, Iterator<Integer>> getComponents();
}
