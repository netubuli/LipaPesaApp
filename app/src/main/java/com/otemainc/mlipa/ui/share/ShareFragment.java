package com.otemainc.mlipa.ui.share;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.otemainc.mlipa.R;
import com.otemainc.mlipa.ui.MainActivity;
import com.otemainc.mlipa.ui.send.SendFragment;
import com.otemainc.mlipa.util.AppConfig;
import com.otemainc.mlipa.util.AppController;
import com.otemainc.mlipa.util.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShareFragment extends Fragment implements View.OnClickListener {

    private ShareViewModel shareViewModel;
    private EditText text_reciever,text_amount;
    private Button withdraw,cancel;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    private static final String TAG = SendFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        shareViewModel = ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);
        final TextView textView = root.findViewById(R.id.text_share);
        shareViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        text_reciever = root.findViewById(R.id.txtRecipient);
        text_amount = root.findViewById(R.id.txtAmount);
        withdraw = root.findViewById(R.id.btnWithdraw);
        withdraw.setOnClickListener(this);
        cancel = root.findViewById(R.id.btnCancel);
        cancel.setOnClickListener(this);
        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancel:
                Intent main = new Intent(getContext(), MainActivity.class);
                startActivity(main);
                break;
            case R.id.btnWithdraw:
            db = new SQLiteHandler(getActivity().getApplicationContext());
// Fetching user details from sqlite and passing on to the sender variable
            HashMap<String, String> account = db.getUserDetails();
            final String sender = account.get("phone");
            final String agent = text_reciever.getText().toString().trim();
            final String amount = text_amount.getText().toString().trim();
            if(valid(agent,amount)){
                cancel.setEnabled(false);
                withdraw.setEnabled(false);
                //if(confirm(sender)) {
                withdraw(sender, agent, amount);
                //}
            }else{
                cancel.setEnabled(true);
                withdraw.setEnabled(true);
            }
        }
    }

    private void withdraw(final String sender, final String agent, final String amount) {
        // Tag used to cancel the request
        String tag_string_send = "Send_Money";
        pDialog.setMessage("Sending ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_WITHDRAW, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Withdraw Response: " + response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONObject transaction = jObj.getJSONObject("transaction");
                        String message = transaction.getString("message");
                        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        cancel.setEnabled(true);
                        withdraw.setEnabled(true);
                        // Launch MAIN activity
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        // Error occurred. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity().getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        cancel.setEnabled(true);
                        withdraw.setEnabled(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Transaction Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), " An error has occurred during cash withdrawal "+error.getMessage(), Toast.LENGTH_LONG).show();
                cancel.setEnabled(true);
                withdraw.setEnabled(true);
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("sender", sender);
                params.put("reciever", agent);
                params.put("amount", amount);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_send);
    }
    private boolean valid(String agent, String amount) {
        boolean valid = true;
        if(agent.isEmpty()||agent.length()<4){
            text_reciever.setError("Invalid Recipient Account");
            valid = false;
        }else{
            text_reciever.setError(null);
        }
        if (amount.isEmpty() || amount.length() < 2) {
            text_amount.setError("Amount should be at least Ksh 50");
            valid = false;
        }else {
            text_amount.setError(null);
        }
        return valid;
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private boolean confirm(String reciever){
        //get user response
        boolean response = false;
        // if the response is true


        return response;
    }
}