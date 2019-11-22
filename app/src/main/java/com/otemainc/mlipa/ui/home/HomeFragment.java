package com.otemainc.mlipa.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.otemainc.mlipa.R;
import com.otemainc.mlipa.ui.gallery.GalleryFragment;
import com.otemainc.mlipa.ui.send.SendFragment;
import com.otemainc.mlipa.ui.share.ShareFragment;
import com.otemainc.mlipa.ui.tools.ToolsFragment;
import com.otemainc.mlipa.util.AppConfig;
import com.otemainc.mlipa.util.AppController;
import com.otemainc.mlipa.util.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private HomeViewModel homeViewModel;
    private CardView send,request,paybill, history;
    private String phoneNo;
    private TextView balance;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    private static final String TAG = HomeFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        balance = root.findViewById(R.id.txtBalance);
        send = root.findViewById(R.id.bankcardId);
        send.setOnClickListener(this);
        request = root.findViewById(R.id.request);
        request.setOnClickListener(this);
        paybill = root.findViewById(R.id.payBill);
        paybill.setOnClickListener(this);
        history = root.findViewById(R.id.history);
        history.setOnClickListener(this);
        db = new SQLiteHandler(getActivity().getApplicationContext());
        HashMap<String, String> userPhone = db.getUserDetails();
        phoneNo = userPhone.get("phone");
        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        getBalance(phoneNo);
        return root;
    }

    private void getBalance(final String phone) {
        // Tag used to cancel the request
        String tag_string_send = "Get_Balance";
        pDialog.setMessage("Querying Balance ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_BALANCE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Query Response: " + response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONObject transaction = jObj.getJSONObject("balance");
                        String amount = transaction.getString("amount");
                        Toast.makeText(getActivity().getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();
                        //Display BALANCE
                        balance.setText("KSH " + amount);
                    } else {
                        // Error occurred. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity().getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Transaction Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), " An error has occurred during balance query "+error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("user", phone);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_send);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bankcardId:
                SendFragment sendFrag= new SendFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, sendFrag, "findSendFragment")
                        .addToBackStack(null).commit();
                break;
            case R.id.request:
                ShareFragment shareFrag= new ShareFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, shareFrag, "findShareFragment")
                        .addToBackStack(null).commit();
                break;
            case R.id.payBill:
                ToolsFragment toolFrag= new ToolsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, toolFrag, "findThisFragment")
                        .addToBackStack(null).commit();
                break;
            case R.id.history:
                GalleryFragment galFrag= new GalleryFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, galFrag, "findGalleryFragment")
                        .addToBackStack(null).commit();
                break;
        }
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}