package icfeavoir.pfe.communication;

import android.content.Context;
import icfeavoir.pfe.proxy.Proxy;

public class LIJURCommunication extends Communication {
    public LIJURCommunication(Context context, Proxy proxy) {
        super(context, proxy, API_ENDPOINTS.LIJUR);
    }
}
