package icfeavoir.pfe.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Note {
    private Student student;
    private Project project;
    private String profUsername;
    private int note;

    public Note(JSONObject noteObject) {
        try {
            this.note = noteObject.has("note") ? noteObject.getInt("note") : -1;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Note(Student student, Project project, String profUsername, int note) {
        this.student = student;
        this.project = project;
        this.profUsername = profUsername;
        this.note = note;
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

    public int getNote() {
        return note;
    }
}
