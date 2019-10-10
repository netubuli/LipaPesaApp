package com.otemainc.mlipa.ui.splash;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.otemainc.mlipa.MainActivity;
import com.otemainc.mlipa.R;
import com.otemainc.mlipa.ui.auth.SignUpActivity;
import com.otemainc.mlipa.util.SharedPref;

public class SplashActivity extends Activity {
    private ProgressBar mProgress;
    private TextView loaderLabel;

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
        for (int progress=0; progress<101; progress+=10) {
            try {
                Thread.sleep(1000);
                mProgress.setProgress(progress);
                for(int i = 0; i<launcher_message.length; i++) {
                    loaderLabel.setText(launcher_message[i]+" "+progress+" %");
                }
            } catch (Exception e) {
                e.printStackTrace();
                           }
        }
    }

    private void startApp() {
//Check if the user has been authenticated
        if (SharedPref.getInstance().getString("is_registered").equalsIgnoreCase("")) {
            //User is not yet logged in
            // show the login activity
            Intent login = new Intent(SplashActivity.this, SignUpActivity.class);
            startActivity(login);
        }else {
            //User has logged in
            //Show the HomeActivity
            Intent home = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(home);
        }
        finish();
    }
}