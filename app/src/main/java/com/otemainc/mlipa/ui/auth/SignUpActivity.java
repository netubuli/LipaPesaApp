package com.otemainc.mlipa.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.otemainc.mlipa.R;
import com.otemainc.mlipa.ui.MainActivity;
import com.otemainc.mlipa.util.AppConfig;
import com.otemainc.mlipa.util.AppController;
import com.otemainc.mlipa.util.helper.SQLiteHandler;
import com.otemainc.mlipa.util.helper.SessionManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    /***
     * This view is responsible for registering the users to the system using their email,
     * full names, id number, phone numbers and password
     * they also need to accept the terms of service in order to proceed with the registration
     * once registered the user can log into the system and transact
     */
    private Button signUp;
    private CheckBox agree;
    private TextView signIn;
    private EditText phoneText, idText,passwordText,cPasswordText;
    private AutoCompleteTextView lastNameText, otherNameText,emailText;
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        phoneText = findViewById(R.id.txtPhone);
        idText = findViewById(R.id.txtId);
        passwordText = findViewById(R.id.txtPassword);
        cPasswordText = findViewById(R.id.txtCpass);
        lastNameText = findViewById(R.id.txtRFname);
        otherNameText = findViewById(R.id.txtROname);
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
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // Session manager
        session = new SessionManager(getApplicationContext());
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
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
                if(validate(lName,oName, email,phone,id,pass,cPass)){

                    if (!isValidPassword(pass)) {
                        passwordText.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, one special character and no space");
                        signUp.setEnabled(true);
                    }else{
                        passwordText.setError(null);
                        registerUser(lName,oName, email,phone,id,pass);
                    }
                }else{
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

    private void registerUser(final String lName, final String oName, final String email, final String phone, final String id, final String pass) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        pDialog.setMessage("Registering ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String final_uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String l_name = user.getString("name");
                        String ot_name = user.getString("oname");
                        String final_email = user.getString("email");
                        String final_phone = user.getString("phone");
                        String final_id = user.getString("id");
                        String final_created_at = user.getString("created_at");
                        // Inserting row in users table
                        db.addUser(l_name, ot_name, final_email, final_phone, final_id, final_uid, final_created_at);
                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
                        // Launch login activity
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("lname", lName);
                params.put("oname", oName);
                params.put("email", email);
                params.put("phone", phone);
                params.put("idno", id);
                params.put("password", pass);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
        if(id.isEmpty()||id.length()<6){
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
    public boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }
}
