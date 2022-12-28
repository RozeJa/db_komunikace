package data.db.buildesr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import data.db.models.IDatabaseEntry;

public abstract class ABuilder implements Iterable<IDatabaseEntry> {

    protected ResultSet rs;

    public void setResultSet(ResultSet rs) {
      this.rs = rs;
    }
 
    public Iterator<IDatabaseEntry> iterator() {
        return new Iterator<IDatabaseEntry>() {
            @Override
            public boolean hasNext() {
                try {
                    boolean hasNext = rs.next();

                    if (!hasNext) {
                        rs.close();
                    }

                    return hasNext;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public IDatabaseEntry next() {
                try {
                    return build();
                } catch (Exception e) {
                    return null;
                }
            }
        };
    }
 
    public abstract IDatabaseEntry build() throws SQLException;
}
