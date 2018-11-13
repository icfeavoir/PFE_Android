package icfeavoir.pfe.proxy;

import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import icfeavoir.pfe.communication.POSTRCommunication;
import icfeavoir.pfe.controller.PFEActivity;

public class POSTRProxy extends Proxy {
    public POSTRProxy(PFEActivity activity) {
        super(activity);
    }

    @Override
    void callWithInternet(final JSONObject json) {
        final POSTRCommunication com = new POSTRCommunication(this.getContext(), this);
        String url = com.call(json, false);
        this.sendDataToController(url);
    }

    @Override
    void callWithoutInternet(JSONObject json) {
        this.sendDataToController(null);
    }

    @Override
    void saveDataFromInternet(List<?> elements) {

    }

    @Override
    public void returnDataAfterInternet(JSONObject json) {

    }

    @Override
    void sendDataToController(Object elements) {
        this.getActivity().displayData(elements);
    }
}
