package icfeavoir.pfe.model;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import icfeavoir.pfe.database.Database;
import icfeavoir.pfe.database.model.NoteDBModel;
import icfeavoir.pfe.database.model.ProjectDBModel;
import icfeavoir.pfe.database.model.StudentDBModel;

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

    public Note(NoteDBModel noteDBModel, Context context) {
        StudentDBModel studentDB = Database.getInstance(context)
                .getStudentDAO()
                .getPerson(noteDBModel.getStudentId());
        this.student = new Student(studentDB, context);
        ProjectDBModel projectDB = Database.getInstance(context)
                .getProjectDAO()
                .getProject(noteDBModel.getProjectId());
        this.project = new Project(projectDB, context);
        this.profUsername = noteDBModel.getProfUsername();
        this.note = noteDBModel.getNote();
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
