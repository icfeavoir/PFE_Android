package icfeavoir.pfe.proxy;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import icfeavoir.pfe.communication.JYINFCommunication;
import icfeavoir.pfe.controller.PFEActivity;
import icfeavoir.pfe.database.Database;
import icfeavoir.pfe.database.model.ProjectDBModel;
import icfeavoir.pfe.model.Project;

public class JYINFProxy extends Proxy {

    public JYINFProxy(PFEActivity activity) {
        super(activity);
    }

    @Override
    void callWithInternet(JSONObject json) {
        Log.i("coolcool",json.toString());
        // call the communication class and give "this" to enable the callback
        JYINFCommunication com = new JYINFCommunication(this.getContext(), this);
        com.call(json);
    }

    @Override
    void callWithoutInternet(final JSONObject json) {
        final ArrayList<Project> allProjects = new ArrayList<>();
        // get data from DB with new Thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // call the database
                try {
                    int juryId = 0;
                    if (json.has("jury")){
                        juryId = json.getInt("jury");
                    }
                    List<ProjectDBModel> projectsDB = Database.getInstance(getContext()).getProjectDAO().getProjectByJury(juryId);
                    // convert ProjectDB in Project
                    Project project;
                    for (ProjectDBModel projectDB : projectsDB){
                        project = new Project(projectDB, getContext());
                        allProjects.add(project);
                    }

                    // display data
                    sendDataToController(allProjects);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void returnDataAfterInternet(JSONObject json) {
        Log.i("salut",json.toString());
        ArrayList<Project> alProject = new ArrayList<>();
        Project project;
        try {
            JSONArray projectsArr = json.has("projects") ? json.getJSONArray("projects") : new JSONArray();
            for (int i = 0; i < projectsArr.length(); i++) {
                JSONObject projectObject = projectsArr.getJSONObject(i);
                project = new Project(projectObject);
                // add project to the list
                alProject.add(project);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // at the end we display data
        this.sendDataToController(alProject);

        // and we save everything in the db
        this.saveDataFromInternet(alProject);
    }

    @Override
    void sendDataToController(Object elements) {
        try {
            List<Project> projects = (List<Project>) elements;
            this.getActivity().displayData(projects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
