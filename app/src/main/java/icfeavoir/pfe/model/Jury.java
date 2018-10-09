package icfeavoir.pfe.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Jury {

    private int juryId;
    private String date;
    private Map<Integer, Project> projects;

    public Jury(JSONObject juryObject) {
        try {
            int juryId = juryObject.getInt("idJury");
            String date = juryObject.getString("date");
            this.juryId = juryId;
            this.date = date;

            JSONObject info = juryObject.getJSONObject("info");
            JSONArray projects = info.getJSONArray("projects");
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
