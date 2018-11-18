package icfeavoir.pfe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import icfeavoir.pfe.R;
import icfeavoir.pfe.proxy.LOGOUTProxy;

public abstract class PFEActivity extends AppCompatActivity {

    NavigationView navigationView;

    /**
     * Called on proxy response
     * @param data the data
     */
    public abstract void displayData(Object data);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_myjur) {
            Intent intent = new Intent(this, MYJURActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_lijur) {

        } else if (id == R.id.menu_logout) {
            LOGOUTProxy proxy = new LOGOUTProxy(this);
            proxy.call();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
