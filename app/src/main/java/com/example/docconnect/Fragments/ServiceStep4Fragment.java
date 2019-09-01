package com.example.docconnect.Fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.docconnect.Common.Common;
import com.example.docconnect.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class ServiceStep4Fragment extends Fragment {

    static ServiceStep4Fragment instance;
    public static ServiceStep4Fragment getInstance(){
        if (instance == null)
            instance = new ServiceStep4Fragment();
        return instance;
    }

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;
    Dialog dialog;

    @BindView(R.id.txt_booking_labor_text)
    TextView txt_booking_labor_text;
    @BindView(R.id.txt_booking_time_next)
    TextView txt_booking_time_next;
    @BindView(R.id.txt_premise_address)
    TextView txt_premise_address;
    @BindView(R.id.txt_premise_name)
    TextView txt_premise_name;
    @BindView(R.id.txt_premise_open_hours)
    TextView txt_premise_open_hours;
    @BindView(R.id.txt_premise_phone)
    TextView txt_premise_phone;
    @BindView(R.id.txt_premise_website)
    TextView txt_premise_website;

    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
//        txt_booking_time_next.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))

        txt_booking_labor_text.setText(Common.currentLabor.getName());
        txt_booking_time_next.setText(new StringBuilder(Common.currentPremise.getTimeSlot().get(Common.currentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(Common.bookingDate.getTime())));

        txt_premise_address.setText(Common.currentPremise.getLocation());
//        txt_premise_website.setText(Common.currentPremise.getWebsite());
        txt_premise_name.setText(Common.currentPremise.getName());
        txt_premise_phone.setText(Common.currentPremise.getPhone());
        txt_premise_open_hours.setText(Common.openingHours);
//        txt_premise_open_hours.setText(Common.currentPremise.getOpenHours());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simpleDateFormat  = new SimpleDateFormat("dd/MM/yyy");
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceiver, new IntentFilter(Common.KEY_CONFIRM_BOOKING));

        // init Dialog
        dialog = new SpotsDialog.Builder().setCancelable(false).setContext(getContext())
                .build();

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_service_step4,container,false);
        unbinder = ButterKnife.bind(this, itemView);
        return itemView;
    }
}
