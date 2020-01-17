package com.otemainc.mlipa.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.otemainc.mlipa.R;
import com.otemainc.mlipa.util.model.TransactionHistory;

import java.util.List;

public class TransactionHistoryAdapter extends TableLayout.Adapter<TransactionHistoryAdapter.TransactionViewHolder>{

    private Context mCtx;
    private List<TransactionHistory> transactionHistories;

    public TransactionHistoryAdapter(Context mCtx, List<TransactionHistory> transactionHistories) {
        this.mCtx = mCtx;
        this.transactionHistories = transactionHistories;
    }
    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.table_layout, null);
        return new TransactionViewHolder(view);
    }
    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        TransactionHistory history = transactionHistories.get(position);

        holder.textViewcode.setText(history.getCode());
        holder.textViewTdate.setText(history.getTdate());
        holder.textViewSender.setText(history.getSender());
        holder.textViewAmount.setText(String.valueOf(history.getAmount()));
        holder.textViewReceiver.setText(history.getReceiver());
    }
    @Override
    public int getItemCount() {
        return transactionHistories.size();
    }
    class TransactionViewHolder extends TableLayout.ViewHolder {

        TextView textViewcode, textViewTdate, textViewSender, textViewAmount, textViewReceiver;

        public TransactionViewHolder(View itemView) {
            super(itemView);

            textViewcode = itemView.findViewById(R.id.code);
            textViewTdate = itemView.findViewById(R.id.tdate);
            textViewSender = itemView.findViewById(R.id.secondary);
            textViewAmount = itemView.findViewById(R.id.amount);
            textViewReceiver = itemView.findViewById(R.id.receiver);
        }
    }
}
