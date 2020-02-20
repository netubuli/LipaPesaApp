package com.otemainc.mlipa.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.otemainc.mlipa.ui.MainActivity;
import com.otemainc.mlipa.R;
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

public class LoginActivity extends AppCompatActivity {
    private AutoCompleteTextView emailText;
    private EditText passwordText;
    private Button login;
    private TextView register, reset;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private static final String TAG = SignUpActivity.class.getSimpleName();

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
            loadMain();
        }
        login = findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailText.getText().toString().trim();
                final String pass = passwordText.getText().toString().trim();
                login.setEnabled(false);
                //create and show the progressDialog
                if(validate(email,pass)){
                        passwordText.setError(null);
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
        reset = findViewById(R.id.btnReset);
        reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent loadReset = new Intent(LoginActivity.this,PasswordResetActivity.class);
                startActivity(loadReset);
                finish();
            }
        });
    }
    private void checkLogin(final String email, final String pass) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        pDialog.setMessage("Logging in ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);
                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String oname = user.getString("oname");
                        String email = user.getString("email");
                        String phone = user.getString("phone");
                        String idno = user.getString("id");
                        String account = user.getString("account");
                        String account_type = user.getString("account_type");
                        String created_at = user.getString("created_at");
                        // Inserting row in users table
                        db.addUser(name, oname, email, phone, idno, uid, account, account_type, created_at);
                        Toast.makeText(getApplicationContext(),"Welcome " + name+" "+oname,Toast.LENGTH_LONG).show();
                        // Launch main activity
                        loadMain();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),"Login Error "+errorMsg, Toast.LENGTH_LONG).show();
                        login.setEnabled(true);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    login.setEnabled(true);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " +error + ">>" + error.networkResponse.statusCode
                                + ">>" + error.networkResponse.data
                                + ">>" + error.getCause()
                                + ">>" + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error Loging In please try again "+error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
                login.setEnabled(true);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", pass);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.setIndeterminate(true);
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
        if (pass.isEmpty() || pass.length() < 4) {
            passwordText.setError("Password / PIN should be at least 4 characters long");
            valid = false;
        }else {
            passwordText.setError(null);
        }
        return valid;
    }
}
