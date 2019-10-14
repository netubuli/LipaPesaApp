package com.otemainc.mlipa.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.otemainc.mlipa.R;

public class LoginActivity extends AppCompatActivity {
    /***
     * disallow the shared prefs
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
