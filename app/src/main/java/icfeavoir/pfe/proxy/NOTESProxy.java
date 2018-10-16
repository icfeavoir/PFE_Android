package icfeavoir.pfe.proxy;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import icfeavoir.pfe.communication.NOTESCommunication;
import icfeavoir.pfe.controller.PFEActivity;
import icfeavoir.pfe.database.Database;
import icfeavoir.pfe.database.model.NoteDBModel;
import icfeavoir.pfe.model.Note;

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
                    Note note;
                    for (NoteDBModel noteDB : notesDB) {
                        note = new Note(
                                noteDB.getUserId(),
                                noteDB.getForename(),
                                noteDB.getSurname(),
                                noteDB.getNote(),
                                noteDB.getAvgNote()
                        );
                        notes.add(note);
                    }
                    Log.i("NOTE", notes.size() +" notes in DB");

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
        Log.i("NOTES", json.toString());
        List<Note> notes = new ArrayList<>();
        Gson gson = new Gson();
        try {
            JSONArray notesArr = json.getJSONArray("notes");
            if (notesArr.length() > 0) {
                notes = Arrays.asList(gson.fromJson(notesArr.toString(), Note[].class));
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
                        note.getUserId(),
                        this.projectId,
                        note.getForename(),
                        note.getSurname(),
                        note.getNote(),
                        note.getAvgNote()
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
                Log.i("NOTES", "added " + noteDBModels.size() + " notes");
            }
        }).start();
    }

    @Override
    void sendDataToController(Object elements) {
        try {
            List<Note> notes = (List<Note>) elements;
            for (Note note : notes) {
                Log.i("NOTE", note.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
