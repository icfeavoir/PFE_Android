package icfeavoir.pfe.communication;

import android.content.Context;

import icfeavoir.pfe.proxy.Proxy;

public class NOTESCommunication extends Communication {

    public NOTESCommunication(Context context, Proxy proxy) {
        super(context, proxy, API_ENDPOINTS.NOTES);
    }
}
