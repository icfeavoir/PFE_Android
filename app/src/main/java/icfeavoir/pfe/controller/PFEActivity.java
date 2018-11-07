package icfeavoir.pfe.controller;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public abstract class PFEActivity extends AppCompatActivity {

    /**
     * Called on proxy response
     * @param data
     */
    public abstract void displayData(Object data);
}
