package icfeavoir.pfe.controller;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;

import icfeavoir.pfe.R;
import icfeavoir.pfe.notification.NotificationPublisher;

public abstract class PFEActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Called on proxy response
     * @param data the data
     */
    public abstract void displayData(Object data);

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // close the menu
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // if we are in MYJUR, prev is connection (so do nothing)/
            if (this instanceof MYJURActivity) {
                // do nothing
            } else {
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_myjur) {
            Intent intent = new Intent(this, MYJURActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_lijur) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
