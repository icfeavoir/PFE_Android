package icfeavoir.pfe.communication;

import android.content.Context;

import icfeavoir.pfe.proxy.Proxy;

public class NEWNTCommunication extends Communication {
    public NEWNTCommunication(Context context, Proxy proxy) {
        super(context, proxy);
        this.setQuery(API_ENDPOINTS.NEWNT.toString());
    }
}
