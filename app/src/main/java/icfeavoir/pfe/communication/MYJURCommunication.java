package icfeavoir.pfe.communication;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import icfeavoir.pfe.proxy.Proxy;

public class MYJURCommunication extends Communication {
    public MYJURCommunication(Context context, Proxy proxy) {
        super(context, proxy);
        this.setQuery("MUJYR");
    }
}
