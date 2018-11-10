package icfeavoir.pfe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import java.util.List;

import icfeavoir.pfe.R;
import icfeavoir.pfe.adapters.MYJURAdapter;
import icfeavoir.pfe.model.Jury;
import icfeavoir.pfe.notification.NotificationPublisher;
import icfeavoir.pfe.proxy.MYJURProxy;
import icfeavoir.pfe.utils.Utils;

public class MYJURActivity extends PFEActivity {

    public static final String JURY_EXTRA = "jury_extra";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private MYJURAdapter myjurAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mRecyclerView = (RecyclerView) findViewById(R.id.myjurList);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        this.myjurAdapter = new MYJURAdapter(this);
        mRecyclerView.setAdapter(this.myjurAdapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        attemptMYJUR();

        Utils.scheduleNotification(this, Utils.getNotification(this, "Jury 1", "Votre jury commence dans 15 minutes !"), 1000, 1);
    }

    @Override
    public void displayData(Object data) {
        List<Jury> juries = (List<Jury>) data;
        this.myjurAdapter.setJuries(juries);

        // TODO: remove this
//        Intent i = new Intent(this, PRJActivity.class);
//        i.putExtra("projectId", 1);
//        startActivity(i);
//        finish();
    }

    public void clickJuryCard(Jury jury) {
        Intent intent = new Intent(this, JYINFActivity.class);
        intent.putExtra(JURY_EXTRA, jury.getJuryId());
        startActivity(intent);
    }

    private void attemptMYJUR() {
        MYJURProxy proxy = new MYJURProxy(this);
        proxy.call();
    }
}
