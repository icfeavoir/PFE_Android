package icfeavoir.pfe.proxy;

import android.graphics.drawable.Drawable;

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
    void callWithInternet(JSONObject json) {
        POSTRCommunication com = new POSTRCommunication(this.getContext(), this);
        String url = "";
        try {
            url = com.call(json, false);
        } catch (Exception e) {
            // may produce exception since the communication will not return a json as usual but it's ok
        }
        Drawable drawable;
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            drawable = Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            drawable = null;
        }
        this.sendDataToController(drawable);
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
        if (elements instanceof Drawable) {
            this.getActivity().displayData(elements);
        }
    }
}
