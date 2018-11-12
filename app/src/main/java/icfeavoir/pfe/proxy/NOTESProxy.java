package icfeavoir.pfe.proxy;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import icfeavoir.pfe.communication.NOTESCommunication;
import icfeavoir.pfe.controller.PFEActivity;
import icfeavoir.pfe.database.Database;
import icfeavoir.pfe.database.model.NoteDBModel;
import icfeavoir.pfe.model.ModelConstructor;
import icfeavoir.pfe.model.Note;
import icfeavoir.pfe.model.Project;

public class NOTESProxy extends Proxy {

    private int projectId;

    public NOTESProxy(PFEActivity activity) {
        super(activity);
    }

    @Override
    void beforeCalling(JSONObject json) {
        // save the project id;
        if (json.has("proj")) {
            try {
                this.projectId = json.getInt("proj");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void callWithInternet(JSONObject json) {
        NOTESCommunication com = new NOTESCommunication(this.getContext(), this);
        com.call(json);
    }

    @Override
    void callWithoutInternet(JSONObject json) {
        final ArrayList<Note> notes = new ArrayList<>();
        // get data from DB with new Thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // call the database
                try {
                    List<NoteDBModel> notesDB = Database.getInstance(getContext()).getNoteDAO().getNotesByProjectId(projectId);
                    // convert NoteDB in Note
                    for (NoteDBModel noteDB : notesDB) {
                        notes.add((Note) ModelConstructor.modelFactory(noteDB, getContext()));
                    }
                    // display data
                    sendDataToController(notes);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void returnDataAfterInternet(JSONObject json) {
        List<Note> notes = new ArrayList<>();
        Note note;
        try {
            JSONArray notesArr = json.getJSONArray("notes");
            for (int i = 0; i < notesArr.length(); i++) {
                JSONObject noteObj = notesArr.getJSONObject(i);
                note = new Note(noteObj);
                // add project to the list
                note.setProject(new Project(projectId));
                notes.add(note);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.sendDataToController(notes);
        this.saveDataFromInternet(notes);
    }

    @Override
    void saveDataFromInternet(List<?> elements) {
        final List<NoteDBModel> noteDBModels = new ArrayList<>();
        try {
            List<Note> notes = (List<Note>) elements;
            for (Note note : notes) {
                noteDBModels.add(new NoteDBModel(
                        note.getStudent().getStudentId(),
                        note.getProject().getProjectId(),
                        note.getProfUsername(),
                        note.getNote(),
                        note.getAvg()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // save data in DB with new Thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // delete all notes of this project
                Database.getInstance(getContext()).getNoteDAO().deleteByProjectId(projectId);
                Database.getInstance(getContext()).getNoteDAO().insert(noteDBModels);
            }
        }).start();
    }

    @Override
    void sendDataToController(Object elements) {
        try {
            List<Note> notes = (List<Note>) elements;
            this.getActivity().displayData(notes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
