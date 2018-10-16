package icfeavoir.pfe.proxy;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import icfeavoir.pfe.controller.PFEActivity;
import icfeavoir.pfe.model.User;
import icfeavoir.pfe.utils.Utils;

public abstract class Proxy {
    private final PFEActivity activity;

    public Proxy(PFEActivity activity) {
        this.activity = activity;
    }

    public void call(JSONObject json) {
        if (Utils.isConnected(activity.getApplicationContext())) {
            // internet connection
            // add token is exists
            User user = User.getInstance();
            if (user.getToken() != null && user.getUsername() != null) {
                try {
                    json.put("user", user.getUsername());
                    json.put("token", user.getToken());
                } catch (JSONException e) {
                }
            }
            this.callWithInternet(json);
        } else {
            // no internet connection
            this.callWithoutInternet(json);
        }
    }

    PFEActivity getActivity() {
        return activity;
    }

    Context getContext() {
        return this.getActivity().getApplicationContext();
    }

    abstract void callWithInternet(JSONObject json);
    abstract void callWithoutInternet(JSONObject json);

    void saveDataFromInternet(ArrayList<?> elements) {

    }

    public void checkDataAfterInternet(JSONObject json) {
        try {
            String result = json.getString("result");
            if (result.equals("OK")) {
                this.returnDataAfterInternet(json);
            }
            // to remove
            else {
                Log.e("BACKEND_ERR", json.toString());
            }
        } catch (JSONException e) {
            Log.e("PROXY", "No result found");
        }
    }

    public abstract void returnDataAfterInternet(JSONObject json);

    abstract void sendDataToController(Object elements);
}
