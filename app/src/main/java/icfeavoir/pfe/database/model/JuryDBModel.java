package icfeavoir.pfe.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class JuryDBModel extends DBModel {

    @PrimaryKey private final int juryId;
    private final String date;

    public JuryDBModel(int juryId, String date) {
        this.juryId = juryId;
        this.date = date;
    }

    public int getJuryId() {
        return juryId;
    }

    public String getDate() {
        return date;
    }
}
