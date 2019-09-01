package com.example.docconnect.Common;

import android.content.Intent;
import android.os.Parcelable;

import com.example.docconnect.Model.Labor;
import com.example.docconnect.Model.Premise;
import com.example.docconnect.Model.Service;
import com.example.docconnect.Model.User;

import java.security.cert.CertPath;
import java.util.Calendar;

public class Common {

    public static final String API_RESTAURANT_ENDPOINT = "http://10.0.2.2:3000/";
    //10.161.90.71/MYPENLT-DLWKNQ2\TILIEWSQL
//    public static final String API_RESTAURANT_ENDPOINT = "http://10.0.2.2:3000/";
    public static final String API_KEY = "1234"; // We will hard code API key now. Secure it with Firebase remote  config
    public static final int APP_REQUEST_CODE = 1234;
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_PREMISE_SELECTED = "PREMISE_SELECTED";
    public static final String KEY_SERVICE_LOAD_DONE = "SERVICE_LOAD_DONE"; // Premise's Services
    public static final String KEY_INFO_LOAD_DONE = "INFO_LOAD_DONE";// Premise's Info
    public static final String KEY_LABOR_LOAD_DONE = "LABOR_LOAD_DONE"; // Labors
    public static final String KEY_LABOR_SELECTED = "LABOR_SELECTED";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT" ;
    public static final int TIME_SLOT_TOTAL = 11;
    public static final String KEY_TIME_SLOT = "TIME_SLOT";
    public static final Object DISABLE_TAG = "DISABLE"; //Disable time slot tag
    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING" ;
    public static boolean KEY_CHIP_SELECTED = false;
    public static boolean KEY_SPINNER_SELECTED = false;
    public static String IS_LOGIN = "IsLogin";
    public static User currentUser;
    public static Premise currentPremise; // This is used to register premise from AllPremises
    public static Premise currentPremiseTemp; // This is used to register premise from AllServices
    public static Labor currentLabor;
    public static String selectedService;
    public static int step = 0;
    public static String premise = ""; // This is premiseId
    public static String service = ""; // This is serviceId
    public static String labor = ""; // This is laborId
    public static String timeSlot;
    public static int currentTimeSlot = -1;
    public static Calendar bookingDate = Calendar.getInstance();;
    public static String openingHours;


    public static String convertTimeSlotToString(int slot) {
        switch (slot){
            case 0:
                return "9:00";
            case 1:
                return "10:00";
            case 2:
                return "11:00";
            case 3:
                return "12:00";
            case 4:
                return "13:00";
            case 5:
                return "14:00";
            case 6:
                return "15:00";
            case 7:
                return "16:00";
            case 8:
                return "17:00";
            case 9:
                return "18:00";
            case 10:
                return "19:00";
            default:
                return "Closed";
        }
    }


    public class KEY_STEP {
    }
}
