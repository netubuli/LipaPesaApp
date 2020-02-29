package com.otemainc.mlipa.ui.splash;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.otemainc.mlipa.ui.MainActivity;
import com.otemainc.mlipa.R;
import com.otemainc.mlipa.ui.auth.LoginActivity;
import com.otemainc.mlipa.util.helper.SessionManager;

public class SplashActivity extends Activity {
    private com.beardedhen.androidbootstrap.BootstrapProgressBar mProgress;
    private TextView loaderLabel;
    private SessionManager session;
    ViewFlipper flipper, textFlipper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show the splash screen
        setContentView(R.layout.activity_splash);
        TypefaceProvider.registerDefaultIconSets();
        flipper = findViewById(R.id.Flipper);
        textFlipper = findViewById(R.id.titles);
        mProgress = findViewById(R.id.progressBar);
        loaderLabel = findViewById(R.id.progressMessage);
        int[] images = {R.drawable.slide, R.drawable.slide1, R.drawable.slide2, R.drawable.slide3, R.drawable.slide4};
        int[] titles = {R.string.slide, R.string.slide1, R.string.slide2, R.string.slide3, R.string.slide4};
        for(int image:images){
            flipperImages(image);
        }
        for(int title:titles) {
            flipperTitles(title);
        }
        // Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                doWork();
                startApp();
                finish();
            }
        }).start();
    }

    private void flipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);
        flipper.addView(imageView);
        flipper.setFlipInterval(2000);
        flipper.setAutoStart(true);
        flipper.setInAnimation(this,android.R.anim.slide_in_left);
        flipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }
    private void flipperTitles(int title){
        TextView desc = new TextView(this);
        desc.setTextColor(getResources().getColor(R.color.colorWhite));
        desc.setTextSize(18);
        desc.setText(title);
        textFlipper.addView(desc);
        textFlipper.setFlipInterval(2000);
        textFlipper.setAutoStart(true);
        textFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        textFlipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }
    String[] launcher_message = {"Initializing system", "Starting engine", "opening payment gateway",
            "initializing stack", "Initializing security", "Loading pools", "More security initialization",
            "Starting system", "Loading app", "Updating local database"};

    private void doWork() {
        for (int progress = 0; progress < launcher_message.length; progress ++) {
            try {
                Thread.sleep(2000);
                //mProgress.setProgress(progress);
                loaderLabel.setText(launcher_message[progress] + " " + (progress+1)*10 + " %");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startApp() {
//Check if the user has been authenticated
        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            //User is logged in
            // show the main activity
            Intent main = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(main);
        }else {
            //User has not logged in
            //Show the LoginActivity
            Intent home = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(home);
        }
        finish();
    }
}