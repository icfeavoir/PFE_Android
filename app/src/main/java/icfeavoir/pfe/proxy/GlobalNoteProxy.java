package icfeavoir.pfe.proxy;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import icfeavoir.pfe.controller.PFEActivity;
import icfeavoir.pfe.controller.PRJActivity;
import icfeavoir.pfe.database.Database;

/**
 * Special Proxy only in local
 */
public class GlobalNoteProxy extends Proxy {
    public GlobalNoteProxy(PFEActivity activity) {
        super(activity);
    }

    @Override
    void callWithInternet(JSONObject json) {
        this.callWithoutInternet(json);
    }

    @Override
    void callWithoutInternet(final JSONObject json) {
        if (json.has("proj")) {
            try {
                final int projectId = json.getInt("proj");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // save in the database
                        try {
                            if (json.has("SAVE") && json.has("note")) {
                                Double note = json.getDouble("note");
                                Database.getInstance(getContext())
                                        .getProjectDAO()
                                        .updateGlobalNote(note, projectId);
                            } else if (json.has("GET")) {
                                Log.i("", "GET GLOBAL NOTE");
                                Double globalNote = Database.getInstance(getContext())
                                        .getProjectDAO()
                                        .getGlobalNote(projectId);
                                sendDataToController(globalNote);
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void returnDataAfterInternet(JSONObject json) {

    }

    @Override
    void sendDataToController(Object elements) {
        if (elements instanceof Integer) {
            Map<PRJActivity.possibleDataType, Double> map = new HashMap<>();
            map.put(PRJActivity.possibleDataType.GLOBAL_NOTE, (Double) elements);
            this.getActivity().displayData(map);
        }
    }
}
