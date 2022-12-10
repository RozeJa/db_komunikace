package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public abstract class ABuilder implements Iterable<IDatabaseEntry> {

   private ResultSet rs;

   public void setResultSet(ResultSet rs) {
      this.rs = rs;
   }
 
   public Iterator<IDatabaseEntry> iterator() {
      return new Iterator<IDatabaseEntry>() {
         @Override
         public boolean hasNext() {
             try {
                 return rs.next();
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
 
   public IDatabaseEntry build() throws SQLException {
      // TODO: 

      return null;
   }
}
