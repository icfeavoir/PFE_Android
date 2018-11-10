package icfeavoir.pfe.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = StudentDBModel.class,
                parentColumns = "studentId",
                childColumns = "studentId",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = ProjectDBModel.class,
                parentColumns = "projectId",
                childColumns = "projectId",
                onDelete = ForeignKey.CASCADE),
        })
public class NoteDBModel extends DBModel {

    @PrimaryKey(autoGenerate = true)
    private final int studentId;
    private final int projectId;
    private final String profUsername;
    private final int note;

    public NoteDBModel(int studentId, int projectId, String profUsername, int note) {
        this.studentId = studentId;
        this.projectId = projectId;
        this.profUsername = profUsername;
        this.note = note;
    }

    public int getProjectId() {
        return this.projectId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getNote() {
        return note;
    }

    public String getProfUsername() {
        return profUsername;
    }
}
