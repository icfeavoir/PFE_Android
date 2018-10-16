package icfeavoir.pfe.proxy;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import icfeavoir.pfe.communication.LOGONCommunication;
import icfeavoir.pfe.controller.PFEActivity;
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
        sendDataToController(false);
    }

    @Override
    void saveDataFromInternet(ArrayList<?> elements) {
        // do nothing, not saving account in DB
    }

    @Override
    public void returnDataAfterInternet(JSONObject json) {
        try {
            String result = json.getString("result");
            if (result.equals("OK")) {
                User user = User.getInstance();
                user.setToken(json.getString("token"));
                sendDataToController(true);
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
