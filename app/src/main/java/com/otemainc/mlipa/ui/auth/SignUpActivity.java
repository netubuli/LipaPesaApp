package com.otemainc.mlipa.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.otemainc.mlipa.R;

import org.jetbrains.annotations.NotNull;

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
private EditText phoneText, idText,passwordText,cPasswordText;
private AutoCompleteTextView lastNameText, otherNameText,emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        phoneText = findViewById(R.id.txtPhone);
        idText = findViewById(R.id.txtId);
        passwordText = findViewById(R.id.txtPassword);
        cPasswordText = findViewById(R.id.txtCpass);
        lastNameText = findViewById(R.id.txtRFname);
        otherNameText = findViewById(R.id.txtRFname);
        emailText = findViewById(R.id.txtRemail);
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
                final String lName = lastNameText.getText().toString().trim();
                final String oName = otherNameText.getText().toString().trim();
                final String email = emailText.getText().toString().trim();
                final String phone = phoneText.getText().toString().trim();
                final String id = idText.getText().toString().trim();
                final String pass = passwordText.getText().toString().trim();
                final String cPass = cPasswordText.getText().toString().trim();
                //create and show the progressDialog
                final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();
                if(validate(lName,oName, email,phone,id,pass,cPass)){

                    if (!isValidPassword(pass)) {
                        passwordText.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, one special character and no space");
                        progressDialog.dismiss();
                        signUp.setEnabled(true);
                    }else{
                        passwordText.setError(null);
                        save(lName,oName, email,phone,id,pass, progressDialog);
                    }
                }else{
                    progressDialog.dismiss();
                    signUp.setEnabled(true);
                }

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

    private void save(String lName, String oName, String email, String phone, String id, String pass, ProgressDialog progressDialog) {

    }

    private boolean validate(@NotNull String lName, String oName, String email, String phone, String id, String pass, String cpass) {
        boolean valid = true;
        if (lName.isEmpty() || lName.length() < 3) {
            lastNameText.setError("Last name should be at least 3 characters");
            valid = false;
        } else {
            lastNameText.setError(null);
        }
        if (oName.isEmpty() || oName.length() < 4) {
            otherNameText.setError("Name should be at least 4 characters");
            valid = false;
        } else {
            otherNameText.setError(null);
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
        if(id.isEmpty()||id.length()<10){
            idText.setError("Invalid Id/Passport number");
            valid = false;
        }else{
            idText.setError(null);
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
    //Create a generator for id

    //Use the Id to authenticate the user against the original credentials

    //Check for user permission

    //if admin

    //else if Agent

    //else if basic user

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
