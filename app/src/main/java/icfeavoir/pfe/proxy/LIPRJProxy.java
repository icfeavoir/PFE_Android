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
                    Project project;
                    for (ProjectDBModel projectDB : projects) {
                        project = new Project(
                                projectDB.getProjectId(),
                                projectDB.getTitle(),
                                projectDB.getDescription(),
                                projectDB.getConfid(),
                                projectDB.hasPoster(),
                                projectDB.getSupervisor(),
                                projectDB.getJuryId(),
                                null
                        );
                        project.fillStudents(getContext(), projectDB);
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
    public void saveDataFromInternet(List<?> elements) {
        Project project;
        final List<ProjectDBModel> projectsDB = new ArrayList<>();
        final List<StudentProjectDBModel> studentProjectDBModels = new ArrayList<>();
        final List<StudentDBModel> studentDBModels = new ArrayList<>();

        // convert every Project in ProjectDB
        for (Object obj : elements) {
            try {
                project = (Project) obj;
                projectsDB.add(new ProjectDBModel(
                        project.getProjectId(),
                        project.getTitle(),
                        project.getDescription(),
                        project.getConfid(),
                        project.hasPoster(),
                        project.getSupervisor(),
                        project.getJuryId()
                ));
                for (Student p : project.getStudents()) {
                    studentProjectDBModels.add(new StudentProjectDBModel(p.getStudentId(), project.getProjectId()));
                    studentDBModels.add(new StudentDBModel(p.getStudentId(), p.getForename(), p.getSurname()));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        // save data in DB with new Thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // the persons
                for (StudentDBModel person : studentDBModels) {
                    Database.getInstance(getContext()).getPersonDAO().delete(person.getStudentId());
                    Database.getInstance(getContext()).getPersonDAO().insert(person);
                }
                // PROJECT : delete and resave to have the last values
                for (ProjectDBModel project : projectsDB) {
                    Database.getInstance(getContext()).getProjectDAO().delete(project.getProjectId());
                    Database.getInstance(getContext()).getProjectDAO().insert(project);

                    // PersonProject : delete and resave to have the last values
                    Database.getInstance(getContext()).getPersonProjectDAO().delete(project.getProjectId());
                }
                // add all at once
                Database.getInstance(getContext()).getPersonProjectDAO().insert(studentProjectDBModels);
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
                Project project = null;
                for (Project p : projects) {
                    if (p.getProjectId() == projectId) {
                        project = p;
                        this.getActivity().displayData(project);
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
