package icfeavoir.pfe.communication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import icfeavoir.pfe.proxy.Proxy;

public abstract class Communication extends AsyncTask<String, Void, String> {

    private static final String URL = "https://192.168.4.248/pfe/webservice.php?";
    private String query;
    private Context context;
    private Proxy proxy;
    private boolean shouldReturn;

    Communication(Context context, Proxy proxy, API_ENDPOINTS endpoint) {
        this.context = context;
        this.proxy = proxy;
        this.setQuery(endpoint.toString());
    }

    public enum API_ENDPOINTS {
        LOGON,
        LIPRJ,
        MYPRJ,
        LIJUR,
        MYJUR,
        JYINF,
        POSTR,
        NOTES,
        NEWNT,
        PORTE,
    }

    Proxy getProxy() {
        return proxy;
    }

    private String getQuery() {
        return query;
    }

    private void setQuery(String query) {
        this.query = query;
    }

    private void request(String endpoint, JSONObject data, final Proxy callbackProxy) {
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

        try{
            this.execute(url.toString());
        } catch (Exception e ){
            e.printStackTrace();
        }
    }

    public void call() {
        this.call(new JSONObject());
    }

    public void call(JSONObject json) {
        this.call(json, true);
    }

    public void call(JSONObject json, boolean shouldReturn) {
        this.shouldReturn = shouldReturn;
        if (this.getQuery() != null) {
            request(this.getQuery(), json, this.getProxy());
        } else {
            Log.e("COMMUNICATION", "NO QUERY");
        }
    }

    // ASYNC PART

    @Override
    protected String doInBackground(String... params){
        String urlQuery = params[0];
        StringBuilder sb = new StringBuilder();
        String res = "";

        // NEW WE CA
        TrustManager trustManager = new TrustManager();
        trustManager.getCertificate(this.context);
        try {
            URL url = new URL(urlQuery);

            //Créer une connection
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setSSLSocketFactory(trustManager.getSSLContext().getSocketFactory());
            InputStream inputStream = httpsURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                sb.append(line);
            }
            res = sb.toString();

        } catch (Exception e) {
            Log.e("QUERY", "Error with url: " + urlQuery + " | Exception: " + e.getClass());
        }

        return res;
    }

    @Override
    protected void onPostExecute(String result){
        if (this.shouldReturn) {
            try {
                JSONObject json = new JSONObject(result);
                this.proxy.checkDataAfterInternet(json);
            } catch (JSONException e) {
                Log.i("NON JSON RES", result);
                Log.e("POST ASYNC", "Response is not a JSON");
            }
        }
    }
}
