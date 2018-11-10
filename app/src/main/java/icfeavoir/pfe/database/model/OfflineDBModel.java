package icfeavoir.pfe.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class OfflineDBModel extends DBModel {
    @PrimaryKey (autoGenerate = true) private int id;
    private final String query;
    private final String endpoint;

    public OfflineDBModel(int id, String query, String endpoint) {
        this.id = id;
        this.query = query;
        this.endpoint = endpoint;
    }

    public String getQuery() {
        return query;
    }

    public int getId() {
        return id;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
