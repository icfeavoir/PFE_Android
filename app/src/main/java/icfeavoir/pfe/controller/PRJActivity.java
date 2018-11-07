package icfeavoir.pfe.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import icfeavoir.pfe.R;
import icfeavoir.pfe.model.Project;
import icfeavoir.pfe.proxy.LIPRJProxy;

public class PRJActivity extends PFEActivity {

    private int projectId;
    private TextView title;
    private Button posterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prj);

        this.title = findViewById(R.id.project_title);
        this.posterButton = findViewById(R.id.project_poster_button);

        try {
            this.projectId = getIntent().getExtras().getInt("projectId");
            this.getProjectInfo(this.projectId);
        } catch (NullPointerException e) {
            this.noProjectException();
        }
    }

    private void getProjectInfo(int projectId) {
        JSONObject json = new JSONObject();
        try {
            json.put("projectId", this.projectId);
            LIPRJProxy proxy = new LIPRJProxy(this);
            proxy.call(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void noProjectException() {
        this.title.setText("Erreur: aucun projet trouvé");
        this.posterButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void displayData(Object data) {
        try {
            Project project = (Project) data;
            this.title.setText(project.getTitle());
            this.posterButton.setText(R.string.go_to_poster_btn);
            Log.i("PROJET", project.toString());
        } catch (Exception e) {
            this.noProjectException();
        }
    }
}
