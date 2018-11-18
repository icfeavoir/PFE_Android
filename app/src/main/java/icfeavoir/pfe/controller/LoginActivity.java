package icfeavoir.pfe.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import icfeavoir.pfe.R;
import icfeavoir.pfe.database.Database;
import icfeavoir.pfe.database.model.UserDBModel;
import icfeavoir.pfe.proxy.LOGONProxy;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends PFEActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        new Thread(new Runnable() {
            @Override
            public void run() {
                UserDBModel user = Database.getInstance(getApplicationContext()).getUserDAO().getUser();
                if (user != null){
                    connect(user.getUsername(), user.getPassword());
                }
            }
        }).start();


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    /**
     * Try to login
     */
    private void attemptLogin() {
        String user = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        connect(user, password);
    }

    private void connect(String user, String password) {
        LOGONProxy proxy = new LOGONProxy(this);
        JSONObject json = new JSONObject();
        try {
            json.put("user", user);
            json.put("pass", password);
            proxy.call(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayData(Object data) {
        if ((boolean) data){
            Intent intent = new Intent(this, MYJURActivity.class);
            startActivity(intent);
        }
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView textView = findViewById(R.id.error_message);
                    textView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

}

