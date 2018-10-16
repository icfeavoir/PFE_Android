package icfeavoir.pfe.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Project {

    private int projectId;
    private String title;
    private String description;
    private int confid;
    private boolean poster;
    private String supervisor;
    private int studentId;

    public Project(JSONObject projectObject) {
        try {
            this.projectId = projectObject.has("projectId") ? projectObject.getInt("projectId") : 0;
            this.title = projectObject.has("title") ? projectObject.getString("title") : "";
            this.description = projectObject.has("description") ? projectObject.getString("description") : "";
            this.confid = projectObject.has("confid") ? projectObject.getInt("confid") : 0;
            this.poster = projectObject.has("poster") ? projectObject.getBoolean("poster") : false;
            this.supervisor = projectObject.has("supervisor") ? (projectObject.getJSONObject("supervisor").getString("forename") + " " + projectObject.getJSONObject("supervisor").getString("surname")) : "";
            this.studentId = projectObject.has("students") ? (projectObject.getJSONArray("students").getJSONObject(0).getInt("userId")) : 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Project(int projectId, String title, String description, int confid, boolean poster, String supervisor) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.confid = confid;
        this.poster = poster;
        this.supervisor = supervisor;
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

    public int getStudentId() {
        return studentId;
    }
}
