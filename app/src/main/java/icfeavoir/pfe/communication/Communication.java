package icfeavoir.pfe.communication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.Iterator;
import java.net.URL;

import icfeavoir.pfe.proxy.Proxy;

public abstract class Communication extends AsyncTask<String, Void, String> {

    private static final String URL = "https://192.168.4.248/www/pfe/webservice.php?";
    private String query;
    private Context context;
    private Proxy proxy;

    Communication(Context context, Proxy proxy) {
        this.context = context;
        this.proxy = proxy;
    }

    Proxy getProxy() {
        return proxy;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    void request(String endpoint, JSONObject data, final Proxy callbackProxy) {
        StringBuilder url = new StringBuilder(URL);
        url.append("q=").append(endpoint);

        // taking data to put in url
        Iterator<String> keys = data.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = "";
            try {
                value = data.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            url.append("&").append(key).append("=").append(value);
        }

        Log.i("PFE", "request is: " + url.toString());

        this.execute(url.toString());
    }

    public void getData(JSONObject json) {
        if (this.getQuery() != null) {
            request(this.getQuery(), json, this.getProxy());
        } else {
            Log.e("COMMUNICATION", "NO QUERY");
        }
    }

    // ASYNC PART

    @Override
    protected String doInBackground(String... params){
        String url = params[0];
        BufferedReader reader = null;
        StringBuilder sb = null;
        String dataToSend = "";
        try {
            URL finalUrl = new URL(url.toString());
            URLConnection conn = finalUrl.openConnection();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String result){
        Log.i("PFE", "response is: " + result);
    }
}
