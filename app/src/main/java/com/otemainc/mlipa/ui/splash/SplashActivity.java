package com.otemainc.mlipa.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.otemainc.mlipa.MainActivity;
import com.otemainc.mlipa.R;

/***
 *
 */
public class SplashActivity extends AppCompatActivity {
    ProgressBar progress;
    TextView loaderLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progress = findViewById(R.id.progressBar);
        loaderLabel = findViewById(R.id.progressMessage);
        progress.setIndeterminate(true);
        String[] launcher_message = {"Initializing system","Starting engine","opening payment gateway","initializing stack","Initializing security",
        "Loading pools","More security initialization","Starting system","Loading app","Updating local database"};
        int i = 0;
        while (i < launcher_message.length) {
            for (int j =0; j<=100; j++) {
                if(j==100 && 1==9){
                    Intent loadMain = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(loadMain);
                    finish();
                }else {
                    loaderLabel.setText(new StringBuilder().append(launcher_message[i]).append(" ").append(j).append(" %").toString());
                    i++;
                }

            }

        }



    }

}
