package icfeavoir.pfe.communication;

import android.content.Context;

import icfeavoir.pfe.proxy.Proxy;

public class POSTRCommunication extends Communication {
    public POSTRCommunication(Context context, Proxy proxy) {
        super(context, proxy, API_ENDPOINTS.POSTR);
    }
}
