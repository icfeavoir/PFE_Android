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
import icfeavoir.pfe.database.model.StudentDBModel;
import icfeavoir.pfe.database.model.StudentProjectDBModel;
import icfeavoir.pfe.model.ModelConstructor;
import icfeavoir.pfe.model.Project;
import icfeavoir.pfe.model.Student;

public class JYINFProxy extends Proxy {

    private int juryId;

    public JYINFProxy(PFEActivity activity) {
        super(activity);
    }

    @Override
    void beforeCalling(JSONObject json) {
        if (json.has("jury")) {
            try {
                this.juryId = json.getInt("jury");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void callWithInternet(JSONObject json) {
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
                        project = (Project) ModelConstructor.modelFactory(projectDB, getContext());
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
    public void saveDataFromInternet(final List<?> elements) {
        final List<ProjectDBModel> projectsDB = new ArrayList<>();
        final List<StudentProjectDBModel> studentProjectDBModels = new ArrayList<>();
        final List<StudentDBModel> studentDBModels = new ArrayList<>();

        // save data in DB with new Thread
        new Thread(new Runnable() {
            @Override
            public void run() {
            Project project;
            // convert every Project in ProjectDB
            for (Object obj : elements) {
                try {
                    project = (Project) obj;
                    projectsDB.add((ProjectDBModel) ModelConstructor.dbModelFactory(project, getContext()));
                    for (Student p : project.getStudents()) {
                        studentProjectDBModels.add(new StudentProjectDBModel(p.getStudentId(), project.getProjectId()));
                        studentDBModels.add(new StudentDBModel(p.getStudentId(), p.getForename(), p.getSurname()));
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            // the persons
            for (StudentDBModel person : studentDBModels) {
                Database.getInstance(getContext()).getStudentDAO().delete(person.getStudentId());
                Database.getInstance(getContext()).getStudentDAO().insert(person);
            }
            // PROJECT : delete and resave to have the last values
            for (ProjectDBModel projectDB : projectsDB) {
                Database.getInstance(getContext()).getProjectDAO().delete(projectDB.getProjectId());
                Database.getInstance(getContext()).getProjectDAO().insert(projectDB);
            }

            // add all at once (link between students and projects)
            Database.getInstance(getContext()).getStudentProjectDAO().insert(studentProjectDBModels);
            }
        }).start();
    }

    @Override
    public void returnDataAfterInternet(JSONObject json) {
        ArrayList<Project> alProject = new ArrayList<>();
        Project project;
        try {
            JSONArray projectsArr = json.has("projects") ? json.getJSONArray("projects") : new JSONArray();
            for (int i = 0; i < projectsArr.length(); i++) {
                JSONObject projectObject = projectsArr.getJSONObject(i);
                // save juryId because not in api...
                projectObject.put("jury", juryId);
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
