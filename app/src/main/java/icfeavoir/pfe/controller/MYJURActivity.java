package icfeavoir.pfe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import icfeavoir.pfe.R;
import icfeavoir.pfe.model.Jury;
import icfeavoir.pfe.proxy.MYJURProxy;

public class MYJURActivity extends PFEActivity {

    public static final String JURY_EXTRA = "jury_extra";

    private LinearLayout layout;

    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view_home);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });

        this.layout = findViewById(R.id.myjurList);
        this.inflater = getLayoutInflater();

        attemptMYJUR();
    }

    @Override
    public void displayData(Object data) {
        List<Jury> juries = (List<Jury>) data;
        for (final Jury jury : juries) {
            final View juryView = inflater.inflate(R.layout.jury_card_layout, this.layout, false);

            TextView id = juryView.findViewById(R.id.jury_id_title);
            id.setText("Jury n°" + jury.getJuryId());

            TextView date = juryView.findViewById(R.id.jury_date);
            date.setText(jury.getDate());

            juryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickJuryCard(jury);
                }
            });

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layout.addView(juryView);
                }
            });
        }
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
