package com.otemainc.mlipa.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.otemainc.mlipa.R;
import com.otemainc.mlipa.ui.send.SendFragment;
import com.otemainc.mlipa.ui.share.ShareFragment;
import com.otemainc.mlipa.ui.tools.ToolsFragment;

public class HomeFragment extends Fragment implements View.OnClickListener {
    /***
     * allow transaction history
     */

    private HomeViewModel homeViewModel;
    private CardView send,request,paybill, account;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        send = root.findViewById(R.id.bankcardId);
        send.setOnClickListener(this);
        request = root.findViewById(R.id.request);
        request.setOnClickListener(this);
        paybill = root.findViewById(R.id.payBill);
        paybill.setOnClickListener(this);

        return root;
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
        }
    }
}