package icfeavoir.pfe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import icfeavoir.pfe.R;
import icfeavoir.pfe.adapters.JYINFAdapter;
import icfeavoir.pfe.model.Project;
import icfeavoir.pfe.proxy.JYINFProxy;

public class JYINFActivity extends PFEActivity {

    public static final String PROJECT_EXTRA = "project_extra";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private JYINFAdapter jyinfAdapter;

    private int juryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jyinf);
        mRecyclerView = (RecyclerView) findViewById(R.id.jyinfList);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        this.jyinfAdapter = new JYINFAdapter(this);
        mRecyclerView.setAdapter(this.jyinfAdapter);

        this.juryID = getIntent().getExtras().getInt("jury_extra");
        attemptJYINF();
    }

    public void clickProjectCard(Project project) {
        Intent intent = new Intent(this, PRJActivity.class);
        intent.putExtra(PROJECT_EXTRA, project.getProjectId());
        startActivity(intent);
    }


    @Override
    public void displayData(Object data) {
        ArrayList<Project> projects = (ArrayList<Project>) data;
        Log.i("tata", "" + projects.size());
        this.jyinfAdapter.setProjects(projects);
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
