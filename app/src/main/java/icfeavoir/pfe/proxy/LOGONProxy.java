package icfeavoir.pfe.proxy;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import icfeavoir.pfe.communication.LOGONCommunication;
import icfeavoir.pfe.controller.PFEActivity;
import icfeavoir.pfe.database.Database;
import icfeavoir.pfe.database.model.UserDBModel;
import icfeavoir.pfe.model.User;

public class LOGONProxy extends Proxy {
    public LOGONProxy(PFEActivity activity) {
        super(activity);
    }

    @Override
    public void checkDataAfterInternet(JSONObject json) {
        // override because we handle the KO exception (pbm with pass)
        this.returnDataAfterInternet(json);
    }

    @Override
    void callWithInternet(JSONObject json) {
        // save data
        User user = User.getInstance();
        try {
            user.setUsername(json.getString("user"));
            user.setPassword(json.getString("pass"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LOGONCommunication com = new LOGONCommunication(this.getContext(), this);
        com.call(json);
    }

    @Override
    void callWithoutInternet(JSONObject json) {
        // do nothing, no login without connection
        Log.e("CONNECTION", "Not connected, cannot connect");
        sendDataToController(true);
    }

    @Override
    void saveDataFromInternet(List<?> elements) {
        // do nothing, not saving account in DB
    }

    @Override
    public void returnDataAfterInternet(JSONObject json) {
        try {
            String result = json.getString("result");
            if (result.equals("OK")) {
                final User user = User.getInstance();
                user.setToken(json.getString("token"));
                sendDataToController(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Database.getInstance(getContext()).getUserDAO().delete();
                        UserDBModel userDB = new UserDBModel(user.getUsername(), user.getPassword());
                        Database.getInstance(getContext()).getUserDAO().insert(userDB);
                    }
                }).start();
            } else {
                sendDataToController(false);
            }

        } catch (JSONException e) {
            Log.e("PROXY", "No result found");
        }
    }

    @Override
    void sendDataToController(Object elements) {
        this.getActivity().displayData(elements);
    }
}
