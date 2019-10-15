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

import com.otemainc.mlipa.MainActivity;
import com.otemainc.mlipa.R;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private AutoCompleteTextView emailText;
    private EditText passwordText;
    private Button login;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText = findViewById(R.id.txtEmail);
        passwordText = findViewById(R.id.txtPassword);
        login = findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailText.getText().toString().trim();
                final String pass = passwordText.getText().toString().trim();
                login.setEnabled(false);
                //create and show the progressDialog
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
                if(validate(email,pass)){
                    auth(email,pass, progressDialog);
                }else{
                    progressDialog.dismiss();
                    login.setEnabled(true);
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
    private void auth(String email, String pass, ProgressDialog progressDialog) {
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
