package com.otemainc.mlipa.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PasswordResetActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email;
    Button login;
    TextView register;
    ImageButton reset;
    ProgressDialog pDialog;
    private static final String TAG = PasswordResetActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
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

    private void passwordReset(final String emailText) {
        // Tag used to cancel the request
        String tag_string_req = "req_pwdReset";
        pDialog.setMessage("Sending request ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_PASSWORDRESET, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Reset Response: " + response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        // password reset successfully
                        Toast.makeText(getApplicationContext(),"Your password has been reset successfully. Please check your email for instructions on how to login ",Toast.LENGTH_LONG).show();
                        // Launch main activity
                        loadLogin();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),"Reset Error "+errorMsg, Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "Reset Error: " +error + ">>" + error.networkResponse.statusCode
                        + ">>" + error.networkResponse.data
                        + ">>" + error.getCause()
                        + ">>" + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error Resetting Password please try again "+error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
                login.setEnabled(true);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("email", emailText);
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
    private void loadLogin() {
        Intent loadMain = new Intent(PasswordResetActivity.this, LoginActivity.class);
        startActivity(loadMain);
        finish();
    }
}
