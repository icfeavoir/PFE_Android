package icfeavoir.pfe.controller;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

import icfeavoir.pfe.R;
import icfeavoir.pfe.proxy.POSTRProxy;

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
        if (data == null) {
            // no image or no connection
            Toast.makeText(this, "Impossible de charger l'image", Toast.LENGTH_LONG).show();
        } else if (data instanceof Drawable) {
            Drawable drawable = (Drawable) data;
            this.imageView.setImageDrawable(drawable);
        }
    }
}
