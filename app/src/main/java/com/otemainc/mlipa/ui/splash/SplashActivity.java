package com.otemainc.mlipa.ui.splash;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.otemainc.mlipa.ui.MainActivity;
import com.otemainc.mlipa.R;
import com.otemainc.mlipa.ui.auth.LoginActivity;
import com.otemainc.mlipa.util.helper.SessionManager;

public class SplashActivity extends Activity {
    private ProgressBar mProgress;
    private TextView loaderLabel;
    private SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show the splash screen
        setContentView(R.layout.activity_splash);
        mProgress = findViewById(R.id.progressBar);
        loaderLabel = findViewById(R.id.progressMessage);
        // Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                doWork();
                startApp();
                finish();
            }
        }).start();
    }
    String[] launcher_message = {"Initializing\tsystem","Starting\tengine","opening\tpayment\tgateway",
            "initializing\tstack","Initializing\tsecurity","Loading\tpools","More\tsecurity\tinitialization",
            "Starting\tsystem","Loading\tapp","Updating\tlocal\tdatabase"};

    private void doWork() {
        for (int progress=0,i=0; progress<101; progress+=10,i++) {
            try {
                Thread.sleep(1000);
                mProgress.setProgress(progress);
                    loaderLabel.setText(launcher_message[i]+" "+progress+" %");
            } catch (Exception e) {
                e.printStackTrace();
                           }
        }
    }

    private void startApp() {
//Check if the user has been authenticated
        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            //User is not yet logged in
            // show the login activity
            Intent login = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(login);
        }else {
            //User has logged in
            //Show the HomeActivity
            Intent home = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(home);
        }
        finish();
    }
}