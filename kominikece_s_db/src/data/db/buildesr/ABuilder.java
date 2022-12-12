package data.db.buildesr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import data.db.models.ADatabaseEntry;

public abstract class ABuilder implements Iterable<ADatabaseEntry> {

    protected ResultSet rs;

    public void setResultSet(ResultSet rs) {
      this.rs = rs;
    }
 
    public Iterator<ADatabaseEntry> iterator() {
        return new Iterator<ADatabaseEntry>() {
            @Override
            public boolean hasNext() {
                try {
                    return rs.next();
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public ADatabaseEntry next() {
                try {
                    return build();
                } catch (Exception e) {
                    return null;
                }
            }
        };
    }
 
    public abstract ADatabaseEntry build() throws SQLException;
}
