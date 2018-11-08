package icfeavoir.pfe.communication;

import android.content.Context;

import icfeavoir.pfe.proxy.Proxy;

public class JYINFCommunication extends Communication {
    public JYINFCommunication(Context context, Proxy proxy) {
        super(context, proxy, API_ENDPOINTS.JYINF);
    }
}
