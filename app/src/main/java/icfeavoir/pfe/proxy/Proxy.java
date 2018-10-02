package icfeavoir.pfe.proxy;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import icfeavoir.pfe.controller.PFEActivity;
import icfeavoir.pfe.utils.Utils;

public abstract class Proxy {
    private final PFEActivity activity;

    public Proxy(PFEActivity activity) {
        this.activity = activity;
        Log.i("PFE", "Proxy created");
    }

    public void getData(JSONObject json) {
        if (Utils.isConnected(activity.getApplicationContext())) {
            // internet connection
            this.getDataFromInternet(json);
        } else {
            // no internet connection
            this.getDataWithoutInternet(json);
        }
    }

    public PFEActivity getActivity() {
        return activity;
    }

    Context getContext() {
        return this.getActivity().getApplicationContext();
    }

    abstract void getDataFromInternet(JSONObject json);
    abstract void getDataWithoutInternet(JSONObject json);
    abstract void saveDataFromInternet(ArrayList<?> elements);

    public abstract void returnData(JSONObject json);
}
