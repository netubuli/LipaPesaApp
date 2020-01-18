package com.otemainc.mlipa.ui.gallery;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.otemainc.mlipa.R;
import com.otemainc.mlipa.util.AppConfig;
import com.otemainc.mlipa.util.AppController;
import com.otemainc.mlipa.util.adapter.TransactionHistoryAdapter;
import com.otemainc.mlipa.util.helper.SQLiteHandler;
import com.otemainc.mlipa.util.model.TransactionHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    //a list to store all the transaction histories
   private List<TransactionHistory> transactionHistories;

    //the table layout
    TableLayout transactionView;
    List<TransactionHistory> transactionList;
    ProgressBar progressBar;
    private SQLiteHandler db;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        //getting the recyclerview from xml
        transactionView = root.findViewById(R.id.transactionTableLayout);

        //getting the progressbar
        progressBar = root.findViewById(R.id.loadingProgress);

        transactionList = new ArrayList<>();
        db = new SQLiteHandler(getActivity().getApplicationContext());
        HashMap<String, String> account = db.getUserDetails();
        final String user = account.get("phone");

        //this method will fetch and parse the data
        loadHeroList(user);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    private void loadHeroList(final String user) {
        String tag_string_req = "Get_Transaction_History";
        //making the progressbar visible
        progressBar.setVisibility(View.VISIBLE);
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_TRANSAC_DETAILS, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray transactionArray = obj.getJSONArray("t_history");
                            //now looping through all the elements of the json array
                            for (int i = 0; i < transactionArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject transactionObject = transactionArray.getJSONObject(i);
                                View tableRow = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.table_layout, null, false);
                                TextView t_id = tableRow.findViewById(R.id.t_id);
                                TextView code = tableRow.findViewById(R.id.code);
                                TextView t_date = tableRow.findViewById(R.id.tdate);
                                TextView sender = tableRow.findViewById(R.id.sender);
                                TextView amount = tableRow.findViewById(R.id.amount);
                                TextView receiver = tableRow.findViewById(R.id.receiver);
                                t_id.setText(String.valueOf(i+1));
                                code.setText(transactionObject.getString("tid"));
                                t_date.setText(transactionObject.getString("tdate"));
                                sender.setText(transactionObject.getString("sender"));
                                amount.setText(transactionObject.getString("amount"));
                                receiver.setText(transactionObject.getString("receiver"));
                                transactionView.addView(tableRow);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })        {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to url
                Map<String, String> params = new HashMap<>();
                params.put("user", user);
                return params;
            }
            };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        }
    /*private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }*/
}