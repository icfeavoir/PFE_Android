package icfeavoir.pfe.proxy;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import icfeavoir.pfe.communication.MYJURCommunication;
import icfeavoir.pfe.controller.PFEActivity;
import icfeavoir.pfe.database.Database;
import icfeavoir.pfe.database.model.JuryDBModel;
import icfeavoir.pfe.database.model.ProjectDBModel;
import icfeavoir.pfe.model.Jury;
import icfeavoir.pfe.model.Project;

public class MYJURProxy extends Proxy {

    public MYJURProxy(PFEActivity activity) {
        super(activity);
    }

    @Override
    void callWithInternet(JSONObject json) {
        // call the communication class and give "this" to enable the callback
        MYJURCommunication com = new MYJURCommunication(this.getContext(), this);
        com.call(json);
    }

    @Override
    void callWithoutInternet(JSONObject json) {
        final ArrayList<Jury> allJuries = new ArrayList<>();
        // get data from DB with new Thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // call the database
                try {
                    List<JuryDBModel> juries = Database.getInstance(getContext()).getJuryDAO().getAllJuries();
                    // convert JuryDB in Jury
                    Jury jury;
                    Project project;
                    for (JuryDBModel juryDB : juries) {
                        jury = new Jury(juryDB.getJuryId(), juryDB.getDate());
                        // find projects
                        List<ProjectDBModel> projects = Database.getInstance(getContext()).getProjectDAO().getProjectByJury(jury.getJuryId());
                        for (ProjectDBModel projectDB : projects) {
                            project = new Project(projectDB.getProjectId(), projectDB.getTitle(), projectDB.getDescription(), projectDB.getConfid(), projectDB.hasPoster(), projectDB.getSupervisor());
                            jury.addProject(project);
                        }

                        allJuries.add(jury);
                    }

                    // display data
                    sendDataToController(allJuries);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void saveDataFromInternet(List<?> elements) {
        Jury jury;
        final List<JuryDBModel> juriesDB = new ArrayList<>();
        final List<ProjectDBModel> projectsDB = new ArrayList<>();

        // convert every Jury in JuryDB
        for (Object obj : elements) {
            try {
                jury = (Jury) obj;
                juriesDB.add(new JuryDBModel(jury.getJuryId(), jury.getDate()));

                // convert every Project in ProjectDB
                for (Map.Entry<Integer, Project> entry : jury.getProjects().entrySet()) {
                    Project project = entry.getValue();
                    projectsDB.add(new ProjectDBModel(project.getProjectId(), project.getTitle(), project.getDescription(), project.getConfid(), project.hasPoster(), project.getSupervisor(), jury.getJuryId()));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        // save data in DB with new Thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // delete and resave to have the last values
                for (JuryDBModel jury : juriesDB) {
                    Database.getInstance(getContext()).getJuryDAO().delete(jury.getJuryId());
                    Database.getInstance(getContext()).getJuryDAO().insert(jury);
                }
                for (ProjectDBModel project : projectsDB) {
                    Database.getInstance(getContext()).getProjectDAO().delete(project.getProjectId());
                    Database.getInstance(getContext()).getProjectDAO().insert(project);
                }
            }
        }).start();
    }

    /*
    * Callback when retrieving data from the Internet
     */
    @Override
    public void returnDataAfterInternet(JSONObject json) {
        ArrayList<Jury> alJuries = new ArrayList<>();
        Jury jury;
        try {
            JSONArray juriesArr = json.has("juries") ? json.getJSONArray("juries") : new JSONArray();
            for (int i = 0; i < juriesArr.length(); i++) {
                JSONObject juryObject = juriesArr.getJSONObject(i);
                jury = new Jury(juryObject);
                // add jury to the list
                alJuries.add(jury);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // at the end we display data
        this.sendDataToController(alJuries);

        // and we save everything in the db
        this.saveDataFromInternet(alJuries);
    }

    @Override
    void sendDataToController(Object elements) {
        try {
            List<Jury> juries = (List<Jury>) elements;
            this.getActivity().displayData(juries);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
