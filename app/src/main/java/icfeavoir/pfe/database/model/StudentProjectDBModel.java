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
public class StudentProjectDBModel {

    @PrimaryKey(autoGenerate = true)
    private final int studentId;
    private final int projectId;

    public StudentProjectDBModel(int studentId, int projectId) {
        this.studentId = studentId;
        this.projectId = projectId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getProjectId() {
        return projectId;
    }
}
