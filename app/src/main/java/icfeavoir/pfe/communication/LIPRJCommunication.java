package icfeavoir.pfe.communication;

import android.content.Context;

import icfeavoir.pfe.proxy.Proxy;

public class LIPRJCommunication extends Communication {
    public LIPRJCommunication(Context context, Proxy proxy) {
        super(context, proxy, API_ENDPOINTS.LIPRJ);
    }
}
