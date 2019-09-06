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
import android.widget.Toast;

import com.example.docconnect.Common.Common;
import com.example.docconnect.Model.BookingInformation;
import com.example.docconnect.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

    @OnClick(R.id.btn_confirm)
    void confirmBooking(){

        dialog.show();

        // DatabaseUtils.getAllCart(CartDatabase.getInstance(getContext()), this);

        compositeDisposable.add(cartDataSource.getAllItemFromCart(Common.currentUser.getPhoneNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItems -> {

                    // Part 31: Move onGetAllItemFromCartSuccess to here
                    // Process Timestamp
                    // We will use Timestamp to filter all booking with date is greater than today
                    // This is to display all the future booking
                    String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
                    String[] convertTime = startTime.split("-"); //Split (i.e 9:00 - 10:00)
                    //Get start time : get 9:00
                    String[] startTimeConvert = convertTime[0].split(":");
                    int startHourInt = Integer.parseInt(startTimeConvert[0].trim()); // we get 9
                    int startMinInt = Integer.parseInt(startTimeConvert[1].trim()); // we get 00

                    Calendar bookingDateWithOurHouse = Calendar.getInstance();
                    bookingDateWithOurHouse.setTimeInMillis(Common.bookingDate.getTimeInMillis());
                    bookingDateWithOurHouse.set(Calendar.HOUR_OF_DAY, startHourInt);
                    bookingDateWithOurHouse.set(Calendar.MINUTE, startMinInt);

                    // Create timestamp object and apply it to BookingInformation
                    Timestamp timestamp = new Timestamp(bookingDateWithOurHouse.getTime());

                    // Create booking information
                    final BookingInformation bookingInformation = new BookingInformation();

                    bookingInformation.setTimestamp(timestamp);
                    bookingInformation.setDone(false); //Always FALSE we will use this filed to filter for display on user
                    bookingInformation.setLaborID(Common.currentLabor.getLaborId());
                    bookingInformation.setLaborName(Common.currentLabor.getName());
                    bookingInformation.setCustomerName(Common.currentUser.getName());
                    bookingInformation.setCustomerPhone(Common.currentUser.getPhoneNumber());
                    bookingInformation.setPremiseID(Common.currentPremise.getPremiseId());
                    bookingInformation.setPremiseAddress(Common.currentPremise.getLocation());
                    bookingInformation.setPremiseName(Common.currentPremise.getName());
                    bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                            .append(" at ")
                            .append(simpleDateFormat.format(bookingDateWithOurHouse.getTime())).toString());
                    bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));
//                    bookingInformation.setCartItemList(cartItems); //Part 28: add cartItemList to BookingInfos

                    // Submit to Barber document
                    // /AllPremises/Premise1/AllLabors/Ali
                    final DocumentReference bookingDate = FirebaseFirestore.getInstance()
                            .collection("AllPremises")
                            .document(Common.currentPremise.getPremiseId())
                            .collection("AllLabors")
                            .document(Common.currentLabor.getLaborId())
                            .collection(Common.simpleDateFormat.format(Common.bookingDate.getTime()))
                            .document(String.valueOf(Common.currentTimeSlot));

                    // Write data

                    bookingDate.set(bookingInformation)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
                                    // Part 28: After successfully added to bookingInfo, delete cart
                                    // DatabaseUtils.clearCart(CartDatabase.getInstance(getContext()));
                                    //Part 31: Replace with line:
//                                    cartDataSource.clearCart(Common.currentUser.getPhoneNumber())
//                                            .subscribeOn(Schedulers.io())
//                                            .observeOn(AndroidSchedulers.mainThread())
//                                            .subscribe(new SingleObserver<Integer>() {
//                                                @Override
//                                                public void onSubscribe(Disposable d) {
//
//                                                }
//
//                                                @Override
//                                                public void onSuccess(Integer integer) {
//                                                    addToUserBooking(bookingInformation);
//                                                }
//
//                                                @Override
//                                                public void onError(Throwable e) {
//                                                    Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                }
//                                            });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });


                }, throwable -> {
                    Toast.makeText(getContext(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }
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
