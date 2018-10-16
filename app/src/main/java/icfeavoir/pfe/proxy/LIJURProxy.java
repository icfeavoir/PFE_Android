package icfeavoir.pfe.proxy;

import org.json.JSONObject;

import java.util.ArrayList;

import icfeavoir.pfe.communication.LIJURCommunication;
import icfeavoir.pfe.controller.PFEActivity;

public class LIJURProxy extends Proxy {
    public LIJURProxy(PFEActivity activity) {
        super(activity);
    }

    @Override
    void callWithInternet(JSONObject json) {
        LIJURCommunication com = new LIJURCommunication(this.getContext(), this);
        com.call(json);
    }

    @Override
    void callWithoutInternet(JSONObject json) {

    }

    @Override
    void saveDataFromInternet(ArrayList<?> elements) {

    }

    @Override
    public void returnDataAfterInternet(JSONObject json) {
    }

    @Override
    void sendDataToController(Object elements) {

    }
}
