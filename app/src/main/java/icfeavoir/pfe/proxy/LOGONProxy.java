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
    void getDataFromInternet(JSONObject json) {
        // save data
        User user = User.getInstance();
        try {
            user.setUsername(json.getString("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LOGONCommunication com = new LOGONCommunication(this.getContext(), this);
        com.getData(json);
    }

    @Override
    void getDataWithoutInternet(JSONObject json) {
        // do nothing, no login without connection
        Log.e("CONNECTION", "Not connected, cannot connect");
    }

    @Override
    void saveDataFromInternet(ArrayList<?> elements) {
        // do nothing, not saving account in DB
    }

    @Override
    public void returnDataAfterInternet(JSONObject json) {
        User user = User.getInstance();
        try {
            user.setToken(json.getString("token"));
            Log.i("TOKEN", user.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    void sendDataToController(ArrayList<?> elements) {
        ArrayList<Boolean> al = new ArrayList<>();
        al.add(true);
        this.getActivity().displayData(al);
    }
}
