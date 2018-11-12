package icfeavoir.pfe.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Note extends Model {
    private Student student;
    private Project project;
    private String profUsername;
    private Double note;
    private Double avg;

    public Note(JSONObject noteObject) {
        try {
            int userId = noteObject.has("userId") ? noteObject.getInt("userId") : -1;
            String forename = noteObject.has("forename") ? noteObject.getString("forename") : "";
            String surname = noteObject.has("surname") ? noteObject.getString("surname") : "";
            this.student = new Student(userId, forename, surname);
            this.project = null;
            this.note = noteObject.has("note") ? noteObject.getDouble("note") : -1.0;
            this.profUsername = User.getInstance().getUsername();
            this.avg = noteObject.has("avg") ? noteObject.getDouble("avg") : -1.0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Note(Student student, Project project, String profUsername, Double note, Double avg) {
        this.student = student;
        this.project = project;
        this.profUsername = profUsername;
        this.note = note;
        this.avg = avg;
    }

    public Student getStudent() {
        return student;
    }

    public Project getProject() {
        return project;
    }

    public String getProfUsername() {
        return profUsername;
    }

    public Double getNote() {
        return note;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Double getAvg() {
        return avg;
    }
}
