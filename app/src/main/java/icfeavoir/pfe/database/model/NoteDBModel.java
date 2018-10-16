package icfeavoir.pfe.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class NoteDBModel {

    @PrimaryKey(autoGenerate = true)
    private final int userId;
    private final int projectId;
    private final String forename;
    private final String surname;
    private final int note;
    private final int avgNote;

    public NoteDBModel(int userId, int projectId, String forename, String surname, int note, int avgNote) {
        this.userId = userId;
        this.projectId = projectId;
        this.forename = forename;
        this.surname = surname;
        this.note = note;
        this.avgNote = avgNote;
    }

    public int getProjectId() {
        return this.projectId;
    }

    public int getUserId() {
        return userId;
    }

    public String getForename() {
        return forename;
    }

    public String getSurname() {
        return surname;
    }

    public int getNote() {
        return note;
    }

    public int getAvgNote() {
        return avgNote;
    }
}
