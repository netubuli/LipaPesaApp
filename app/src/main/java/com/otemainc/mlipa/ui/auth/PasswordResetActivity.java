package com.otemainc.mlipa.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.otemainc.mlipa.R;

public class PasswordResetActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email;
    Button login;
        TextView register;
        ImageButton reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        email = findViewById(R.id.Remail);
        login = findViewById(R.id.RsignIn);
        register = findViewById(R.id.RSignUp);
        reset = findViewById(R.id.reset);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.reset:
                String emailText = email.getText().toString().trim();
                passwordReset(emailText);

                break;
            case R.id.RsignIn:
                Intent loginIntent = new Intent(PasswordResetActivity.this,LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;
            case R.id.RSignUp:
                Intent registerIntent = new Intent(PasswordResetActivity.this,SignUpActivity.class);
                startActivity(registerIntent);
                finish();
                break;
        }
    }

    private void passwordReset(String emailText) {
    }
}
