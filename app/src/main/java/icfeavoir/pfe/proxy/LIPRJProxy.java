package icfeavoir.pfe.proxy;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import icfeavoir.pfe.communication.LIPRJCommunication;
import icfeavoir.pfe.controller.PFEActivity;
import icfeavoir.pfe.database.Database;
import icfeavoir.pfe.database.model.StudentDBModel;
import icfeavoir.pfe.database.model.StudentProjectDBModel;
import icfeavoir.pfe.database.model.ProjectDBModel;
import icfeavoir.pfe.model.ModelConstructor;
import icfeavoir.pfe.model.Student;
import icfeavoir.pfe.model.Project;

public class LIPRJProxy extends Proxy {
    private JSONObject jsonSent;

    public LIPRJProxy(PFEActivity activity) {
        super(activity);
    }

    @Override
    void beforeCalling(JSONObject json) {
        // save json to be able to use it after
        this.jsonSent = json;
    }

    @Override
    void callWithInternet(JSONObject json) {
        LIPRJCommunication com = new LIPRJCommunication(this.getContext(), this);
        com.call(json);
    }

    @Override
    void callWithoutInternet(JSONObject json) {
        final ArrayList<Project> allProjects = new ArrayList<>();
        // get data from DB with new Thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // call the database
                try {
                    List<ProjectDBModel> projects = Database.getInstance(getContext()).getProjectDAO().getAllProjects();
                    // convert ProjectDB in Project
                    for (ProjectDBModel projectDB : projects) {
                        allProjects.add((Project) ModelConstructor.modelFactory(projectDB, getContext()));
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

                // PersonProject : delete and resave to have the last values
                Database.getInstance(getContext()).getStudentProjectDAO().delete(projectDB.getProjectId());
            }
            // add all at once
            Database.getInstance(getContext()).getStudentProjectDAO().insert(studentProjectDBModels);
            }
        }).start();
    }

    /**
     * Called after internet
     * @param json the response
     */
    @Override
    public void returnDataAfterInternet(JSONObject json) {
        ArrayList<Project> alProjects= new ArrayList<>();
        Project project;
        try {
            JSONArray projectsArr = json.has("projects") ? json.getJSONArray("projects") : new JSONArray();
            for (int i = 0; i < projectsArr.length(); i++) {
                JSONObject projectObject = projectsArr.getJSONObject(i);
                project = new Project(projectObject);
                // add project to the list
                alProjects.add(project);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // at the end we display data
        this.sendDataToController(alProjects);

        // and we save everything in the db
        this.saveDataFromInternet(alProjects);
    }

    @Override
    void sendDataToController(Object elements) {
        try {
            List<Project> projects = (List<Project>) elements;
            // if json has a specific id, we only return the selected project
            if (this.jsonSent.has("projectId")) {
                final int projectId = this.jsonSent.getInt("projectId");
                for (Project p : projects) {
                    if (p.getProjectId() == projectId) {
                        this.getActivity().displayData(p);
                        return;
                    }
                }
            } else {
                this.getActivity().displayData(projects);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
