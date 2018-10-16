package icfeavoir.pfe.proxy;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import icfeavoir.pfe.communication.NEWNTCommunication;
import icfeavoir.pfe.controller.PFEActivity;
import icfeavoir.pfe.model.User;

public class NEWNTProxy extends Proxy {
    public NEWNTProxy(PFEActivity activity) {
        super(activity);
    }

    @Override
    void callWithInternet(JSONObject json) {
        NEWNTCommunication com = new NEWNTCommunication(this.getContext(), this);
        com.call(json);
        Log.i("NEWNT", "sent : " + json.toString());
    }

    @Override
    void callWithoutInternet(JSONObject json) {
        // save in DB to send later
    }

    @Override
    public void returnDataAfterInternet(JSONObject json) {
        Log.i("NEWNT", json.toString());
    }

    @Override
    void sendDataToController(Object elements) {
//        this.getActivity().displayData(elements);
    }
}
