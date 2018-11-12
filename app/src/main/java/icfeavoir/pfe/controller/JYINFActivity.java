package icfeavoir.pfe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import icfeavoir.pfe.R;
import icfeavoir.pfe.model.Project;
import icfeavoir.pfe.proxy.JYINFProxy;

public class JYINFActivity extends PFEActivity {

    public static final String PROJECT_EXTRA = "project_extra";
    private LinearLayout layout;
    private int juryID;

    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jyinf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.inflater = getLayoutInflater();
        this.layout = findViewById(R.id.myprojList);

        try {
            this.juryID = getIntent().getExtras().getInt("jury_extra");
            attemptJYINF();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickProjectCard(Project project) {
        Intent intent = new Intent(this, PRJActivity.class);
        intent.putExtra(PROJECT_EXTRA, project.getProjectId());
        startActivity(intent);
    }


    @Override
    public void displayData(Object data) {
        final ArrayList<Project> projects = (ArrayList<Project>) data;
        final PFEActivity it = this;
        for (final Project project : projects) {
            View projectView = inflater.inflate(R.layout.project_card_layout, this.layout, false);

            TextView title = projectView.findViewById(R.id.project_title);
            title.setText(project.getTitle());

            TextView desc = projectView.findViewById(R.id.project_description);
            desc.setText(project.getDescription().substring(0, 200) + "...");

            projectView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(it, PRJActivity.class);
                    i.putExtra("projectId", project.getProjectId());
                    it.startActivity(i);
                }
            });

            layout.addView(projectView);
        }
    }

    private void attemptJYINF() {
        JSONObject json = new JSONObject();
        try {
            json.put("jury", this.juryID);
            JYINFProxy proxy = new JYINFProxy(this);
            proxy.call(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
