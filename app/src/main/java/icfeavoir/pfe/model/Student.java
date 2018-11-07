package icfeavoir.pfe.model;

public class Student {
    private int studentId;
    private String forename;
    private String surname;

    public Student(int studentId, String forename, String surname) {
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

    @Override
    public String toString() {
        return "[PEOPLE] " +
                "studentId: " + this.studentId + " | " +
                "forename: " + this.forename + " | " +
                "surname: " + this.surname;
    }
}
