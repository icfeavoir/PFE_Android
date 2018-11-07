package icfeavoir.pfe.model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import icfeavoir.pfe.database.Database;
import icfeavoir.pfe.database.model.StudentDBModel;
import icfeavoir.pfe.database.model.StudentProjectDBModel;
import icfeavoir.pfe.database.model.ProjectDBModel;

public class Project {

    private int projectId;
    private String title;
    private String description;
    private int confid;
    private boolean poster;
    private String supervisor;
    private List<Student> students;
    private int juryId;

    public Project(JSONObject projectObject) {
        try {
            this.projectId = projectObject.has("projectId") ? projectObject.getInt("projectId") : 0;
            this.title = projectObject.has("title") ? projectObject.getString("title") : "";
            this.description = projectObject.has("descrip") ? projectObject.getString("descrip") : "";
            this.confid = projectObject.has("confid") ? projectObject.getInt("confid") : 0;
            this.poster = projectObject.has("poster") ? projectObject.getBoolean("poster") : false;
            this.juryId = projectObject.has("juryId") ? projectObject.getInt("juryId") : 0;

            this.supervisor = "";
            if (projectObject.has("supervisor")) {
                this.supervisor = projectObject.getJSONObject("supervisor").getString("forename") + " " +
                                    projectObject.getJSONObject("supervisor").getString("surname");
            }

            this.students = new ArrayList<>();
            if (projectObject.has("students")) {
                JSONArray studentsArray = projectObject.getJSONArray("students");
                for (int i = 0; i < studentsArray.length(); i++) {
                    JSONObject studentObj = studentsArray.getJSONObject(i);
                    int stuId = studentObj.has("userId") ? studentObj.getInt("userId") : 0;
                    String stuForename = studentObj.has("forename") ? studentObj.getString("forename") : "";
                    String stuSurname = studentObj.has("surname") ? studentObj.getString("surname") : "";
                    Student student = new Student(stuId, stuForename, stuSurname);
                    this.students.add(student);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Project(int projectId, String title, String description, int confid, boolean poster, String supervisor, int juryId, List<Student> students) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.confid = confid;
        this.poster = poster;
        this.supervisor = supervisor;
        this.juryId = juryId;
        this.students = students;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getConfid() {
        return confid;
    }

    public boolean hasPoster() {
        return poster;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Student> getStudents() {
        return students;
    }

    public int getJuryId() {
        return juryId;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        if (this.students == null) {
            this.students = new ArrayList<>();
        }
        this.students.add(student);
    }

    public void fillStudents(Context context, ProjectDBModel projectDB) {
        // get the students
        List<StudentProjectDBModel> studentsProject = Database.getInstance(context).getPersonProjectDAO().getProjectPersons(projectDB.getProjectId());
        for (StudentProjectDBModel ppDB : studentsProject) {
            StudentDBModel studentDB = Database.getInstance(context).getPersonDAO().getPerson(ppDB.getStudentId());
            Student student = new Student(studentDB.getStudentId(), studentDB.getForename(), studentDB.getSurname());
            this.addStudent(student);
        }
    }

    @Override
    public String toString() {
        return "[PROJET] " +
                "projectId: " + this.projectId + " | " +
                "title: " + this.title + " | " +
                "description: " + this.description + " | " +
                "confid: " + this.confid + " | " +
                "poster: " + this.poster + " | " +
                "supervisor: " + this.supervisor + " | " +
                "studentId: " + this.students + " | " +
                "juryId: " + this.juryId;
    }
}
