package icfeavoir.pfe.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import icfeavoir.pfe.R;
import icfeavoir.pfe.proxy.POSTRProxy;
import icfeavoir.pfe.utils.PicassoTrustAll;

public class POSTRActivity extends PFEActivity {

    private int projectId;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postr);
        this.imageView = findViewById(R.id.poster_image);

        try {
            this.projectId = getIntent().getExtras().getInt("projectId");
            POSTRProxy proxy = new POSTRProxy(this);
            JSONObject json = new JSONObject();
            json.put("proj", this.projectId);
            proxy.call(json);
        } catch (NullPointerException | JSONException e) {
            // do nothing
        }
    }
    /**
     * Called on proxy response
     *
     * @param data the data
     */
    @Override
    public void displayData(Object data) {
        final TextView error = findViewById(R.id.poster_error);
        if (data == null) {
            // no image or no connection
            error.setText("Impossible de charger l'image");
        } else if (data instanceof String) {
            final String url = (String) data;
            final PFEActivity it = this;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PicassoTrustAll.getInstance(it.getApplicationContext())
                            .load(url)
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    error.setVisibility(View.INVISIBLE);
                                    error.setHeight(0);
                                }

                                @Override
                                public void onError() {
                                    error.setText("Une erreur est survenue.");
                                }
                            });
                }
            });
        }
    }
}
