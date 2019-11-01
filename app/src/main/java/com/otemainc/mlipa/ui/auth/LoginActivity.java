package com.otemainc.mlipa.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.otemainc.mlipa.ui.MainActivity;
import com.otemainc.mlipa.R;
import com.otemainc.mlipa.util.helper.SQLiteHandler;
import com.otemainc.mlipa.util.helper.SessionManager;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private AutoCompleteTextView emailText;
    private EditText passwordText;
    private Button login;
    private TextView register;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText = findViewById(R.id.txtEmail);
        passwordText = findViewById(R.id.txtPassword);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // Session manager
        session = new SessionManager(getApplicationContext());
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        login = findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailText.getText().toString().trim();
                final String pass = passwordText.getText().toString().trim();
                login.setEnabled(false);
                //create and show the progressDialog
                if(validate(email,pass)&& isValidPassword(pass)){
                    checkLogin(email, pass);
                }else{
                    login.setEnabled(true);
                    Toast.makeText(LoginActivity.this,"An error occured please check the submitted data and try again",Toast.LENGTH_LONG).show();
                }
            }
        });
        register = findViewById(R.id.btnRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loadReg = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(loadReg);
                finish();
            }
        });
    }
    private void checkLogin(String email, String pass) {
        //Login code goes here
        //load main after successfull login
        loadMain();
    }
    private void loadMain() {
        Intent loadMain = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(loadMain);
        finish();
    }
    private boolean validate(@NotNull String email, String pass) {
        boolean valid = true;
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }
        if (pass.isEmpty() || pass.length() < 6) {
            passwordText.setError("Password should be at least 6 characters long");
            valid = false;
        }else {
            passwordText.setError(null);
        }
        return valid;
    }
    //Check if password is valid
    public boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
