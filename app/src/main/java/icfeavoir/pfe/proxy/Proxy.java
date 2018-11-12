package icfeavoir.pfe.proxy;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (Utils.isConnected(activity.getApplicationContext())) {
            // internet connection
            // add token if exists
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

    /**
     * Call with a query (to call in local)
     * @param query A string with a valid query
     */
    public void callWithoutInternet(String query) {
        String realQuery = query.split("\\?")[1];
        List<String> params = Arrays.asList(realQuery.split("&"));

        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        JSONObject json = new JSONObject(map);
        this.callWithoutInternet(json);
    }


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
