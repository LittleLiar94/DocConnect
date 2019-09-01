package com.example.docconnect.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docconnect.Adapter.MyLaborAdapter;
import com.example.docconnect.Common.Common;
import com.example.docconnect.Common.SpacesItemDecoration;
import com.example.docconnect.Interface.IAllPremisesInfoLoadListener;
import com.example.docconnect.Interface.IAllServicesLoadListener;
import com.example.docconnect.Model.Labor;
import com.example.docconnect.Model.Premise;
import com.example.docconnect.Model.Service;
import com.example.docconnect.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ServiceStep2Fragment extends Fragment implements IAllServicesLoadListener, IAllPremisesInfoLoadListener {

    static ServiceStep2Fragment instance;
    public static ServiceStep2Fragment getInstance(){
        if (instance == null)
            instance = new ServiceStep2Fragment();
        return instance;
    }

    Unbinder unbinder;

    LocalBroadcastManager localBroadcastManager;
    IAllServicesLoadListener iAllServicesLoadListener;
    IAllPremisesInfoLoadListener iAllPremisesInfoLoadListener;

    LayoutInflater inflater;

    @BindView(R.id.recycler_review)
    RecyclerView recycler_review;
    @BindView(R.id.recycler_labor)
    RecyclerView recycler_labor;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_description)
    TextView tv_description;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_rating)
    TextView tv_rating;
    @BindView(R.id.tv_ratingTimes)
    TextView tv_ratingTimes;
    @BindView(R.id.chip_group_services)
    ChipGroup chip_group_services;
    @BindView(R.id.rating_premise)
    RatingBar rating_premise;
    @BindView(R.id.tv_mon_opening_hours)
    TextView tv_mon_opening_hours;
    @BindView(R.id.tv_tue_opening_hours)
    TextView tv_tue_opening_hours;
    @BindView(R.id.tv_wed_opening_hours)
    TextView tv_wed_opening_hours;
    @BindView(R.id.tv_thur_opening_hours)
    TextView tv_thur_opening_hours;
    @BindView(R.id.tv_fri_opening_hours)
    TextView tv_fri_opening_hours;
    @BindView(R.id.tv_sat_opening_hours)
    TextView tv_sat_opening_hours;
    @BindView(R.id.tv_sun_opening_hours)
    TextView tv_sun_opening_hours;


    // Part 27:
    private BroadcastReceiver serviceLoadDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Common.KEY_SERVICE_LOAD_DONE.equals(intent.getAction()))
            {
                ArrayList<Service> serviceArrayList = intent.getParcelableArrayListExtra(Common.KEY_SERVICE_LOAD_DONE);
//                Toast.makeText(context, ""+serviceArrayList.get(0).getServiceId(""), Toast.LENGTH_SHORT).show();

                iAllServicesLoadListener.onAllServicesOfSelectedPremiseLoadSuccess(serviceArrayList);
            }
            else if (Common.KEY_INFO_LOAD_DONE.equals(intent.getAction()))
            {
                Premise permiseInfo = intent.getParcelableExtra(Common.KEY_INFO_LOAD_DONE);
//                Toast.makeText(context, ""+permiseInfo.getName(), Toast.LENGTH_SHORT).show();

                iAllPremisesInfoLoadListener.onAllPremisesInfoLoadSuccess((Premise) permiseInfo);
            }
            else if (Common.KEY_LABOR_LOAD_DONE.equals(intent.getAction())){
                ArrayList<Labor> laborArrayList = intent.getParcelableArrayListExtra(Common.KEY_LABOR_LOAD_DONE);
                // Create review adapter here
                MyLaborAdapter adapter = new MyLaborAdapter(getContext(),laborArrayList);
                recycler_labor.setAdapter(adapter);

            }

            // Create review adapter here
//            MyBarberAdapter adapter = new MyBarberAdapter(getContext(),barberArrayList);
//            recycler_review.setAdapter(adapter);
//            tv_title.setText(permiseInfo.getName());

        }
    };

    @Override
    public void onDestroy() {
    localBroadcastManager.unregisterReceiver(serviceLoadDoneReceiver);
       super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(serviceLoadDoneReceiver, new IntentFilter(Common.KEY_SERVICE_LOAD_DONE));
        localBroadcastManager.registerReceiver(serviceLoadDoneReceiver, new IntentFilter(Common.KEY_INFO_LOAD_DONE));
        localBroadcastManager.registerReceiver(serviceLoadDoneReceiver, new IntentFilter(Common.KEY_LABOR_LOAD_DONE));

        iAllServicesLoadListener = this;
        iAllPremisesInfoLoadListener = this;
        inflater = LayoutInflater.from(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_service_step2,container,false);
        unbinder = ButterKnife.bind(this, itemView);
        initView();
        return itemView;
    }


    private void initView()
    {

        recycler_labor.setHasFixedSize(true);
        recycler_labor.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recycler_labor.addItemDecoration(new SpacesItemDecoration(4));

    }


    @Override
    public void onAllServicesLoadSuccess(List<String> serviceList) {

    }

    @Override
    public void onAllServicesLoadFailed(String message) {

    }

    @Override
    public void onAllServicesOfSelectedPremiseLoadSuccess(List<Service> serviceIdList) {
        int pos=0;
        //Initialize the chipgroup view
        chip_group_services.removeAllViews();
        for(pos=0; pos<serviceIdList.size(); pos++) {
//            tv_services.setText(serviceIdList.get(pos).getServiceId(""));

            final Chip item = (Chip) inflater.inflate(R.layout.chip_item, null);
            item.setText(serviceIdList.get(pos).getServiceId(""));
            item.setTag(pos);
            item.setClickable(false);
            item.setCloseIconVisible(false);
            item.setCheckable(false);

            if (Common.service.equals(serviceIdList.get(pos).getServiceId("")))
            {
                item.setChipBackgroundColorResource(R.color.colorPrimary);
                item.setTextColor(getActivity().getResources().getColor(android.R.color.white));
            }
            else {
                item.setChipBackgroundColorResource(R.color.colorDefault);
                item.setTextColor(getActivity().getResources().getColor(R.color.colorTextHeavy));
            }

            chip_group_services.addView(item);
        }
    }

    @Override
    public void onAllServicesOfSelectedPremiseLoadFailed(String message) {

    }

    @Override
    public void onAllPremisesInfoLoadSuccess(Premise premiseInfo) {
        tv_title.setText(premiseInfo.getName());
        tv_phone.setText(premiseInfo.getPhone());
        tv_address.setText(premiseInfo.getLocation());
        tv_description.setText(premiseInfo.getDescription());
        tv_rating.setText(premiseInfo.getRating().toString());
        tv_ratingTimes.setText("("+premiseInfo.getRatingTimes().toString()+")");
        rating_premise.setRating((float)premiseInfo.getRating() / premiseInfo.getRatingTimes());

        // Belows logic is used to calculate the close time.
        SimpleDateFormat inDateFormat = new SimpleDateFormat("h:mmaa");
        long timeSlotDiffInHour = 0;
        Date openTime=null, openTime2 = null, lastTimeSlot = null, closeTime = new Date();
        try {
            openTime = inDateFormat.parse(premiseInfo.getTimeSlot().get(0));
            openTime2 = inDateFormat.parse(premiseInfo.getTimeSlot().get(1));
            lastTimeSlot = inDateFormat.parse(premiseInfo.getTimeSlot().get(premiseInfo.getTimeSlot().size()-1));
            timeSlotDiffInHour = (openTime2.getTime() - openTime.getTime()); // Get the different between timeSlot
            closeTime.setTime(lastTimeSlot.getTime()+timeSlotDiffInHour);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Common.openingHours = (inDateFormat.format(openTime)+" - "+inDateFormat.format(closeTime))
                .replace("AM", "am").replace("PM", "pm");

        int pos = 0;
        if(premiseInfo.getOpeningDayOfWeek().get(pos))
            tv_mon_opening_hours.setText(Common.openingHours);
        else
            tv_mon_opening_hours.setText("Closed");
        if(premiseInfo.getOpeningDayOfWeek().get(pos+1))
            tv_tue_opening_hours.setText(Common.openingHours);
        else
            tv_tue_opening_hours.setText("Closed");
        if(premiseInfo.getOpeningDayOfWeek().get(pos+2))
            tv_wed_opening_hours.setText(Common.openingHours);
        else
            tv_wed_opening_hours.setText("Closed");
        if(premiseInfo.getOpeningDayOfWeek().get(pos+3))
            tv_thur_opening_hours.setText(Common.openingHours);
        else
            tv_thur_opening_hours.setText("Closed");
        if(premiseInfo.getOpeningDayOfWeek().get(pos+4))
            tv_fri_opening_hours.setText(Common.openingHours);
        else
            tv_fri_opening_hours.setText("Closed");
        if(premiseInfo.getOpeningDayOfWeek().get(pos+5))
            tv_sat_opening_hours.setText(Common.openingHours);
        else
            tv_sat_opening_hours.setText("Closed");
        if(premiseInfo.getOpeningDayOfWeek().get(pos+6))
            tv_sun_opening_hours.setText(Common.openingHours);
        else
            tv_sun_opening_hours.setText("Closed");
    }

    @Override
    public void onAllPremisesInfoLoadFailed(String message) {

    }
}
