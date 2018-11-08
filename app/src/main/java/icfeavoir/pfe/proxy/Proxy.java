package icfeavoir.pfe.proxy;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import icfeavoir.pfe.communication.Communication;
import icfeavoir.pfe.communication.LIJURCommunication;
import icfeavoir.pfe.communication.LOGONCommunication;
import icfeavoir.pfe.communication.MYJURCommunication;
import icfeavoir.pfe.communication.NEWNTCommunication;
import icfeavoir.pfe.communication.NOTESCommunication;
import icfeavoir.pfe.controller.PFEActivity;
import icfeavoir.pfe.model.User;
import icfeavoir.pfe.utils.Utils;

public abstract class Proxy {
    private final PFEActivity activity;

    public Proxy(PFEActivity activity) {
        this.activity = activity;
    }

    public void call() {
        this.call(new JSONObject());
    }

    public void call(JSONObject json) {
        this.beforeCalling(json);
        // TODO: remove the
        if (false && Utils.isConnected(activity.getApplicationContext())) {
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

    void saveDataFromInternet(List<?> elements) {}
    void beforeCalling(JSONObject json) {}

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

    public static Proxy proxyBuilder(Communication.API_ENDPOINTS endpoint, PFEActivity activity) {
        Proxy proxy = null;
        switch (endpoint) {
            case LOGON:
                proxy = new LOGONProxy(activity);
                break;
            case LIPRJ:
//                proxy = new LIPRJProxy(activity);
                break;
            case MYPRJ:
//                proxy = new MYPRJProxy(activity);
                break;
            case LIJUR:
                proxy = new LIJURProxy(activity);
                break;
            case MYJUR:
                proxy = new MYJURProxy(activity);
                break;
            case JYINF:
//                proxy = new JYINFProxy(activity);
                break;
            case POSTR:
//                proxy = new POSTRProxy(activity);
                break;
            case NOTES:
                proxy = new NOTESProxy(activity);
                break;
            case NEWNT:
                proxy = new NEWNTProxy(activity);
                break;
            case PORTE:
//                proxy = new PORTEProxy(activity);
                break;
        }
        return proxy;
    }
}
