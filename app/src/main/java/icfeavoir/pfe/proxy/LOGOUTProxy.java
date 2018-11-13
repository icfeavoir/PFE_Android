package icfeavoir.pfe.proxy;

import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

import icfeavoir.pfe.controller.LoginActivity;
import icfeavoir.pfe.controller.PFEActivity;
import icfeavoir.pfe.database.Database;

public class LOGOUTProxy extends Proxy {

    private PFEActivity activity;

    public LOGOUTProxy(PFEActivity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    void callWithInternet(JSONObject json) {
        this.callWithoutInternet(json);
    }

    @Override
    void callWithoutInternet(JSONObject json) {
        Log.i("btn", "clicked disco proxy");
        Database.getInstance(this.getContext()).getUserDAO().delete();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void returnDataAfterInternet(JSONObject json) {

    }

    @Override
    void sendDataToController(Object elements) {

    }
}
