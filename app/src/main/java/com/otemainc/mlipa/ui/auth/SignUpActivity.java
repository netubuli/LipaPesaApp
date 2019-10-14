package com.otemainc.mlipa.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.otemainc.mlipa.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    /***
     * This view is responsible for registering the users to the system using their email,
     * full names, id number, phone numbers and password
     * they also need to accept the terms of service in order to proceed with the registration
     * once registered the user can log into the system and transact
     * @param savedInstanceState
     */
private Button signUp;
private CheckBox agree;
private TextView signIn;
EditText 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signIn = findViewById(R.id.btnRLogin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loadSignIn = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(loadSignIn);
                finish();
            }
        });
        signUp = findViewById(R.id.btnRRegister);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        agree = findViewById(R.id.checkBox);
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the button is checked
                if(agree.isChecked()){
                    //if true enable Sign Up button
                    signUp.setEnabled(true);
                }else {
                    signUp.setEnabled(false);

                }
            }
        });
    }
    private boolean validate(@NotNull String name, String email, String phone, String pass, String cpass) {
        boolean valid = true;
        if (name.isEmpty() || name.length() < 4) {
            nameText.setError("Name should be at least 4 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }
        if(phone.isEmpty() || phone.length() <10 ||!android.util.Patterns.PHONE.matcher(phone).matches()){
            phoneText.setError("Invalid Phone number");
            valid = false;
        }else{
            phoneText.setError(null);
        }
        if (pass.isEmpty() || pass.length() < 6) {
            passwordText.setError("Password should be at least 6 characters long");
            valid = false;
        }else {
            passwordText.setError(null);
        }
        if(cpass.isEmpty()){
            cPasswordText.setError("Retype Password");
            valid = false;
        }else if(!cpass.equals(pass)){
            cPasswordText.setError("Passwords do not match");
            valid = false;
        }else{
            cPasswordText.setError(null);
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
