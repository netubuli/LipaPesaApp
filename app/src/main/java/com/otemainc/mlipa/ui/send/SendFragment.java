package com.otemainc.mlipa.ui.send;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.otemainc.mlipa.R;
import com.otemainc.mlipa.ui.MainActivity;
import com.otemainc.mlipa.util.helper.SQLiteHandler;

import java.util.HashMap;

public class SendFragment extends Fragment implements View.OnClickListener {

    private SendViewModel sendViewModel;
    private EditText text_reciever,text_amount;
    private Button send,cancel;
    private SQLiteHandler db;
    private String sender, reciever,amount;



    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        sendViewModel = ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        sendViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        db = new SQLiteHandler(getActivity().getApplicationContext());
// Fetching user details from sqlite
        HashMap<String, String> account = db.getUserDetails();
        sender = account.get("account");
        text_reciever = root.findViewById(R.id.txtRecipient);
        text_amount = root.findViewById(R.id.txtAmount);
        send = root.findViewById(R.id.btnSend);
        send.setOnClickListener(this);
        cancel = root.findViewById(R.id.btnCancel);
        cancel.setOnClickListener(this);
        reciever = text_reciever.getText().toString().trim();
        amount = text_amount.getText().toString().trim();

        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCancel:
                Intent main = new Intent(getContext(), MainActivity.class);
                startActivity(main);
                break;
            case R.id.btnSend:

                if(valid(reciever,amount)){
                    sendMoney(sender,reciever,amount);
                }


        }
    }

    private void sendMoney(String sender, String reciever, String amount) {

    }

    private boolean valid(String reciever, String amount) {
        boolean valid = true;
        if(reciever.isEmpty()||sender.length()<4){
            text_reciever.setError("Invalid Reciepient Account");
            valid = false;
        }else{
            text_reciever.setError(null);
        }
        if (amount.isEmpty() || amount.length() < 2) {
            text_amount.setError("Amount should be atleast Ksh 10");
            valid = false;
        }else {
            text_amount.setError(null);
        }
        return valid;
    }
}