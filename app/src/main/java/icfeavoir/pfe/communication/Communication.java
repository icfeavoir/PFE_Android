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

    Communication(Context context, Proxy proxy) {
        this.context = context;
        this.proxy = proxy;
    }

    enum API_ENDPOINTS {
        LOGON("LOGON"),
        LIPRJ("LIPRJ"),
        MYPRJ("MYPRJ"),
        LIJUR("LIJUR"),
        MYJUR("MYJUR"),
        JYINF("JYINF"),
        POSTR("POSTR"),
        NOTES("NOTES"),
        NEWNT("NEWNT"),
        PORTE("PORTE");

        private String name;

        //Constructeur
        API_ENDPOINTS(String name){
            this.name = name;
        }

        @Override
        public String toString(){
            return name;
        }
    };

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

        try{
            this.execute(url.toString());
        } catch (Exception e ){
            e.printStackTrace();
        }
    }


    public void call(JSONObject json) {
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
//        try {
//            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                    return null;
//                }
//
//                public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                }
//
//                public void checkServerTrusted(X509Certificate[] certs, String authType) {
//                }
//            }};
//
//            // Install the all-trusting trust manager
//            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//
//            // Create all-trusting host name verifier
//            HostnameVerifier allHostsValid = new HostnameVerifier() {
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            };
//
//            // Install the all-trusting host verifier
//            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//
//            URL url = new URL(urlQuery);
//            URLConnection con = url.openConnection();
//            Reader reader = new InputStreamReader(con.getInputStream());
//            while (true) {
//                int ch = reader.read();
//                if (ch == -1) {
//                    break;
//                }
//                sb.append((char) ch);
//            }
//            res = sb.toString();
//        } catch (Exception e) {
//            Log.e("QUERY", "Error with url: " + urlQuery + " | Exception: " + e.getClass());
//        }

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
        try {
            JSONObject json = new JSONObject(result);
            this.proxy.checkDataAfterInternet(json);
        } catch (JSONException e) {
            Log.i("NON JSON RES", result);
            Log.e("POST ASYNC", "Response is not a JSON");
        }
    }
}
