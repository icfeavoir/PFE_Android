package icfeavoir.pfe.proxy;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

import icfeavoir.pfe.communication.LOGONCommunication;
import icfeavoir.pfe.controller.PFEActivity;

public class LOGONProxy extends Proxy {
    public LOGONProxy(PFEActivity activity) {
        super(activity);
    }

    @Override
    void getDataFromInternet(JSONObject json) {
        LOGONCommunication com = new LOGONCommunication(this.getContext(), this);
        com.getData(json);
    }

    @Override
    void getDataWithoutInternet(JSONObject json) {
        // do nothing, no login without connection
    }

    @Override
    void saveDataFromInternet(ArrayList<?> elements) {
        // do nothing, not saving account in DB
    }

    @Override
    public void returnData(JSONObject json) {
        Log.i("PFE", json.toString());
    }
}
