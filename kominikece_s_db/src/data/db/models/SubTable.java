package data.db.models;

public interface SubTable extends IDatabaseEntry {
    Object clone() throws CloneNotSupportedException;

    // Vlastněné id je hodnota která je mapována 
    int getOwnedId();
    void setOwnedId(int ownedId);

    // Vlastnící id je id objektu, který vlastní 
    int getOwnerId();
    void setOwnerId(int ownerId);
}
