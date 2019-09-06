package com.example.docconnect.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.docconnect.Adapter.MyTimeSlotAdapter;
import com.example.docconnect.Common.Common;
import com.example.docconnect.Common.SpacesItemDecoration;
import com.example.docconnect.Interface.IAllPremisesInfoLoadListener;
import com.example.docconnect.Interface.ITimeSlotLoadListener;
import com.example.docconnect.Model.Premise;
import com.example.docconnect.Model.Service;
import com.example.docconnect.Model.TimeSlot;
import com.example.docconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class ServiceStep3Fragment extends Fragment implements ITimeSlotLoadListener, IAllPremisesInfoLoadListener {

    static ServiceStep3Fragment instance;
    public static ServiceStep3Fragment getInstance(){
        if (instance == null)
            instance = new ServiceStep3Fragment();
        return instance;
    }

    Unbinder unbinder;

    Calendar currentDate, SelectedDate;
    int DD, YYYY, mm;
    int selectedYear, selectedMonth, selectedDayOfMonh;
    String dayOfWeek;

    DatePickerDialog datePickerDialog;
    LayoutInflater inflater;

    SimpleDateFormat simpleDateFormat;

    //variable
    DocumentReference laborDoc;
    ITimeSlotLoadListener iTimeSlotLoadListener;
    LocalBroadcastManager localBroadcastManager;

    AlertDialog dialog;

    IAllPremisesInfoLoadListener iAllPremisesInfoLoadListener;

    @BindView(R.id.btn_date)
    Button btn_date;

    @OnClick(R.id.btn_date)
    void btnClick() {
//        Toast.makeText(getContext(), DD+"/"+(mm+1)+"/"+YYYY, Toast.LENGTH_SHORT).show();
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SelectedDate.set(year,month,dayOfMonth);
                String dateOfWeek= SelectedDate.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.SHORT, Locale.getDefault());
                btn_date.setText(dayOfMonth+"/"+(month+1)+"/"+year+" ("+dateOfWeek+")");

                selectedYear = year;
                selectedMonth = month;
                selectedDayOfMonh = dayOfMonth;

                // Load the time slot of selected Date
                loadAvailableTimeSlotOfBarber(Common.currentLabor.getLaborId(),
                        Common.currentPremise, simpleDateFormat.format(SelectedDate.getTime()));

                Common.bookingDate = SelectedDate;
            }
        },selectedYear, selectedMonth, selectedDayOfMonh);

        Date newDate = currentDate.getTime();
        datePickerDialog.getDatePicker().setMinDate(newDate.getTime() - (newDate.getTime()%(24*60*60*1000)));
        datePickerDialog.show();
    }

    @BindView(R.id.recycler_time_slot)
    RecyclerView recycler_time_slot;

    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Calendar date = Calendar.getInstance();
//            date.add(Calendar.DATE, 0); // Add current date

//            Premise permiseInfo = intent.getParcelableExtra(Common.KEY_TIME_SLOT);

//            iAllPremisesInfoLoadListener.onAllPremisesInfoLoadSuccess((Premise) permiseInfo);

//            loadAvailableTimeSlotOfBarber(Common.currentLabor.getLaborId(), permiseInfo,
//                    simpleDateFormat.format(new Date()));

//            MyLaborAdapter adapter = new MyLaborAdapter(getContext(),laborArrayList);
//            recycler_labor.setAdapter(adapter);

//            if(Common.KEY_INFO_LOAD_DONE.equals(intent.getAction()))
//            {
                if(Common.KEY_DISPLAY_TIME_SLOT.equals(intent.getAction())) {
                    Premise permiseInfo = intent.getParcelableExtra(Common.KEY_DISPLAY_TIME_SLOT);
                    //                Toast.makeText(context, ""+serviceArrayList.get(0).getServiceId(""), Toast.LENGTH_SHORT).show();


                    loadAvailableTimeSlotOfBarber(Common.currentLabor.getLaborId(), permiseInfo,
                            simpleDateFormat.format(new Date()));
                }
            }
//        }
    };

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTimeSlot);
        super.onDestroy();
    }

    private void loadAvailableTimeSlotOfBarber(String laborId, Premise permiseInfo, String selectedDate) {
        dialog.show();

        // /AllPremises/Premise1/AllLabors/Ali
        laborDoc = FirebaseFirestore.getInstance()
                .collection("AllPremises")
                .document(Common.currentPremiseTemp.getPremiseId())
                .collection("AllLabors")
                .document(laborId);

        // Get info of this labor
        laborDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){ // if labor available
                        // Get information of booking
                        // If not created, return empty
                        CollectionReference date = FirebaseFirestore.getInstance()
                                .collection("AllPremises")
                                .document(Common.currentPremiseTemp.getPremiseId())
                                .collection("AllLabors")
                                .document(Common.currentLabor.getLaborId())
                                .collection(selectedDate); // bookDate is date simple formate with dd_MM_yyy
                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if(querySnapshot.isEmpty())
                                    {  // If no appointment
                                        List<TimeSlot> timeSlots = new ArrayList<>();
                                        iTimeSlotLoadListener.onTimeSlotLoadEmpty(timeSlots, permiseInfo);
                                    }
                                    else {
                                        // If appointment available
                                        List<TimeSlot>timeSlots = new ArrayList<>();
                                        for(QueryDocumentSnapshot document:task.getResult())
                                            timeSlots.add(document.toObject(TimeSlot.class));

                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots,permiseInfo);
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
                            }
                        });
                    }
                }
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = LayoutInflater.from(getContext());
        iTimeSlotLoadListener =this;
        iAllPremisesInfoLoadListener=this;

        SelectedDate = Calendar.getInstance();
        currentDate = Calendar.getInstance();
        DD = currentDate.get(Calendar.DAY_OF_MONTH);
        mm = currentDate.get(Calendar.MONTH);
        YYYY = currentDate.get(Calendar.YEAR);
        dayOfWeek = currentDate.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.SHORT, Locale.getDefault());

        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy"); // 26/05/2019 (this is key)

        //Listen
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTimeSlot, new IntentFilter(Common.KEY_DISPLAY_TIME_SLOT));
//        localBroadcastManager.registerReceiver(displayTimeSlot, new IntentFilter(Common.KEY_INFO_LOAD_DONE));

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_service_step3, container,false);
        unbinder = ButterKnife.bind(this, itemView);
        initView(itemView);

        return itemView;
    }

    private void initView(View itemView) {
        btn_date.setText(DD+"/"+(mm+1)+"/"+YYYY+" ("+dayOfWeek+")");

        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);

        recycler_time_slot.setLayoutManager(gridLayoutManager);

        recycler_time_slot.addItemDecoration(new SpacesItemDecoration(8));
    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList, Premise premiseInfo) {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext(), timeSlotList, premiseInfo);
        recycler_time_slot.setAdapter(adapter);

        dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadEmpty(List<TimeSlot> timeSlotList, Premise premiseInfo) {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext(), premiseInfo);
        recycler_time_slot.setAdapter(adapter);

        dialog.dismiss();
    }

    @Override
    public void onAllPremisesInfoLoadSuccess(Premise premiseInfo) {

    }

    @Override
    public void onAllPremisesInfoLoadFailed(String message) {

    }
}
