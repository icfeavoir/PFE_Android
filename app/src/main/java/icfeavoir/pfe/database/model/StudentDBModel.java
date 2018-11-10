package icfeavoir.pfe.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class StudentDBModel extends DBModel {

    @PrimaryKey private final int studentId;
    private final String forename;
    private final String surname;

    public StudentDBModel(int studentId, String forename, String surname) {
        this.studentId = studentId;
        this.forename = forename;
        this.surname = surname;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getForename() {
        return forename;
    }

    public String getSurname() {
        return surname;
    }

}
