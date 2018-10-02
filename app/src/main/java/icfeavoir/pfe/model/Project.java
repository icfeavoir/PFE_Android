package icfeavoir.pfe.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Project {

    private int projectId;
    private String title;
    private String description;
    private int confid;
    private boolean poster;
    private String supervisor;

    public Project(JSONObject projectObject) {
        try {
            int projectId = projectObject.getInt("projectId");
            String title = projectObject.getString("title");
            String description = projectObject.getString("description");
            int confid = projectObject.getInt("projectId");
            boolean poster = projectObject.getBoolean("projectId");
            String supervisor = projectObject.getJSONObject("supervisor").getString("forename") + " " + projectObject.getJSONObject("supervisor").getString("surname");
            this.projectId = projectId;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Project(int projectId) {
        this.projectId = projectId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int isConfid() {
        return confid;
    }

    public void setConfid(int confid) {
        this.confid = confid;
    }

    public boolean isPoster() {
        return poster;
    }

    public void setPoster(boolean poster) {
        this.poster = poster;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
}