package icfeavoir.pfe.proxy;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import icfeavoir.pfe.communication.Communication;
import icfeavoir.pfe.communication.NEWNTCommunication;
import icfeavoir.pfe.controller.PFEActivity;
import icfeavoir.pfe.database.Database;
import icfeavoir.pfe.database.model.NoteDBModel;
import icfeavoir.pfe.database.model.OfflineDBModel;
import icfeavoir.pfe.model.User;

public class NEWNTProxy extends Proxy {
    public NEWNTProxy(PFEActivity activity) {
        super(activity);
    }

    @Override
    void callWithInternet(JSONObject json) {
        NEWNTCommunication com = new NEWNTCommunication(this.getContext(), this);
        com.call(json);
    }

    @Override
    void callWithoutInternet(final JSONObject json) {
        // save in DB to send later
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (json.has("user")) {
                    json.remove("user");
                }
                if (json.has("token")) {
                    json.remove("token");
                }
                // id is 0 to auto_increment
                OfflineDBModel query = new OfflineDBModel(0, json.toString(), Communication.API_ENDPOINTS.NEWNT.name());
                Database.getInstance(getContext()).getOfflineDAO().insert(query);

                // updating local DB
                if (json.has("proj") && json.has("student") && json.has("note")) {
                    try {
                        int note = json.getInt("note");
                        int projectId = json.getInt("proj");
                        int studentId = json.getInt("student");
                        String profUsername = User.getInstance().getUsername();
                        // check if note already exists
                        NoteDBModel noteDB = Database.getInstance(getContext())
                                .getNoteDAO()
                                .getNoteByStudentByProjectIdByProfUsername(studentId, projectId, profUsername);
                        if (noteDB == null) {
                            // save
                            noteDB = new NoteDBModel(studentId, projectId, profUsername, note);
                            Database.getInstance(getContext()).getNoteDAO().insert(noteDB);
                            Log.i("", "First insert");
                        } else {
                            // update
                            Database.getInstance(getContext()).getNoteDAO().updateNote(note, projectId, studentId, profUsername);
                            Log.i("", "update");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void returnDataAfterInternet(JSONObject json) {
    }

    @Override
    void sendDataToController(Object elements) {
    }
}
