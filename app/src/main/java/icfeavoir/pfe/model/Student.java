package icfeavoir.pfe.model;

import android.content.Context;

import icfeavoir.pfe.database.model.StudentDBModel;

public class Student extends Model {
    private int studentId;
    private String forename;
    private String surname;

    public Student(int studentId, String forename, String surname) {
        this.studentId = studentId;
        this.forename = forename;
        this.surname = surname;
    }

    public Student(StudentDBModel studentDBModel, Context context) {
        this(
                studentDBModel.getStudentId(),
                studentDBModel.getForename(),
                studentDBModel.getSurname()
        );
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

    public String getFullName() {
        return this.getForename() + " " + this.getSurname();
    }

    @Override
    public String toString() {
        return "[PEOPLE] " +
                "studentId: " + this.studentId + " | " +
                "forename: " + this.forename + " | " +
                "surname: " + this.surname;
    }
}
