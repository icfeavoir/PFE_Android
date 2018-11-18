package icfeavoir.pfe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jyinf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.inflater = getLayoutInflater();
        this.layout = findViewById(R.id.myprojList);
        this.errorText = findViewById(R.id.error_text);

        this.errorText.setVisibility(View.INVISIBLE);
        this.errorText.setHeight(0);

        try {
            this.juryID = getIntent().getExtras().getInt("jury_extra");
            attemptJYINF();
        } catch (Exception e) {
            e.printStackTrace();
            this.noProjectsException();
        }
    }

    private void noProjectsException() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorText.setVisibility(View.VISIBLE);
                errorText.setHeight(20);
                errorText.setText("Pas de projet");
            }
        });
    }

    public void clickProjectCard(Project project) {
        Intent intent = new Intent(this, PRJActivity.class);
        intent.putExtra(PROJECT_EXTRA, project.getProjectId());
        startActivity(intent);
    }

    @Override
    public void displayData(Object data) {
        final ArrayList<Project> projects = (ArrayList<Project>) data;
        if (projects.size() <= 0) {
            this.noProjectsException();
            return;
        }

        for (final Project project : projects) {
            final View projectView = inflater.inflate(R.layout.project_card_layout, layout, false);

            TextView title = projectView.findViewById(R.id.project_title);
            title.setText(project.getTitle());

            TextView desc = projectView.findViewById(R.id.project_description);
            if (project.getDescription().length() >= 200) {
                desc.setText(project.getDescription().substring(0, 200) + "...");
            } else {
                desc.setText(project.getDescription());
            }

            projectView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickProjectCard(project);
                }
            });
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layout.addView(projectView);
                }
            });
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
            this.noProjectsException();
        }
    }
}
