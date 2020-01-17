package com.otemainc.mlipa.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.otemainc.mlipa.R;
import com.otemainc.mlipa.util.model.TransactionHistory;

import java.util.List;

public class TransactionHistoryAdapter extends ArrayAdapter<TransactionHistory>{

//the hero list that will be displayed
private List<TransactionHistory> transactionList;

//the context object
private Context mCtx;

//here we are getting the herolist and context
//so while creating the object of this adapter class we need to give herolist and context
public TransactionHistoryAdapter(List<TransactionHistory> transactionList, Context mCtx) {
        super(mCtx, R.layout.transaction, transactionList);
        this.transactionList = transactionList;
        this.mCtx = mCtx;
        }

//this method will return the list item
@Override
public View getView(int position, View convertView, ViewGroup parent) {
        //getting the layoutinflater
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        //creating a view with our xml layout
        View listViewItem = inflater.inflate(R.layout.transaction, null, true);

        //getting text views
        TextView textViewName = listViewItem.findViewById(R.id.textViewName);
        TextView textViewImageUrl = listViewItem.findViewById(R.id.textViewImageUrl);

        //Getting the hero for the specified position
        TransactionHistory transaction = transactionList.get(position);

        //setting hero values to textviews
        textViewName.setText(transaction.getCode());
        textViewImageUrl.setText(transaction.getTdate());

        //returning the listitem
        return listViewItem;
        }
}
