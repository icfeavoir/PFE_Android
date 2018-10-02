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
    private Date date;
    private Map<Integer, Project> projects;

    public Jury(JSONObject juryObject) {
        try {
            int juryId = juryObject.getInt("idJury");
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(juryObject.getString("date"));
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
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public Jury(int juryId, Date date) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
