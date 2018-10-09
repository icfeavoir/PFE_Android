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
            int projectId = projectObject.has("projectId") ? projectObject.getInt("projectId") : 0;
            String title = projectObject.has("title") ? projectObject.getString("title") : "";
            String description = projectObject.has("description") ? projectObject.getString("description") : "";
            int confid = projectObject.has("confid") ? projectObject.getInt("confid") : 0;
            boolean poster = projectObject.has("poster") ? projectObject.getBoolean("poster") : false;
            String supervisor = projectObject.has("supervisor") ? (projectObject.getJSONObject("supervisor").getString("forename") + " " + projectObject.getJSONObject("supervisor").getString("surname")) : "";
            this.projectId = projectId;
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

    public void setProjectId(int projectId) {
        this.projectId = projectId;
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

    public void setConfid(int confid) {
        this.confid = confid;
    }

    public boolean hasPoster() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
