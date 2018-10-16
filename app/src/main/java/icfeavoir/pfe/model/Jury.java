package icfeavoir.pfe.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Jury {

    private int juryId;
    private String date;
    private Map<Integer, Project> projects;

    public Jury(JSONObject juryObject) {
        this.projects = new HashMap<>();
        try {
            int juryId = juryObject.has("idJury") ? juryObject.getInt("idJury") : 0;
            String date = juryObject.has("date") ? juryObject.getString("date") : "";
            this.juryId = juryId;
            this.date = date;

            JSONObject info = juryObject.has("info") ? juryObject.getJSONObject("info") : new JSONObject();
            JSONArray projects = info.has("projects") ? info.getJSONArray("projects") : new JSONArray();
            Project project;
            for (int i = 0; i < projects.length(); i++) {
                JSONObject projectObject = projects.getJSONObject(i);
                project = new Project(projectObject);
                // ajout du projet à la liste
                this.addProject(project);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Jury(int juryId, String date) {
        this.juryId = juryId;
        this.date = date;
        this.projects = new HashMap<>();
    }

    public Map<Integer, Project> getProjects() {
        return this.projects;
    }

    public void addProject(Project project) {
        this.projects.put(project.getProjectId(), project);
    }

    public void removeProject(int projectId) {
        this.projects.remove(projectId);
    }

    public int getJuryId() {
        return juryId;
    }

    public void setJuryId(int juryId) {
        this.juryId = juryId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
