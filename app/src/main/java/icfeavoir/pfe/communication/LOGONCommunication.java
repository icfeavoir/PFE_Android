package icfeavoir.pfe.communication;

import android.content.Context;
import icfeavoir.pfe.proxy.Proxy;

public class LOGONCommunication extends Communication {
    public LOGONCommunication(Context context, Proxy proxy) {
        super(context, proxy);
        this.setQuery("LOGON");
    }
}
